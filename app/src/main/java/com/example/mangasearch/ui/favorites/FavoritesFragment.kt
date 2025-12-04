package com.example.mangasearch.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mangasearch.R
import com.example.mangasearch.data.local.FavoritesManager
import com.example.mangasearch.data.model.MangaUi
import com.example.mangasearch.databinding.FragmentFavoritesBinding
import com.example.mangasearch.ui.adapters.MangaListAdapter

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavoritesViewModel by viewModels()

    private lateinit var adapter: MangaListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = MangaListAdapter { manga ->
            val bundle = bundleOf(
                "mangaId"          to manga.id,
                "mangaTitle"       to manga.title,
                "mangaAuthor"      to manga.author,
                "mangaDescription" to manga.description,
                "mangaCoverUrl"    to manga.coverUrl,
                "mangaTags"        to ArrayList(manga.tags) // List<String> -> ArrayList<String>
            )

            findNavController().navigate(R.id.mangaDetailFragment, bundle)
        }

        binding.favoritesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.favoritesRecyclerView.adapter = adapter

        viewModel.favorites.observe(viewLifecycleOwner) { list: List<MangaUi> ->
            adapter.submitList(list)
        }
    }

    override fun onResume() {
        super.onResume()
        // reload favorites from persistent storage each time we enter this tab
        val list = FavoritesManager.getFavorites(requireContext())
        viewModel.setFavorites(list)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
