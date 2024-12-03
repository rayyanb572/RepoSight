package com.example.myapplication.data.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ChatDao {
    @Insert
    suspend fun insertChat(chat: Chat)

    @Query("SELECT * FROM chat_table ORDER BY id ASC")
    fun getAllChats(): LiveData<List<Chat>>

    @Query("DELETE FROM chat_table")
    suspend fun deleteAllChats()
}
