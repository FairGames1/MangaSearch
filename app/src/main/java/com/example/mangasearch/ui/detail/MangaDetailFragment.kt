package com.example.mangasearch.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.mangasearch.R
import com.example.mangasearch.data.local.FavoritesManager
import com.example.mangasearch.data.local.OpinionsManager
import com.example.mangasearch.data.model.MangaUi
import com.example.mangasearch.data.model.Opinion
import com.example.mangasearch.databinding.FragmentMangaDetailBinding
import com.example.mangasearch.ui.adapters.OpinionsAdapter

class MangaDetailFragment : Fragment() {

    private var _binding: FragmentMangaDetailBinding? = null
    private val binding get() = _binding!!

    private var mangaUi: MangaUi? = null
    private val opinionsAdapter = OpinionsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMangaDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // --- read args passed in bundle ---
        val id = arguments?.getString("mangaId")
        val title = arguments?.getString("mangaTitle") ?: "Unknown title"
        val author = arguments?.getString("mangaAuthor")
        val description = arguments?.getString("mangaDescription")
        val coverUrl = arguments?.getString("mangaCoverUrl")
        val tags = arguments?.getStringArrayList("mangaTags") ?: arrayListOf()

        if (id == null) {
            binding.titleTextView.text = "Details"
            binding.favoriteButton.isEnabled = false
            binding.chaptersButton.isEnabled = false
            binding.addOpinionButton.isEnabled = false
            return
        }

        mangaUi = MangaUi(
            id = id,
            title = title,
            author = author,
            description = description,
            coverUrl = coverUrl,
            tags = tags
        )

        // --- fill UI ---
        binding.titleTextView.text = title
        binding.authorTextView.text = author ?: "Unknown author"
        binding.descriptionTextView.text = description ?: "No description available"

        val tagsText = if (tags.isNotEmpty()) tags.joinToString(" • ") else "Brak tagów"
        binding.tagsTextView.text = tagsText

        coverUrl?.let { url ->
            binding.coverImageView.load(url)
        }

        // --- chapters button ---
        binding.chaptersButton.setOnClickListener {
            val bundle = bundleOf("mangaId" to id)
            findNavController().navigate(R.id.chapterListFragment, bundle)
        }

        // --- favorites button ---
        mangaUi?.let { manga ->
            updateFavoriteButton(manga)

            binding.favoriteButton.setOnClickListener {
                FavoritesManager.toggleFavorite(requireContext(), manga)
                updateFavoriteButton(manga)
            }
        } ?: run {
            binding.favoriteButton.isEnabled = false
        }

        // --- opinions list setup ---
        binding.opinionsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.opinionsRecyclerView.adapter = opinionsAdapter
        refreshOpinions(id)

        // --- add opinion ---
        binding.addOpinionButton.setOnClickListener {
            val rating = binding.ratingBar.rating.toInt().coerceIn(1, 5)
            val comment = binding.opinionEditText.text?.toString()?.trim().orEmpty()

            if (comment.isBlank()) {
                binding.opinionEditText.error = "Comment cannot be empty"
                return@setOnClickListener
            }

            OpinionsManager.addOpinion(
                requireContext(),
                Opinion(mangaId = id, rating = rating, comment = comment)
            )

            binding.opinionEditText.setText("")
            binding.ratingBar.rating = 0f

            refreshOpinions(id)
        }
    }

    private fun refreshOpinions(mangaId: String) {
        val list = OpinionsManager.getOpinions(requireContext(), mangaId)
        opinionsAdapter.submitList(list)
    }

    private fun updateFavoriteButton(manga: MangaUi) {
        val isFav = FavoritesManager.isFavorite(requireContext(), manga)
        binding.favoriteButton.text = if (isFav) "Remove from favorites" else "Add to favorites"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
