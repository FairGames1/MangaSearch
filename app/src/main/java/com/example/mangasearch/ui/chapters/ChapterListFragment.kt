package com.example.mangasearch.ui.chapters

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
import com.example.mangasearch.databinding.FragmentChapterListBinding
import com.example.mangasearch.ui.adapters.ChapterListAdapter

class ChapterListFragment : Fragment() {

    private var _binding: FragmentChapterListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ChapterListViewModel by viewModels()
    private lateinit var adapter: ChapterListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChapterListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mangaId = arguments?.getString("mangaId") ?: run {
            binding.statusTextView.text = "No manga ID provided"
            return
        }

        adapter = ChapterListAdapter { chapter ->
            val bundle = bundleOf("chapterId" to chapter.id)
            findNavController().navigate(R.id.readerFragment, bundle)
        }
        binding.chapterRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.chapterRecyclerView.adapter = adapter

        viewModel.chapterList.observe(viewLifecycleOwner) { list ->
            if (list.isNullOrEmpty()) {
                binding.chapterRecyclerView.visibility = View.GONE
                binding.statusTextView.visibility = View.VISIBLE
                binding.statusTextView.text = "No chapters found or error."
            } else {
                binding.statusTextView.visibility = View.GONE
                binding.chapterRecyclerView.visibility = View.VISIBLE
                adapter.submitList(list)
            }
        }

        binding.statusTextView.text = "Loading chapters..."
        viewModel.loadChapters(mangaId)
    }
}
