package com.example.mangasearch.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mangasearch.data.model.ChapterUi
import com.example.mangasearch.databinding.ItemChapterBinding

class ChapterListAdapter(
    private val onClick: (ChapterUi) -> Unit
) : ListAdapter<ChapterUi, ChapterListAdapter.ChapterViewHolder>(DiffCallback) {

    inner class ChapterViewHolder(
        private val binding: ItemChapterBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ChapterUi) {
            binding.chapterNumberTextView.text = "Chapter ${item.number}"
            binding.chapterTitleTextView.text = item.title ?: ""
            binding.chapterDateTextView.text = item.date ?: ""
            binding.root.setOnClickListener { onClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChapterViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemChapterBinding.inflate(inflater, parent, false)
        return ChapterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChapterViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<ChapterUi>() {
            override fun areItemsTheSame(oldItem: ChapterUi, newItem: ChapterUi) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: ChapterUi, newItem: ChapterUi) =
                oldItem == newItem
        }
    }
}
