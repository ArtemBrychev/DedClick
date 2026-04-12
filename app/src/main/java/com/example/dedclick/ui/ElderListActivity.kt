package com.example.dedclick.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.dedclick.data.AuthManager
import com.example.dedclick.databinding.ActivityElderHomeBinding
import com.example.dedclick.databinding.ActivityElderListBinding

class ElderListActivity : ComponentActivity(){
    private lateinit var binding: ActivityElderListBinding
    private lateinit var authManager: AuthManager

    override fun onCreate(savedInstanceStore: Bundle?){
        super.onCreate(savedInstanceStore)
        binding = ActivityElderListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        authManager = AuthManager(applicationContext)



    }
}