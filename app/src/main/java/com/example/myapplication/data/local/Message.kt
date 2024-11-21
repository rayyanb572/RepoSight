package com.example.myapplication.data.local

import android.text.Spannable

data class Message(
    val text: Spannable,
    val content: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)