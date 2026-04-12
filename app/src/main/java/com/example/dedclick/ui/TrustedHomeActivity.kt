package com.example.dedclick.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.example.dedclick.data.AuthManager
import com.example.dedclick.databinding.ActivityTrustedHomeBinding
import kotlinx.coroutines.launch

class TrustedHomeActivity  : ComponentActivity(){
    private lateinit var binding: ActivityTrustedHomeBinding
    private lateinit var authManager: AuthManager

    override fun onCreate(savedInstanceStore: Bundle?){
        super.onCreate(savedInstanceStore)

        binding = ActivityTrustedHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        authManager = AuthManager(applicationContext)


        binding.logoutButton.setOnClickListener {
            lifecycleScope.launch {
                authManager.clearAuthInfo()
                startActivity(Intent(this@TrustedHomeActivity, MainActivity::class.java))
                finish()
            }
        }

        binding.eldersButton.setOnClickListener {
            startActivity(Intent(this@TrustedHomeActivity, ElderListActivity::class.java))
            finish()
        }
    }

}