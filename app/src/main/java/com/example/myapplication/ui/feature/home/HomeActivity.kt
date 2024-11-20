package com.example.myapplication.ui.feature.home

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.ActivityHomeBinding
import com.example.myapplication.data.datastore.DataStoreManager
import com.example.myapplication.data.local.RelatedDocument
import com.example.myapplication.ui.adapter.ChatAdapter
import com.example.myapplication.ui.adapter.ChatViewModel
import com.example.myapplication.ui.adapter.DocumentAdapter
import com.example.myapplication.ui.feature.user.LoginActivity
import com.google.android.material.chip.Chip
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var documentAdapter: DocumentAdapter
    private lateinit var dataStoreManager: DataStoreManager
    private val viewModel by lazy { ViewModelProvider(this)[ChatViewModel::class.java] }
    private val selectedDocuments = mutableSetOf<RelatedDocument>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        dataStoreManager = DataStoreManager(this)
        setContentView(binding.root)

        binding.messageInputLayout.setEndIconOnClickListener {
            val message = binding.messageInput.text.toString()

        }

        setupToolbar()
        setupRecyclerViews()
        setupSearchFeature()
        observeViewModel()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        binding.sidebarButton.setOnClickListener {
            binding.drawerLayout.openDrawer(GravityCompat.START)
        }
    }
    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        finish()
    }

    private fun setupRecyclerViews() {
        chatAdapter = ChatAdapter()
        binding.chatRecyclerView.apply {
            adapter = chatAdapter
            layoutManager = LinearLayoutManager(this@HomeActivity)
        }

        documentAdapter = DocumentAdapter { document, isChecked ->
            if (isChecked) {
                selectedDocuments.add(document)
                addDocumentChip(document)
            } else {
                selectedDocuments.remove(document)
                removeDocumentChip(document)
            }
        }
        binding.documentsRecyclerView.apply {
            adapter = documentAdapter
            layoutManager = LinearLayoutManager(this@HomeActivity)
        }
        binding.signOutButton.setOnClickListener {
            lifecycleScope.launch {
                dataStoreManager.setLoggedIn(false)
                navigateToLogin()
            }
        }
        fun showToast(message: String) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
        viewModel.error.observe(this) { error ->
            if (error.isNotEmpty() && !isFinishing && !isDestroyed) {
                showToast(error)
            }
        }
    }

    private fun setupSearchFeature() {
        binding.searchInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.clearIcon.visibility = if (s.isNullOrEmpty()) View.GONE else View.VISIBLE
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        binding.clearIcon.setOnClickListener {
            binding.searchInput.text?.clear()
        }

        binding.searchButton.setOnClickListener {
            val query = binding.searchInput.text.toString()
            if (query.isNotEmpty()) {
                viewModel.searchRelatedDocuments(query)
            } else {
                Toast.makeText(this, "Please enter a search query", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addDocumentChip(document: RelatedDocument) {
        val chip = Chip(this).apply {
            text = document.title
            isCloseIconVisible = true
            setOnCloseIconClickListener {
                selectedDocuments.remove(document)
                binding.selectedDocumentsGroup.removeView(this)
                documentAdapter.notifyDataSetChanged()
            }
        }
        binding.selectedDocumentsGroup.addView(chip)
    }

    private fun removeDocumentChip(document: RelatedDocument) {
        val chipCount = binding.selectedDocumentsGroup.childCount
        for (i in 0 until chipCount) {
            val chip = binding.selectedDocumentsGroup.getChildAt(i) as? Chip
            if (chip?.text == document.title) {
                binding.selectedDocumentsGroup.removeView(chip)
                break
            }
        }
    }

    private fun observeViewModel() {
        viewModel.relatedDocuments.observe(this) { documents ->
            documentAdapter.submitList(documents)
        }

        viewModel.chatResponse.observe(this) { responseText ->
            responseText?.let {
                val spannableMessage = SpannableString(it)
                chatAdapter.addMessage(spannableMessage, false)
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(this) { error ->
            if (error.isNotEmpty()) {
                Toast.makeText(this, error, Toast.LENGTH_LONG).show()
            }
        }
    }
}