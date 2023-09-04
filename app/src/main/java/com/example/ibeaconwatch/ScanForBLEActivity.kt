package com.example.ibeaconwatch

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.RemoteException
import org.altbeacon.beacon.BeaconConsumer
import org.altbeacon.beacon.BeaconManager
import org.altbeacon.beacon.BeaconParser
import org.altbeacon.beacon.Region

class ScanForBLEActivity : Activity(), BeaconConsumer {
    private lateinit var beaconManager: BeaconManager

    private val scanningIntervalMillis: Long = 2000 // Scan every 2 seconds

    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize the BeaconManager
        beaconManager = BeaconManager.getInstanceForApplication(this)

        // Configure the beacon layout for iBeacons
        beaconManager.beaconParsers.add(BeaconParser().setBeaconLayout(BeaconParser.EDDYSTONE_UID_LAYOUT))

        // Bind the BeaconConsumer (this activity) to the BeaconService
        beaconManager.bind(this)
    }

    override fun onBeaconServiceConnect() {
        // Start scanning for BLE devices immediately
        startScanning()

        // Schedule scanning to occur at regular intervals
        handler.postDelayed({ startScanning() }, scanningIntervalMillis)
    }

    private fun startScanning() {
        // Set up the region for iBeacons you want to monitor
        val region = Region("myMonitoringUniqueId", null, null, null)

        // Set up the ranging listener
        beaconManager.addRangeNotifier { beacons, _ ->
            for (beacon in beacons) {
                // Handle iBeacon data, including UUID, major, minor, etc.
                val uuid = beacon.id1.toString()
                val major = beacon.id2.toString()
                val minor = beacon.id3.toString()

                // Print or process the iBeacon information as needed
                println("Detected iBeacon - UUID: $uuid, Major: $major, Minor: $minor")
            }
        }

        // Start ranging for beacons in the defined region
        try {
            beaconManager.startRangingBeaconsInRegion(region)
        } catch (e: RemoteException) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Unbind the BeaconConsumer when the activity is destroyed
        beaconManager.unbind(this)

        // Remove any pending scanning tasks
        handler.removeCallbacksAndMessages(null)
    }
}


