package com.example.mangasearch.ui.search

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
import com.example.mangasearch.databinding.FragmentSearchResultsBinding
import com.example.mangasearch.ui.adapters.MangaListAdapter

class SearchResultsFragment : Fragment() {

    private var _binding: FragmentSearchResultsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchResultsViewModel by viewModels()
    private lateinit var adapter: MangaListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchResultsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val initialQuery = arguments?.getString("query") ?: ""

        // CLICK HANDLER: open MangaDetailFragment
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
        binding.resultsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.resultsRecyclerView.adapter = adapter

        // allow searching again from this screen
        binding.searchAgainEditText.setText(initialQuery)
        binding.searchAgainEditText.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                actionId == EditorInfo.IME_ACTION_DONE
            ) {
                val q = v.text.toString()
                if (q.isNotBlank()) {
                    viewModel.search(q)
                }
                true
            } else {
                false
            }
        }

        // observe ViewModel
        viewModel.mangaList.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
        }

        // trigger initial search
        if (initialQuery.isNotBlank()) {
            viewModel.search(initialQuery)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
