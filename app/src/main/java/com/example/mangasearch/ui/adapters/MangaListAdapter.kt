// file: app/src/main/java/com/example/mangasearch/ui/adapters/MangaListAdapter.kt
package com.example.mangasearch.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.mangasearch.data.model.MangaUi
import com.example.mangasearch.databinding.ItemMangaBinding

class MangaListAdapter(
    private val onClick: (MangaUi) -> Unit
) : ListAdapter<MangaUi, MangaListAdapter.MangaViewHolder>(DiffCallback) {

    inner class MangaViewHolder(
        private val binding: ItemMangaBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MangaUi) {
            binding.titleTextView.text = item.title
            binding.authorTextView.text = item.author ?: "Unknown"

            item.coverUrl?.let { url ->
                binding.coverImageView.load(url)
            }

            binding.root.setOnClickListener {
                onClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MangaViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemMangaBinding.inflate(inflater, parent, false)
        return MangaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MangaViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<MangaUi>() {
            override fun areItemsTheSame(oldItem: MangaUi, newItem: MangaUi): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: MangaUi, newItem: MangaUi): Boolean =
                oldItem == newItem
        }
    }
}
