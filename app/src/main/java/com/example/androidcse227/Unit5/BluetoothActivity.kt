package com.example.androidcse227.Unit5

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.androidcse227.R

class BluetoothActivity : AppCompatActivity() {
    private lateinit var btToggle: Button
    private lateinit var btScan: Button
    private lateinit var lstView: ListView
    private val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    private val btDevice = ArrayList<String>()
    private lateinit var adapter: ArrayAdapter<String>

    private val reciever = object: BroadcastReceiver(){
        @RequiresApi(Build.VERSION_CODES.S)
        override fun onReceive(p0: Context?, p1: Intent?) {
            if(p1?.action == BluetoothDevice.ACTION_FOUND){
                val device = p1.getParcelableExtra<BluetoothDevice>(BluetoothDevice.EXTRA_DEVICE)
                // Check BLUETOOTH_CONNECT permission before accessing device details
                if(ContextCompat.checkSelfPermission(this@BluetoothActivity, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED){
                    // Only run this block if device is not null
                    device?.let {
                        val name = it.name ?: "Unknown Device" // Handle null name
                        val address = it.address.toString()
                        btDevice.add("$name\n$address")
                        adapter.notifyDataSetChanged()
                    }
                } else{
                    // If permission is missing during a scan, we can only request it again
                    // The receiver should ideally not handle permission requests, but we'll leave askPermission() here for now.
                    askPermission()
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_bluetooth2)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btToggle = findViewById(R.id.button11)
        btScan = findViewById(R.id.button12)
        lstView = findViewById(R.id.lstView)

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, btDevice)
        lstView.adapter = adapter

        // --- CORRECTED BT TOGGLE LISTENER ---
        btToggle.setOnClickListener {
            if (bluetoothAdapter == null) {
                Toast.makeText(this, "Bluetooth doesn't exist on this device.", Toast.LENGTH_SHORT).show()
            } else {
                // 1. Check for BLUETOOTH_CONNECT permission
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED) {
                    // Permission is granted, now try to toggle Bluetooth
                    if (bluetoothAdapter.isEnabled) {
                        Toast.makeText(this, "Bluetooth is already ON.", Toast.LENGTH_SHORT).show()
                        // NOTE: If you wanted to turn it OFF, you'd use: bluetoothAdapter.disable()
                        // but this method is often restricted in modern Android versions (API 33+)
                    } else {
                        val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                        startActivity(enableBtIntent)
                        Toast.makeText(this, "Requesting to turn on Bluetooth...", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    askPermission()
                    Toast.makeText(this, "Permission is mandatory for toggling Bluetooth.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        btScan.setOnClickListener @androidx.annotation.RequiresPermission(android.Manifest.permission.BLUETOOTH_CONNECT) {
            if(bluetoothAdapter == null){
                Toast.makeText(this, "Bluetooth doesn't exist", Toast.LENGTH_SHORT).show()
            } else{
                // Check if Bluetooth is enabled and permissions are granted before scanning
                if(ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) == PackageManager.PERMISSION_GRANTED && bluetoothAdapter.isEnabled){
                    // Clear previous list before scanning
                    btDevice.clear()
                    adapter.notifyDataSetChanged()

                    registerReceiver(reciever, IntentFilter(BluetoothDevice.ACTION_FOUND))

                    bluetoothAdapter.startDiscovery()
                    Toast.makeText(this, "Scanning...", Toast.LENGTH_SHORT).show()
                } else if (!bluetoothAdapter.isEnabled){
                    // If Bluetooth is off, prompt user to turn it on
                    Toast.makeText(this, "Bluetooth is OFF. Please toggle it ON first.", Toast.LENGTH_SHORT).show()
                }
                else{
                    // If permissions are missing, ask for them
                    askPermission()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Ensure receiver is unregistered to prevent memory leaks
        unregisterReceiver(reciever)
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun askPermission(){
        ActivityCompat.requestPermissions(this,
            arrayOf(
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.ACCESS_FINE_LOCATION
            ), 101)
    }
}