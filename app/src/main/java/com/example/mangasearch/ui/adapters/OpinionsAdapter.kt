package com.example.mangasearch.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mangasearch.data.model.Opinion
import com.example.mangasearch.databinding.ItemOpinionBinding

class OpinionsAdapter : RecyclerView.Adapter<OpinionsAdapter.OpinionVH>() {

    private val items = mutableListOf<Opinion>()

    fun submitList(list: List<Opinion>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OpinionVH {
        val binding = ItemOpinionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return OpinionVH(binding)
    }

    override fun onBindViewHolder(holder: OpinionVH, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    class OpinionVH(private val binding: ItemOpinionBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(opinion: Opinion) {
            binding.ratingTextView.text = "★ ${opinion.rating}/5"
            binding.commentTextView.text = opinion.comment
        }
    }
}
