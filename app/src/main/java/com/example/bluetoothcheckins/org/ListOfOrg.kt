package com.example.bluetoothcheckins.org


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bluetoothcheckins.MainActivity
import com.example.bluetoothcheckins.OnDataChangeListener
import com.example.bluetoothcheckins.R
import com.example.bluetoothcheckins.data.OrgListAdapter
import com.example.bluetoothcheckins.databinding.ActivityListOfOrgBinding
import com.example.bluetoothcheckins.model.Organizasion
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

class ListOfOrg : AppCompatActivity(), OnDataChangeListener {

    lateinit var gsc: GoogleSignInClient

    lateinit var binding: ActivityListOfOrgBinding
    lateinit var adapter: OrgListAdapter
    lateinit var orgList: ArrayList<Organizasion>
    lateinit var layoutManager:RecyclerView.LayoutManager

    var db = Firebase.firestore
    var userId = FirebaseAuth.getInstance().currentUser?.uid.toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListOfOrgBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //recyclerView code

        orgList = ArrayList<Organizasion>()
        layoutManager = LinearLayoutManager(this)
        //change package if does not work malek
        adapter = OrgListAdapter(orgList,this, this)

        binding.recycleOfOrg.layoutManager = layoutManager
        binding.recycleOfOrg.adapter = adapter

        getAllOrg()
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)


    }
    fun getAllOrg(){
        db.collection(userId).get()
            .addOnSuccessListener { documents ->
                for (doc in documents){
                    val org = Organizasion()
                    org.lengthInDays = doc.data.get("lengthOfTheMeeting").toString()
                    org.organizasion = doc.data.get("meetingName").toString()
                    orgList.add(org)
                    adapter.notifyDataSetChanged()
                }
            }
    }
    fun signOut() {
        gsc.signOut().addOnCompleteListener(this,{startActivity(Intent(this, MainActivity::class.java))})
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            R.id.signOut -> {
                signOut()
                true
            }
            R.id.addMeetingId -> {
                addOrg()
                true
            }

            // Add more cases for other menu items
            else -> super.onOptionsItemSelected(item)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1001 && resultCode == Activity.RESULT_OK) {
            val itemName = data?.getStringExtra("org")
            val itemDays = data?.getStringExtra("days")
            val newItem = Organizasion()
            newItem.organizasion = itemName
            newItem.lengthInDays = itemDays
            orgList.add(newItem)
            adapter.notifyDataSetChanged()

        }
    }
    fun addOrg(){
        var intent = Intent(this, AddOrg::class.java)
        startActivityForResult(intent,1001)


    }

    override fun onDataChanged(org: String) {
        orgList.removeIf{it.organizasion == org}
        adapter.notifyDataSetChanged()
    }

    
}