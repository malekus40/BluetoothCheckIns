package com.example.bluetoothcheckins.people

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bluetoothcheckins.AddDeviceListener
import com.example.bluetoothcheckins.BluetoothController
import com.example.bluetoothcheckins.NewDeviceListener
import com.example.bluetoothcheckins.data.ScanerAdapter
import com.example.bluetoothcheckins.databinding.ActivityAddMorePeopleBinding
import com.example.bluetoothcheckins.model.Attandent
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.delay

class AddMorePeople : AppCompatActivity(), NewDeviceListener, AddDeviceListener {

    lateinit var binding: ActivityAddMorePeopleBinding
    lateinit var adapter: ScanerAdapter
    lateinit var addNewAttandentList: ArrayList<Attandent>
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var orgName: String
    var db = Firebase.firestore
    var userId = FirebaseAuth.getInstance().currentUser?.uid.toString()

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddMorePeopleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        addNewAttandentList = ArrayList<Attandent>()
        layoutManager = LinearLayoutManager(this)
        //change package if does not work malek
        adapter = ScanerAdapter(addNewAttandentList,this,this)

        binding.addMorePeopleRecycler.layoutManager = layoutManager
        binding.addMorePeopleRecycler.adapter = adapter
        orgName = intent.getStringExtra("orgName").toString()

        binding.scan.setOnClickListener {
            addNewAttandentList.clear()
            scanForNewAttandants()

        }
        binding.done.setOnClickListener {
            val resultIntent = Intent().apply {
                putParcelableArrayListExtra("newDevices", addNewAttandentList)
            }
            setResult(Activity.RESULT_OK, resultIntent)

            finish()
        }
    }
    @RequiresApi(Build.VERSION_CODES.S)
    private fun scanForNewAttandants() {
        var bluetoothScanner = BluetoothController(this, this)
        bluetoothScanner.enable()
        bluetoothScanner.scanBT()
    }
    override fun newDevice(name:String, bluetoothId: String){
        val newAttandent = Attandent(name,bluetoothId,0)
        if (!addNewAttandentList.contains(newAttandent)){

            addNewAttandentList.add(newAttandent)
        }else{
        }
        adapter.notifyDataSetChanged()
    }

    override fun addDeviceToDB(name: String, bluetoothId: String) {
        Log.d("addingNewPersonToDB", "lets see")

        db.collection(userId).whereEqualTo("meetingName", orgName).get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    // Get the reference to the document
                    val docRef = db.collection(userId).document(document.id).collection("attendants").document()

                    // Define the data to add to the document
                    val newData = hashMapOf(
                        "name" to name,
                        "bluetoothId" to bluetoothId,
                        "daysAttanded" to 1,
                        "lastDateAttanded" to Timestamp.now()
                    )

                    // Update the document with the new data
                    docRef.set(newData)
                        .addOnSuccessListener {
                            // Handle success
                            println("Document updated successfully.")
                        }
                        .addOnFailureListener { e ->
                            // Handle errors
                            println("Error updating document: $e")
                        }
                }
            }
    }

}