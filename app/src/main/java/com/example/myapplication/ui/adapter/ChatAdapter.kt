package com.example.myapplication.ui.adapter

import android.text.Spannable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.data.local.Message
import com.example.myapplication.R

class ChatAdapter : ListAdapter<Message, ChatAdapter.ChatViewHolder>(MessageDiffCallback()) {

    private val messages = mutableListOf<Pair<Spannable, Boolean>>()

    fun addMessage(message: Spannable, isUser: Boolean) {
        messages.add(Pair(message, isUser))
        notifyItemInserted(messages.size - 1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.chat_bubble, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val (message, isUser) = messages[position]
        holder.bind(message, isUser)
    }

    override fun getItemCount(): Int = messages.size

    class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val messageText: TextView = itemView.findViewById(R.id.messageText)
        private val botMessage: TextView = itemView.findViewById(R.id.botMessage)

        fun bind(message: Spannable, isUser: Boolean) {
            messageText.text = message
            botMessage.text = message

            val background = if (isUser) {
                R.drawable.bubble_background
            } else {
                R.drawable.bubble_background_bot
            }

            messageText.setBackgroundResource(background)
            botMessage.setBackgroundResource(background)

            if (isUser) {
                messageText.visibility = View.VISIBLE
                botMessage.visibility = View.GONE
                messageText.text = message
                messageText.layoutParams = (messageText.layoutParams as ViewGroup.MarginLayoutParams).apply {
                    marginStart = 150
                    marginEnd = 10
                }
            } else {
                botMessage.visibility = View.VISIBLE
                messageText.visibility = View.GONE
                botMessage.text = message
                botMessage.setBackgroundResource(R.drawable.bubble_background_bot)
                botMessage.layoutParams = (botMessage.layoutParams as ViewGroup.MarginLayoutParams).apply {
                    marginStart = 10
                    marginEnd = 150
                }
            }
        }
    }

    class MessageDiffCallback : DiffUtil.ItemCallback<Message>() {
        override fun areItemsTheSame(oldItem: Message, newItem: Message) =
            oldItem.timestamp == newItem.timestamp

        override fun areContentsTheSame(oldItem: Message, newItem: Message) =
            oldItem == newItem
    }
}