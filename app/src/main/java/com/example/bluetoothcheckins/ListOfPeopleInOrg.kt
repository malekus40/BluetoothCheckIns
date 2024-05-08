package com.example.bluetoothcheckins

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.bluetoothcheckins.databinding.ActivityListOfOrgBinding
import com.example.bluetoothcheckins.databinding.ActivityListOfPeopleInOrgBinding

class ListOfPeopleInOrg : AppCompatActivity() {

    lateinit var binding: ActivityListOfPeopleInOrgBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListOfPeopleInOrgBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}