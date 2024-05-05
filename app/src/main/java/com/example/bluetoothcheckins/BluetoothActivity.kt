package com.example.bluetoothcheckins


import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bluetoothcheckins.databinding.ActivityBluetoothBinding
import com.example.bluetoothcheckins.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

class BluetoothActivity : AppCompatActivity() {

    lateinit var gso: GoogleSignInOptions
    lateinit var gsc: GoogleSignInClient

    lateinit var binding: ActivityBluetoothBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBluetoothBinding.inflate(layoutInflater)
        setContentView(binding.root)

        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        gsc = GoogleSignIn.getClient(this,gso)

        var acct = GoogleSignIn.getLastSignedInAccount(this)
        if (acct!= null){
            var token = acct.idToken
            var acctName = acct.displayName
            var acctEmail = acct.email
            binding.name.setText(token)
            binding.email.setText(acctEmail)
        }
        binding.signOutBtn.setOnClickListener{
            signOut()
        }

    }
    fun signOut() {
        gsc.signOut().addOnCompleteListener(this,{startActivity(Intent(this, MainActivity::class.java))})
    }
}