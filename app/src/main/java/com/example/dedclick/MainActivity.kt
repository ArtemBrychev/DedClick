package com.example.dedclick

import android.R
import android.content.Intent
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
            val intent = Intent(this, LoginActivity::class.java)
            intent.putExtra("previous", MainActivity::class.java)
            startActivity(intent)
        }

        val registerButton = binding.registerButton
        registerButton.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            intent.putExtra("previous", MainActivity::class.java)
            startActivity(intent)
        }
    }
}