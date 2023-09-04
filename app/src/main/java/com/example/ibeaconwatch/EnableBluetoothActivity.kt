package com.example.ibeaconwatch

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import java.lang.SecurityException

class EnableBluetoothActivity : Activity() {
    private val REQUEST_ENABLE_BT = 1 // Request code for enabling Bluetooth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {//TODO Call requires permission which may be rejected by user: code should explicitly check to see if permission is available (with `checkPermission`) or explicitly handle a potential `SecurityException`
            // Check if Bluetooth is already enabled
            val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
            if (bluetoothAdapter != null && !bluetoothAdapter.isEnabled) {
                // Bluetooth is not enabled, request the user to enable it
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
            } else {
                // Bluetooth is already enabled
                Log.d("EnableBluetoothActivity", "Bluetooth is enabled.")
                showToast("Bluetooth is enabled.")
                finish()
            }
        } catch (e: SecurityException) {
            // Handle the SecurityException here
            Log.e("EnableBluetoothActivity", "SecurityException: ${e.message}")
            showToast("Please enable Bluetooth permission in app settings.")
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                // Bluetooth has been successfully enabled
                Log.d("EnableBluetoothActivity", "Bluetooth is now enabled.")
            } else {
                // The user declined to enable Bluetooth
                Log.d("EnableBluetoothActivity", "Bluetooth enabling was canceled by the user.")
            }
            finish()
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
