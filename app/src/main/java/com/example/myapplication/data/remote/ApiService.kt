package com.example.myapplication.data.remote

import com.example.myapplication.data.local.RelatedDocument
import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("chat/")
    suspend fun chat(@Body request: ChatRequest): Response<ChatResponse>

    @POST("related_documents/")
    suspend fun getRelatedDocuments(@Body request: DocumentRequest): Response<DocumentResponse>
}

data class ChatRequest(
    val query: String,
    val context: String,
    @SerializedName("chat_history") val chatHistory: List<Int> = emptyList()
)

data class ChatResponse(
    @SerializedName("response") val response: String
)

data class DocumentRequest(
    val title: String,
    val number: Int = 3
)

data class DocumentResponse(
    @SerializedName("related_documents") val relatedDocuments: List<RelatedDocument>
)
