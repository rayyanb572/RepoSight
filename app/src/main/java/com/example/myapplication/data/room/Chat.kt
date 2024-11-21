package com.example.myapplication.data.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_table")
data class Chat(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val message: String,
    val isSentByUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)
