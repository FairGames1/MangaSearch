package com.example.mangasearch.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.mangasearch.data.model.PageUi
import com.example.mangasearch.databinding.ItemPageBinding

class PageListAdapter :
    ListAdapter<PageUi, PageListAdapter.PageViewHolder>(DiffCallback) {

    inner class PageViewHolder(
        private val binding: ItemPageBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PageUi) {
            binding.pageImageView.load(item.url)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPageBinding.inflate(inflater, parent, false)
        return PageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<PageUi>() {
            override fun areItemsTheSame(oldItem: PageUi, newItem: PageUi) =
                oldItem.url == newItem.url

            override fun areContentsTheSame(oldItem: PageUi, newItem: PageUi) =
                oldItem == newItem
        }
    }
}
