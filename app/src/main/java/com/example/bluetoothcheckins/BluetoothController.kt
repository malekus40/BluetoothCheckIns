package com.example.bluetoothcheckins

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

class BluetoothController(private val context: Context, val listener: NewDeviceListener) : AppCompatActivity() {


    val bluetoothManager: BluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    val bluetoothAdapter: BluetoothAdapter = bluetoothManager.adapter
    val discoveredDevices: MutableSet<BluetoothDevice> = mutableSetOf()


     fun enable(){

        if (!bluetoothAdapter.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)

            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.BLUETOOTH
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(context as Activity ,
                    arrayOf(Manifest.permission.BLUETOOTH),1)
            }
            startActivityForResult(enableBtIntent, 1)
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun scanBT(){
        val filter = IntentFilter()
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED)
        filter.addAction(BluetoothDevice.ACTION_FOUND)
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED)
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),1)
        }
        context.registerReceiver(receiver, filter)

        bluetoothAdapter.startDiscovery()

    }

    private val receiver = object : BroadcastReceiver() {

        @SuppressLint("MissingPermission")
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            when(action) {
                BluetoothAdapter.ACTION_STATE_CHANGED ->{
                    Log.d("discoverDevices1","STATE CHANGED")
                }
                BluetoothAdapter.ACTION_DISCOVERY_STARTED ->{
                    Log.d("discoverDevices2","Discovery Started")
                }
                BluetoothAdapter.ACTION_DISCOVERY_FINISHED ->{
                    Log.d("discoverDevices3","Discovery Finished")
                    discoveredDevices.clear()
                }
                BluetoothDevice.ACTION_FOUND -> {
                    val device: BluetoothDevice? = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    if (device?.name != null){
                        if (!discoveredDevices.contains(device)){
                            discoveredDevices.add(device)
                            listener.newDevice(device.name,device.address)
                            device.let {
                                Log.d("foundDevice", "device: ${device.name} : ${device.address} is checked in")
                            }
                        }
                    }

                }
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }
}