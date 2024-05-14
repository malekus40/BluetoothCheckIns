package com.example.bluetoothcheckins.model

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.Timestamp

class Attandent(name: String, bluetoothId: String, i: Int) : Parcelable{
    constructor() : this("", "",0)
    constructor(parcel: Parcel) : this (
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt() ?: 0

    ) {
        name = parcel.readString()
        bluetoothID = parcel.readString()
        daysAttended = parcel.readValue(kotlin.Int::class.java.classLoader) as? Int
    }

    var name: String?= name
    var bluetoothID: String?=bluetoothId
    var daysAttended: Int?=i
    var checked: Boolean?=false
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(bluetoothID)
        parcel.writeValue(daysAttended)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Attandent> {
        override fun createFromParcel(parcel: Parcel): Attandent {
            return Attandent(parcel)
        }

        override fun newArray(size: Int): Array<Attandent?> {
            return arrayOfNulls(size)
        }
    }
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Attandent) return false

        return this.bluetoothID == other.bluetoothID
    }

}