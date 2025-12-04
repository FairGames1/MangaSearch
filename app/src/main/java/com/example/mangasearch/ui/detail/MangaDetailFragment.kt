package com.example.mangasearch.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.mangasearch.R
import com.example.mangasearch.data.local.FavoritesManager
import com.example.mangasearch.data.model.MangaUi
import com.example.mangasearch.databinding.FragmentMangaDetailBinding

class MangaDetailFragment : Fragment() {

    private var _binding: FragmentMangaDetailBinding? = null
    private val binding get() = _binding!!

    private var mangaUi: MangaUi? = null

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

        val id          = arguments?.getString("mangaId")
        val title       = arguments?.getString("mangaTitle") ?: "Unknown title"
        val author      = arguments?.getString("mangaAuthor")
        val description = arguments?.getString("mangaDescription")
        val coverUrl    = arguments?.getString("mangaCoverUrl")
        val tags        = arguments?.getStringArrayList("mangaTags") ?: arrayListOf()

        if (id == null) {
            binding.titleTextView.text = "Details"
            binding.favoriteButton.isEnabled = false
            binding.chaptersButton.isEnabled = false
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

        binding.titleTextView.text       = title
        binding.authorTextView.text      = author ?: "Unknown author"
        binding.descriptionTextView.text = description ?: "No description available"

        val tagsText =
            if (tags.isNotEmpty()) tags.joinToString(" • ")
            else "Brak tagów"
        binding.tagsTextView.text = tagsText

        coverUrl?.let { url ->
            binding.coverImageView.load(url)
        }

        binding.chaptersButton.setOnClickListener {
            val bundle = bundleOf("mangaId" to id)
            findNavController().navigate(R.id.chapterListFragment, bundle)
        }

        mangaUi?.let { manga ->
            updateFavoriteButton(manga)

            binding.favoriteButton.setOnClickListener {
                FavoritesManager.toggleFavorite(requireContext(), manga)
                updateFavoriteButton(manga)
            }
        } ?: run {
            binding.favoriteButton.isEnabled = false
        }
    }

    private fun updateFavoriteButton(manga: MangaUi) {
        val isFav = FavoritesManager.isFavorite(requireContext(), manga)
        binding.favoriteButton.text = if (isFav) {
            "Remove from favorites"
        } else {
            "Add to favorites"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
