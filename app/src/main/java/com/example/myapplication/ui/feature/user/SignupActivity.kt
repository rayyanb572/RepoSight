package com.example.myapplication.ui.feature.user

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private val authViewModel: AuthViewModel by viewModels()

    private var activeToast: Toast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupInputListeners()

        binding.signupButton.setOnClickListener {
            val fullName = binding.nameInput.text.toString().trim()
            val email = binding.emailInput.text.toString().trim()
            val password = binding.passwordInput.text.toString().trim()

            val isEmailValid = validateEmail(email)
            val isPasswordValid = validatePassword(password)

            if (fullName.isEmpty()) {
                binding.nameLayout.error = "Name cannot be empty"
            } else {
                binding.nameLayout.error = null
            }

            if (isEmailValid && isPasswordValid && fullName.isNotEmpty()) {
                showToast("SignUp successfully")
                authViewModel.signUp(email, password, fullName)
            }
        }

        binding.loginLink.setOnClickListener {
            navigateToLogin()
        }
    }

    private fun setupInputListeners() {
        binding.emailInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validateEmail(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.passwordInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validatePassword(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
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

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
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