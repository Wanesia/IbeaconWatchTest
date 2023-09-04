package com.example.ibeaconwatch

import android.app.Activity
import android.content.Intent
import android.os.Bundle

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Start scanning for iBeacons when the app launches
        val scanIntent = Intent("your.package.name.ACTION_SCAN_BLE_DEVICES")
        startActivity(scanIntent)
    }
}
