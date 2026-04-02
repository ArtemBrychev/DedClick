package com.example.dedclick

import android.R
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.dedclick.databinding.ActivityStartBinding

class MainActivity : ComponentActivity() {

    private lateinit var binding: ActivityStartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val loginButton = binding.loginButton
        loginButton.setOnClickListener {
            binding.loginButtonText.text = "нажато"
        }

        val registerButton = binding.registerButton
        registerButton.setOnClickListener {
            binding.registerButtonText.text = "нажато"
        }
    }
}