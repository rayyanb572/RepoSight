package com.example.myapplication.ui.feature.history

import android.os.Bundle
import android.text.SpannableString
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.databinding.ActivityHistoryBinding
import com.example.myapplication.ui.adapter.ChatAdapter
import com.example.myapplication.data.local.Message
import kotlinx.coroutines.launch

class HistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHistoryBinding
    private lateinit var chatAdapter: ChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupRecyclerView()
        loadChatHistory()
    }

    private fun setupRecyclerView() {
        chatAdapter = ChatAdapter()
        binding.historyRecyclerView.apply {
            adapter = chatAdapter
            layoutManager = LinearLayoutManager(this@HistoryActivity)
        }
    }

    private fun loadChatHistory() {
        lifecycleScope.launch {
            val database = ChatDatabase.getDatabase(this@HistoryActivity)
            database.chatDao().getAllChats().observe(this@HistoryActivity) { chats ->
                val messages = chats.map { chat ->
                    val spannableText = SpannableString(chat.message)
                    Message(
                        text = spannableText,
                        content = chat.message,
                        isUser = chat.isSentByUser,
                        timestamp = chat.timestamp
                    )
                }
                chatAdapter.submitList(messages)
            }
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            title = "Chat History"
            setDisplayHomeAsUpEnabled(true)
        }
        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}
