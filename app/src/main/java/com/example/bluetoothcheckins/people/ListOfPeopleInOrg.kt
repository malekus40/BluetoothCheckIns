package com.example.bluetoothcheckins.people

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bluetoothcheckins.BluetoothController
import com.example.bluetoothcheckins.MainActivity
import com.example.bluetoothcheckins.NewDeviceListener
import com.example.bluetoothcheckins.R
import com.example.bluetoothcheckins.data.PeopleListAdapter
import com.example.bluetoothcheckins.databinding.ActivityListOfPeopleInOrgBinding
import com.example.bluetoothcheckins.model.Attandent
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

class ListOfPeopleInOrg : AppCompatActivity(),NewDeviceListener{

    lateinit var binding: ActivityListOfPeopleInOrgBinding
    lateinit var gsc: GoogleSignInClient
    var adapter: PeopleListAdapter?=null
    var attList: ArrayList<Attandent>?=null
    var layoutManager:RecyclerView.LayoutManager?=null
    var db = Firebase.firestore
    var userId = FirebaseAuth.getInstance().currentUser?.uid.toString()
    var orgName : String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListOfPeopleInOrgBinding.inflate(layoutInflater)
        setContentView(binding.root)

        orgName = intent.getStringExtra("orgName").toString()

        attList = ArrayList<Attandent>()
        layoutManager = LinearLayoutManager(this)
        adapter = PeopleListAdapter(attList!!, this)

        binding.recycleOfPeople.layoutManager = layoutManager
        binding.recycleOfPeople.adapter = adapter

        //change the title of toolbar
        binding.toolbar.title = intent.getStringExtra("orgName")

        binding.home.setOnClickListener{
            finish()
        }


        loadAttandants()

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_for_adding_people, menu)
        return true
    }
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            R.id.signOut -> {
                signOut()
                true
            }
            R.id.addMorePeople -> {
                addMorePeople()
                true
            }
            R.id.takeAttandance -> {
                scanForNewAttandants()
                true
            }

            // Add more cases for other menu items
            else -> super.onOptionsItemSelected(item)
        }
    }
    fun signOut() {
        gsc.signOut().addOnCompleteListener(this,{startActivity(Intent(this, MainActivity::class.java))})
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1002 && resultCode == Activity.RESULT_OK) {
            val itemName = data?.getStringExtra("org")
            val newDevices : ArrayList<Attandent>? = data?.getParcelableArrayListExtra("newDevices")
            if (newDevices != null) {
                for (att in newDevices){
                    attList?.add(att)
                    Log.d("NewDeviceAdded", "added a device")
                }
                adapter?.notifyDataSetChanged()
            }
        }
    }

    fun addMorePeople(){
        var intent = Intent(this, AddMorePeople::class.java)
        intent.putExtra("orgName",binding.toolbar.title)
        startActivityForResult(intent,1002)
    }

    fun loadAttandants() {
        db.collection(userId).whereEqualTo("meetingName",orgName).get()
            .addOnSuccessListener { documents ->
                for (doc in documents){
                    db.collection(userId).document(doc.id).collection("attendants").get()
                        .addOnSuccessListener { documents ->
                            for (doc in documents){
                                val attandent = Attandent();
                                attandent.name = doc.data["name"].toString()
                                attandent.bluetoothID = doc.data["bluetoothId"].toString()
                                attandent.daysAttended = doc.data["daysAttanded"].toString().toInt()
                                if(wasItUpdatedToday(doc.getTimestamp("lastDateAttanded"))){
                                    attandent.checked = true
                                }
                                attList?.add(attandent)
                            }
                            adapter?.notifyDataSetChanged()
                        }.addOnFailureListener{
                            Log.d("loadingSomeone", "loading unsuccessfully")
                        }
                }
            }
    }

    override fun newDevice(name: String, bluetoothId: String) {
        val foundAttandent = Attandent(name, bluetoothId, 0)
        if (attList?.contains(foundAttandent)!!) {
            db.collection(userId).whereEqualTo("meetingName", orgName).get()
                .addOnSuccessListener { documents ->
                    for (doc in documents) {
                        db.collection(userId).document(doc.id).collection("attendants")
                            .whereEqualTo("bluetoothId", bluetoothId).get()
                            .addOnSuccessListener { documents ->
                                for (doc2 in documents) {
                                    if (!wasItUpdatedToday(doc2.getTimestamp("lastDateAttanded"))){
                                        wasItUpdatedToday(doc2.getTimestamp("lastDateAttanded"))
                                        val increateAttandentDay = doc2.data["daysAttanded"].toString().toInt() + 1
                                        db.collection(userId).document(doc.id)
                                            .collection("attendants")
                                            .document(doc2.id).update(
                                                "daysAttanded",
                                                (increateAttandentDay)
                                            )
                                        db.collection(userId).document(doc.id)
                                            .collection("attendants")
                                            .document(doc2.id).update(
                                                "lastDateAttanded",
                                                (Timestamp.now())
                                            )
                                    }

                                }
                                attList!!.clear()
                                loadAttandants()
                            }
                    }
                }

        }
    }
    @RequiresApi(Build.VERSION_CODES.S)
    private fun scanForNewAttandants() {
        var bluetoothScanner = BluetoothController(this, this)
        bluetoothScanner.enable()
        bluetoothScanner.scanBT()
    }

    fun wasItUpdatedToday(timestamp: Timestamp?): Boolean {
        if (timestamp != null) {
            val dif =  Timestamp.now().toDate().time - timestamp.toDate().time
            val oneDay = 86400000L
            if ( dif <= oneDay){
                return true
            }
        }
        return false
    }



}