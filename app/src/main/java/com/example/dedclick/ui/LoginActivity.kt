package com.example.dedclick.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.dedclick.databinding.ActivityLoginBinding

class LoginActivity : ComponentActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceStore: Bundle?){
        super.onCreate(savedInstanceStore)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val priveousButton = binding.priveousButton
        priveousButton.setOnClickListener {
            val intentNew = Intent(this, MainActivity::class.java)
            startActivity(intentNew)
        }
    }


}