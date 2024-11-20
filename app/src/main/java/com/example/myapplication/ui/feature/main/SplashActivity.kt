package com.example.myapplication.ui.feature.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.data.datastore.DataStoreManager
import com.example.myapplication.ui.feature.main.MainActivity
import com.example.myapplication.ui.feature.user.LoginActivity
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {

    private lateinit var dataStoreManager: DataStoreManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inisialisasi DataStoreManager
        dataStoreManager = DataStoreManager(this)

        // Memeriksa status login
        MainScope().launch {
            val isLoggedIn = dataStoreManager.isLoggedIn.first()
            if (isLoggedIn) {
                navigateToMainActivity()
            } else {
                navigateToLogin()
            }
        }
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // Tutup SplashActivity setelah berpindah ke MainActivity
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish() // Tutup SplashActivity setelah berpindah ke LoginActivity
    }
}
