package com.example.bluetoothcheckins

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.bluetoothcheckins.databinding.ActivityMainBinding
import com.example.bluetoothcheckins.org.ListOfOrg
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.firestore

class MainActivity : AppCompatActivity() {


    lateinit var gso: GoogleSignInOptions
    lateinit var gsc: GoogleSignInClient
    lateinit var gBtn: SignInButton
    lateinit var auth : FirebaseAuth
    var db = Firebase.firestore
    val USERS_COLLECTION = "users"
    val MEETINGS_COLLECTION = "meetings"
    val ATTENDANCES_COLLECTION = "attendances"

    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        gBtn = binding.googleBtn
        gBtn.setOnClickListener{
            gSignIn()
        }

        gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("406354242650-0duu2imbmfser8sp71tpkh6f7iplli5r.apps.googleusercontent.com")
            .requestEmail()
            .build()
        gsc = GoogleSignIn.getClient(this,gso)

        var acct = GoogleSignIn.getLastSignedInAccount(applicationContext)
        if (acct != null) {
            handleGoogleAccessToken(acct.idToken)
            navigateToSecondActivity()
        }

    }

    private fun handleGoogleAccessToken(token: String?){
        val idToken = token
        when {
            idToken != null -> {
                // Got an ID token from Google. Use it to authenticate
                // with Firebase.
                val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                auth.signInWithCredential(firebaseCredential)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("login", "signInWithCredential:success")
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("login", "signInWithCredential:failure", task.exception)
                        }
                    }
            }
            else -> {
                // Shouldn't happen.
                Log.d(TAG, "No ID token!")
            }
        }
    }
    fun gSignIn() {
        var signInIntent = gsc.getSignInIntent()

        startActivityForResult(signInIntent, 1000)
    }
    fun navigateToSecondActivity() {
        finish()
        var intent = Intent(this@MainActivity, ListOfOrg::class.java)
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1000){
            var task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                task.getResult(ApiException::class.java)
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
                navigateToSecondActivity()

            } catch (e: ApiException){
                Toast.makeText(applicationContext, "Something went wrong", Toast.LENGTH_SHORT).show()
            }
        }

    }
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    // Here you can handle what to do after successful sign-in
                    // For example, navigate to another activity, etc.
                } else {
                    // If sign-in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    // Here you can handle the failure scenario
                }
            }
    }

    data class User(val userId: String)
    data class Meeting(val meetingId: String, val userId: String, val name: String, val lengthOfTheMeeting: Int)
    data class Attendance(val attendanceId: String, val name: String, val bluetoothId: String, val meetingId: String)

    fun addUser(user: User) {
        db.collection(USERS_COLLECTION)
            .document(user.userId)
            .set(user)
            .addOnSuccessListener { println("User added successfully") }
            .addOnFailureListener { e -> println("Error adding user: $e") }
    }

    // Function to add meeting to Firestore
    fun addMeeting(meeting: Meeting) {
        db.collection(MEETINGS_COLLECTION)
            .document(meeting.meetingId)
            .set(meeting)
            .addOnSuccessListener { println("Meeting added successfully") }
            .addOnFailureListener { e -> println("Error adding meeting: $e") }
    }

    // Function to add attendance to Firestore
    fun addAttendance(attendance: Attendance) {
        db.collection(ATTENDANCES_COLLECTION)
            .document(attendance.attendanceId)
            .set(attendance)
            .addOnSuccessListener { println("Attendance added successfully") }
            .addOnFailureListener { e -> println("Error adding attendance: $e") }

    }
}



