package com.example.myapplication.ui.feature.user

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.R
import com.example.myapplication.data.datastore.DataStoreManager
import com.example.myapplication.databinding.ActivityLoginBinding
import com.example.myapplication.ui.feature.main.MainActivity
import kotlinx.coroutines.launch
import androidx.core.widget.addTextChangedListener

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: AuthViewModel by viewModels()
    private lateinit var dataStoreManager: DataStoreManager

    private var activeToast: Toast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dataStoreManager = DataStoreManager(this)

        val signupLink = findViewById<TextView>(R.id.signupLink)
        signupLink.setOnClickListener {
            navigateToSignup()
        }

        setupInputListeners()
        setupViews()
        observeAuthState()
    }

    private fun setupInputListeners() {
        binding.emailInput.addTextChangedListener { text ->
            validateEmail(text.toString())
        }

        binding.passwordInput.addTextChangedListener { text ->
            validatePassword(text.toString())
        }
    }

    private fun setupViews() {
        binding.loginButton.setOnClickListener {
            val email = binding.emailInput.text.toString().trim()
            val password = binding.passwordInput.text.toString().trim()

            val isEmailValid = validateEmail(email)
            val isPasswordValid = validatePassword(password)

            if (isEmailValid && isPasswordValid) {
                viewModel.login(email, password)
            }
        }
    }

    private fun validateEmail(email: String): Boolean {
        return if (!email.endsWith("@gmail.com")) {
            binding.emailLayout.error = "Please enter a valid email"
            false
        } else {
            binding.emailLayout.error = null
            true
        }
    }

    private fun validatePassword(password: String): Boolean {
        return if (password.length < 6) {
            binding.passwordLayout.error = "Password must be at least 6 characters long"
            false
        } else {
            binding.passwordLayout.error = null
            true
        }
    }

    private fun observeAuthState() {
        viewModel.authState.observe(this) { state ->
            when (state) {
                is AuthViewModel.AuthState.Loading -> {
                    binding.loginButton.isEnabled = false
                }
                is AuthViewModel.AuthState.Success -> {
                    showToast("Login Successful!")
                    lifecycleScope.launch {
                        dataStoreManager.setLoggedIn(true)
                        navigateToMainActivity()
                    }
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
        finish()
    }

    private fun showToast(message: String) {
        activeToast?.cancel()
        activeToast = Toast.makeText(this, message, Toast.LENGTH_SHORT).apply {
            show()
        }
    }
}
