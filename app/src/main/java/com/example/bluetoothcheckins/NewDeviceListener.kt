package com.example.bluetoothcheckins

interface NewDeviceListener {
    fun newDevice(name:String, bluetoothId: String)
}