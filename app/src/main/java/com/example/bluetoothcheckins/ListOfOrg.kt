package com.example.bluetoothcheckins


import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bluetoothcheckins.data.OrgListAdapter
import com.example.bluetoothcheckins.databinding.ActivityListOfOrgBinding
import com.example.bluetoothcheckins.model.Organizasion
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

class ListOfOrg : AppCompatActivity() {

    lateinit var gso: GoogleSignInOptions
    lateinit var gsc: GoogleSignInClient

    lateinit var binding: ActivityListOfOrgBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListOfOrgBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //recyclerView code
        var adapter: OrgListAdapter?= null
        var orgList: ArrayList<Organizasion>?=null
        var layoutManager:RecyclerView.LayoutManager?=null

        orgList = ArrayList<Organizasion>()
        layoutManager = LinearLayoutManager(this)
        //change package if does not work malek
        adapter = OrgListAdapter(orgList,this)

        binding.recycleOfOrg.layoutManager = layoutManager
        binding.recycleOfOrg.adapter = adapter

        val nameOfOrgList: Array<String> = arrayOf("couch potato","potato couch", "lazy machine", "Power Of AI",
            "kaka","mama","papa","gone","como astas")
        for (i in 0..(nameOfOrgList.size-1)){
            val org = Organizasion()
            org.organizasion = nameOfOrgList[i]
            org.lengthInDays = "30"
            orgList.add(org)
        }
        adapter.notifyDataSetChanged()

        //google sign in
        gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        gsc = GoogleSignIn.getClient(this,gso)

        var acct = GoogleSignIn.getLastSignedInAccount(this)
        if (acct!= null){
            var token = acct.idToken
            var acctName = acct.displayName
            var acctEmail = acct.email
        }

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

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
            // Add more cases for other menu items
            else -> super.onOptionsItemSelected(item)
        }
    }


}