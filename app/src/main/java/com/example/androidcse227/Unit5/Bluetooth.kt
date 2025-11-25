package com.example.androidcse227.Unit5

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.androidcse227.R
import android.Manifest
import androidx.annotation.RequiresPermission

class Bluetooth : AppCompatActivity() {

    private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    private val foundList = ArrayList<String>()
    private val pairedList = ArrayList<String>()

    private lateinit var btnToggle: Button
    private lateinit var btnScan: Button
    private lateinit var lvFound: ListView
    private lateinit var lvPaired: ListView
    private lateinit var foundAdapter: ArrayAdapter<String>
    private lateinit var pairedAdapter: ArrayAdapter<String>

    private lateinit var permLauncher: ActivityResultLauncher<Array<String>>
    private lateinit var enableLauncher: ActivityResultLauncher<Intent>
    private var receiverRegistered = false

    private val discoveryReceiver = object : BroadcastReceiver() {
        @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
        override fun onReceive(context: Context?, intent: Intent?) {
            when (intent?.action) {
                BluetoothDevice.ACTION_FOUND -> {
                    val device: BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    val entry = "${device?.name ?: "Unknown"}\n${device?.address ?: "N/A"}"
                    if (!foundList.contains(entry)) {
                        foundList.add(entry)
                        foundAdapter.notifyDataSetChanged()
                    }
                }
                BluetoothAdapter.ACTION_DISCOVERY_STARTED -> showToast("Discovery started")
                BluetoothAdapter.ACTION_DISCOVERY_FINISHED -> showToast("Discovery finished")
                BluetoothDevice.ACTION_BOND_STATE_CHANGED -> showToast("Bond state changed")
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bluetooth)

        btnToggle = findViewById(R.id.buttonToggle)
        btnScan = findViewById(R.id.buttonScan)
        lvFound = findViewById(R.id.listFound)
        lvPaired = findViewById(R.id.listPaired)

        foundAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, foundList)
        pairedAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, pairedList)
        lvFound.adapter = foundAdapter
        lvPaired.adapter = pairedAdapter

        permLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { perms ->
            val ok = perms.values.all { it }
            if (ok) loadPairedDevices() else showToast("Permissions required for Bluetooth")
        }

        enableLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { res ->
            if (res.resultCode == Activity.RESULT_OK) { showToast("Bluetooth enabled"); loadPairedDevices() }
            else showToast("Bluetooth not enabled")
        }

        btnToggle.setOnClickListener { toggleBluetooth() }
        btnScan.setOnClickListener { toggleDiscovery() }

        lvFound.setOnItemClickListener { _, _, pos, _ ->
            val addr = foundList[pos].substringAfterLast("\n")
            attemptPair(addr)
        }

        requestPermissionsIfNeeded()
    }

    private fun requestPermissionsIfNeeded() {
        val needed = arrayOf(
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT
        ).filter { ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED }
            .toTypedArray()

        if (needed.isNotEmpty()) permLauncher.launch(needed) else loadPairedDevices()
    }

    private fun hasPermission(p: String) =
        ContextCompat.checkSelfPermission(this, p) == PackageManager.PERMISSION_GRANTED

    private fun loadPairedDevices() {
        if (!hasPermission(Manifest.permission.BLUETOOTH_CONNECT)) {
            showToast("BLUETOOTH_CONNECT needed to read paired devices")
            return
        }
        pairedList.clear()
        try {
            bluetoothAdapter?.bondedDevices?.forEach {
                pairedList.add("${it.name ?: "Unknown"}\n${it.address}")
            }
            pairedAdapter.notifyDataSetChanged()
        } catch (se: SecurityException) {
            showToast("Cannot access paired devices (SecurityException)")
        }
    }

    private fun toggleBluetooth() {
        if (bluetoothAdapter == null) { showToast("Device has no Bluetooth"); return }
        if (!bluetoothAdapter.isEnabled) {
            enableLauncher.launch(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))
        } else {
            if (!hasPermission(Manifest.permission.BLUETOOTH_CONNECT)) {
                showToast("BLUETOOTH_CONNECT needed to disable Bluetooth")
                requestPermissionsIfNeeded()
                return
            }
            try {
                bluetoothAdapter.disable()
                showToast("Turning Bluetooth off")
            } catch (se: SecurityException) {
                showToast("SecurityException while disabling Bluetooth")
            }
        }
    }

    private fun toggleDiscovery() {
        if (bluetoothAdapter == null) { showToast("Device has no Bluetooth"); return }

        // Explicit check so Android Lint knows the permission is handled
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            showToast("BLUETOOTH_SCAN required to discover")
            requestPermissionsIfNeeded()
            return
        }

        if (bluetoothAdapter.isDiscovering) {
            try {
                bluetoothAdapter.cancelDiscovery()
                unregisterReceiverSafe()
                showToast("Discovery canceled")
            } catch (se: SecurityException) {
                showToast("SecurityException when canceling discovery")
            }
        } else {
            foundList.clear(); foundAdapter.notifyDataSetChanged()
            val filter = IntentFilter().apply {
                addAction(BluetoothDevice.ACTION_FOUND)
                addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
                addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)
                addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED)
            }
            try {
                registerReceiver(discoveryReceiver, filter)
                receiverRegistered = true
                val started = bluetoothAdapter.startDiscovery()
                showToast(if (started) "Scanning..." else "Could not start scanning")
            } catch (se: SecurityException) {
                showToast("SecurityException when starting discovery")
            }
        }
    }


    private fun attemptPair(address: String) {
        if (!hasPermission(Manifest.permission.BLUETOOTH_CONNECT)) {
            showToast("BLUETOOTH_CONNECT required to pair")
            requestPermissionsIfNeeded()
            return
        }
        try {
            val device = bluetoothAdapter?.getRemoteDevice(address) ?: run { showToast("Device not found"); return }
            if (device.bondState == BluetoothDevice.BOND_NONE) {
                device.createBond()
                showToast("Pairing started: $address")
            } else showToast("Already paired: ${device.name}")
        } catch (se: SecurityException) {
            showToast("SecurityException during pairing")
        } catch (iae: IllegalArgumentException) {
            showToast("Invalid address: $address")
        }
    }

    private fun unregisterReceiverSafe() {
        if (receiverRegistered) {
            try { unregisterReceiver(discoveryReceiver) } catch (_: Exception) {}
            receiverRegistered = false
        }
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_SCAN)
    override fun onDestroy() {
        super.onDestroy()
        if (bluetoothAdapter?.isDiscovering == true) {
            try { bluetoothAdapter.cancelDiscovery() } catch (_: Exception) {}
        }
        unregisterReceiverSafe()
    }

    private fun showToast(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}
