package com.example.androidcse227.Unit5

import com.example.androidcse227.R
import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class WifiActivity : AppCompatActivity() {
    private lateinit var toggleWifi: Button
    private lateinit var scanWifi: Button
    private lateinit var lstView: ListView
    private val wifiList = ArrayList<String>()
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var wifiManager: WifiManager

    private val Reciever = object: BroadcastReceiver(){
        @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
        override fun onReceive(p0: Context?, p1: Intent?) {
            // Check if the action is what we expect
            if (p1?.action == WifiManager.SCAN_RESULTS_AVAILABLE_ACTION) {
                // Check for permission before accessing scan results
                if(ContextCompat.checkSelfPermission(this@WifiActivity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    wifiList.clear() // Clear previous results before adding new ones
                    val results = wifiManager.scanResults

                    for(network in results){
                        // Check if SSID is not empty (for hidden networks)
                        if (network.SSID != null && network.SSID.isNotEmpty()) {
                            wifiList.add(network.SSID)
                        }
                    }
                    adapter.notifyDataSetChanged()
                } else{
                    // If permission is missing, ask for it
                    askPermission()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_wifi)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize WifiManager early
        wifiManager = applicationContext.getSystemService(WIFI_SERVICE) as WifiManager

        toggleWifi = findViewById(R.id.button13)
        scanWifi = findViewById(R.id.button14)
        lstView = findViewById(R.id.listView2)

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, wifiList)
        lstView.adapter = adapter

        // --- CORRECTED TOGGLE BUTTON LOGIC ---
        toggleWifi.setOnClickListener {
            // In modern Android (10+), applications cannot directly turn ON/OFF Wi-Fi.
            // The best practice is to prompt the user to the Wi-Fi settings page.

            // The required permissions (CHANGE_WIFI_STATE) are often not enough for direct control.
            // We use an Intent to open the settings panel for the user to toggle manually.

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // For Android 10 (Q) and above, prompt the user to the Settings Panel
                val panelIntent = Intent(Settings.Panel.ACTION_WIFI)
                startActivity(panelIntent)
                Toast.makeText(this, "Please use the panel to toggle Wi-Fi.", Toast.LENGTH_LONG).show()
            } else {
                // For older versions, direct method can be attempted (requires CHANGE_WIFI_STATE permission)
                if(ContextCompat.checkSelfPermission(this, Manifest.permission.CHANGE_WIFI_STATE) == PackageManager.PERMISSION_GRANTED) {
                    val isEnabled = wifiManager.isWifiEnabled
                    wifiManager.isWifiEnabled = !isEnabled // Toggle the state
                    Toast.makeText(this, "Wi-Fi toggled.", Toast.LENGTH_SHORT).show()
                } else {
                    askPermission()
                    Toast.makeText(this, "Permissions are required to toggle Wi-Fi.", Toast.LENGTH_SHORT).show()
                }
            }
        }
        // --- END OF CORRECTION ---

        scanWifi.setOnClickListener {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                // Ensure Wi-Fi is enabled before scanning (The direct enable method is now restricted,
                // but we keep this check for older devices and to notify the user.)
                if(!wifiManager.isWifiEnabled) {
                    Toast.makeText(this, "Wi-Fi is OFF. Please toggle it ON first.", Toast.LENGTH_LONG).show()
                    // Prompt to toggle via settings panel for modern devices (Optional, can be removed if toggle button handles it)
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        startActivity(Intent(Settings.Panel.ACTION_WIFI))
                    }
                    return@setOnClickListener
                }

                // Wi-Fi is enabled, proceed with scan
                registerReceiver(Reciever, IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION))
                wifiManager.startScan()
                Toast.makeText(this, "Scanning Wifi...", Toast.LENGTH_SHORT).show()

            } else{
                askPermission()
                Toast.makeText(this, "Location permission is required for Wi-Fi scanning.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Unregister receiver to prevent memory leaks
        try {
            unregisterReceiver(Reciever)
        } catch (e: IllegalArgumentException) {
            // Receiver might not be registered if scan was never clicked
        }
    }
}

private fun WifiActivity.askPermission() {
    ActivityCompat.requestPermissions(
        this,
        arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION, // Required for scanning
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.CHANGE_WIFI_STATE // Required for toggling (in older APIs)
        ),
        101
    )
}