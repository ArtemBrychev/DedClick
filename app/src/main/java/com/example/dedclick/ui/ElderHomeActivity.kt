package com.example.dedclick.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.dedclick.databinding.ActivityElderHomeBinding

class ElderHomeActivity  : ComponentActivity(){
    private lateinit var binding: ActivityElderHomeBinding

    override fun onCreate(savedInstanceStore: Bundle?){
        super.onCreate(savedInstanceStore)

        binding = ActivityElderHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}