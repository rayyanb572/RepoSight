package com.example.myapplication.ui.feature.user

import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myapplication.databinding.ActivityLoginBinding
import android.content.Intent
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.R
import com.example.myapplication.data.datastore.DataStoreManager
import com.example.myapplication.ui.feature.main.MainActivity
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: AuthViewModel by viewModels()
    private lateinit var dataStoreManager: DataStoreManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dataStoreManager = DataStoreManager(this)

        val signupLink = findViewById<TextView>(R.id.signupLink)
        signupLink.setOnClickListener {
            navigateToSignup()
        }

        setupViews()
        observeAuthState()
    }

    private fun setupViews() {
        binding.loginButton.setOnClickListener {
            val username = binding.emailInput.text.toString().trim()
            val password = binding.passwordInput.text.toString().trim()

            if (username.isNotEmpty() && password.isNotEmpty()) {

                lifecycleScope.launch {
                    dataStoreManager.setLoggedIn(true)
                    navigateToMainActivity()
                }
            } else {
                Toast.makeText(this, "Please enter valid credentials", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun observeAuthState() {
        viewModel.authState.observe(this) { state ->
            when (state) {
                is AuthViewModel.AuthState.Loading -> {
                    binding.loginButton.isEnabled = false
                }

                is AuthViewModel.AuthState.Success -> {
                    navigateToMainActivity()
                }

                is AuthViewModel.AuthState.Error -> {
                    binding.loginButton.isEnabled = true
                    showToast(state.message)
                }

                else -> {
                    binding.loginButton.isEnabled = true
                }
            }
        }
    }

    private fun navigateToSignup() {
        val intent = Intent(this, SignupActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // Close LoginActivity to prevent returning to it
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}