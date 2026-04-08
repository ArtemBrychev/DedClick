package com.example.dedclick.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.dedclick.databinding.ActivityTrustedHomeBinding

class TrustedHomeActivity  : ComponentActivity(){
    private lateinit var binding: ActivityTrustedHomeBinding

    override fun onCreate(savedInstanceStore: Bundle?){
        super.onCreate(savedInstanceStore)

        binding = ActivityTrustedHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}