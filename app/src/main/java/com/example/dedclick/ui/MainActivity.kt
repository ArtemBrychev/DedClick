package com.example.dedclick.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import com.example.dedclick.data.AuthManager
import com.example.dedclick.data.management.RoleDataStoreManager
import com.example.dedclick.data.management.TokenDataStoreManager
import com.example.dedclick.databinding.ActivityStartBinding
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private lateinit var binding: ActivityStartBinding
    private lateinit var authManager: AuthManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStartBinding.inflate(layoutInflater)
        authManager = AuthManager(applicationContext)
        lifecycleScope.launch {

            val userInfo = authManager.getUserAuthInfo() ?: return@launch
            val intent = when (userInfo.role) {
                "elder" -> Intent(this@MainActivity, ElderHomeActivity::class.java)
                "trusted" -> Intent(this@MainActivity, TrustedHomeActivity::class.java)
                else -> return@launch
            }

            startActivity(intent)
            finish()
        }
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