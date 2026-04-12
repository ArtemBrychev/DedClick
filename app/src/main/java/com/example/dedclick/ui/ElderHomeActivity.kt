package com.example.dedclick.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.example.dedclick.data.AuthManager
import com.example.dedclick.databinding.ActivityElderHomeBinding
import kotlinx.coroutines.launch

class ElderHomeActivity  : ComponentActivity(){
    private lateinit var binding: ActivityElderHomeBinding
    private lateinit var authManager: AuthManager

    override fun onCreate(savedInstanceStore: Bundle?){
        super.onCreate(savedInstanceStore)

        binding = ActivityElderHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authManager = AuthManager(applicationContext)

        lifecycleScope.launch {
            val user = authManager.getUserAuthInfo()
            binding.titleTop.text = "Здравствуй, ${user?.name ?: "пользователь"}!"
        }


        binding.trustedListButton.setOnClickListener {
            val intent = Intent(this, TrustedContactsActivity::class.java)
            startActivity(intent)
            finish()
        }


        binding.logoutButton.setOnClickListener {
            lifecycleScope.launch {
                authManager.clearAuthInfo()
                startActivity(Intent(this@ElderHomeActivity, MainActivity::class.java))
            }
        }
    }

}