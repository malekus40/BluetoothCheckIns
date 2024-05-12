package com.example.bluetoothcheckins.org

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.bluetoothcheckins.R
import com.example.bluetoothcheckins.databinding.ActivityAddOrgBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore

class AddOrg : AppCompatActivity() {
    var db = Firebase.firestore
    lateinit var binding: ActivityAddOrgBinding
    lateinit var userId: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddOrgBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d("Hello","I dont know what to day")

        userId = FirebaseAuth.getInstance().currentUser?.uid.toString()


        binding.create.setOnClickListener{
            if (userId != null) {
                createNewOrg(userId)
            }
        }
    }

    fun createNewOrg(uid:String){

        var org = binding.newName.text.toString()
        var days = binding.days.text.toString()
        if (!org.trim().equals("")  && !days.trim().equals("")){
            val currUser = uid
            val meetingData = hashMapOf(
                "meetingName" to org,
                "lengthOfTheMeeting" to days
            )

            db.collection(currUser)
                .add(meetingData)
                .addOnSuccessListener { _ ->
                    val resultIntent = Intent().apply {
                        putExtra("org", org)
                        putExtra("days",days)
                    }
                    setResult(Activity.RESULT_OK, resultIntent)
                    finish()
                }.addOnFailureListener { e ->
                }
        } else {
            Snackbar.make(findViewById(R.id.create),"Wrong input",Snackbar.LENGTH_LONG).show()
        }

    }
}