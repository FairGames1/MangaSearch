package com.example.mangasearch.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mangasearch.R
import com.example.mangasearch.databinding.FragmentHomeBinding
import com.example.mangasearch.ui.adapters.MangaListAdapter

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var adapter: MangaListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = MangaListAdapter { manga ->
            // navigate to detail screen using plain Bundle instead of Safe Args
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
        binding.mangaRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.mangaRecyclerView.adapter = adapter

        binding.searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                actionId == EditorInfo.IME_ACTION_DONE
            ) {
                val query = binding.searchEditText.text.toString()
                if (query.isNotBlank()) {
                    val bundle = bundleOf("query" to query)
                    findNavController().navigate(R.id.searchResultsFragment, bundle)
                }
                true
            } else {
                false
            }
        }

        viewModel.mangaList.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
        }

        viewModel.loadPopular()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
