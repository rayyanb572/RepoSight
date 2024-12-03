package com.example.myapplication.ui.adapter

import com.example.myapplication.data.local.RelatedDocument
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ItemDocumentBinding

class DocumentAdapter(
    private val onDocumentSelected: (RelatedDocument, Boolean) -> Unit
) : ListAdapter<RelatedDocument, DocumentAdapter.DocumentViewHolder>(DocumentDiffCallback()) {

    private val selectedDocuments = mutableSetOf<RelatedDocument>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocumentViewHolder {
        return DocumentViewHolder(
            ItemDocumentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: DocumentViewHolder, position: Int) {
        val document = getItem(position)
        holder.bind(document, selectedDocuments.contains(document)) { doc, isChecked ->
            if (isChecked) {
                selectedDocuments.add(doc)
            } else {
                selectedDocuments.remove(doc)
            }
            onDocumentSelected(doc, isChecked)
        }
    }

    fun deselectDocument(document: RelatedDocument) {
        val index = currentList.indexOf(document)
        if (index != -1) {
            notifyItemChanged(index)
        }
    }

    class DocumentViewHolder(
        private val binding: ItemDocumentBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(
            document: RelatedDocument,
            isSelected: Boolean,
            onCheckedChange: (RelatedDocument, Boolean) -> Unit
        ) {
            binding.documentTitle.text = document.judul
            binding.documentUrl.text = document.url
            binding.documentCheckbox.isChecked = isSelected

            binding.documentUrl.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(document.url))
                itemView.context.startActivity(intent)
            }

            binding.documentCheckbox.setOnCheckedChangeListener(null)
            binding.documentCheckbox.setOnCheckedChangeListener { _, isChecked ->
                onCheckedChange(document, isChecked)
            }

            binding.root.setOnClickListener {
                binding.documentCheckbox.isChecked = !binding.documentCheckbox.isChecked
            }
        }
    }

    class DocumentDiffCallback : DiffUtil.ItemCallback<RelatedDocument>() {
        override fun areItemsTheSame(oldItem: RelatedDocument, newItem: RelatedDocument) =
            oldItem.judul == newItem.judul

        override fun areContentsTheSame(oldItem: RelatedDocument, newItem: RelatedDocument) =
            oldItem == newItem
    }
}
