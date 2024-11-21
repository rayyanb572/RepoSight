package com.example.myapplication.ui.feature.main

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.data.datastore.DataStoreManager
import com.example.myapplication.ui.adapter.ChatViewModel
import com.example.myapplication.ui.feature.home.HomeActivity
import com.example.myapplication.ui.feature.user.LoginActivity
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var dataStoreManager: DataStoreManager
    private val viewModel: ChatViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dataStoreManager = DataStoreManager(this)

        lifecycleScope.launch {
            dataStoreManager.isLoggedIn.collect { isLoggedIn ->
                if (isLoggedIn) {
                    android.util.Log.d("MainActivity", "User is logged in")
                    Toast.makeText(this@MainActivity, "Login status: Logged in", Toast.LENGTH_SHORT).show()
                    navigateToHome()
                } else {
                    android.util.Log.d("MainActivity", "User is not logged in")
                    Toast.makeText(this@MainActivity, "Login status: Not logged in", Toast.LENGTH_SHORT).show()
                    navigateToLogin()
                }
            }
        }
    }

    private fun navigateToHome() {
        Toast.makeText(this, "Navigating to Home", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, HomeActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
    }

    private fun navigateToLogin() {
        Toast.makeText(this, "Navigating to Login", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
    }
}