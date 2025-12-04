package com.example.mangasearch.ui.reader

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.mangasearch.databinding.FragmentReaderBinding
import com.example.mangasearch.ui.adapters.PageListAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.example.mangasearch.R
import com.example.mangasearch.data.local.ReadingProgressManager


class ReaderFragment : Fragment() {

    private var totalPages: Int = 0

    private var _binding: FragmentReaderBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ReaderViewModel by viewModels()

    private lateinit var pageAdapter: PageListAdapter

    private var chapterId: String? = null
    private var externalUrl: String? = null

    // state: is UI (indicator + system bars) visible?
    private var uiVisible: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReaderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chapterId = arguments?.getString("chapterId")
        externalUrl = arguments?.getString("externalUrl")

        pageAdapter = PageListAdapter()
        binding.viewPagerPages.adapter = pageAdapter
        binding.viewPagerPages.offscreenPageLimit = 2

        // --- TAP HANDLER: single tap on page toggles UI ---
        // --- TAP HANDLER: left/right/center zones ---
        val gestureDetector = GestureDetector(
            requireContext(),
            object : GestureDetector.SimpleOnGestureListener() {

                override fun onSingleTapUp(e: MotionEvent): Boolean {
                    val width = binding.viewPagerPages.width
                    if (width <= 0) {
                        toggleUiVisibility()
                        return true
                    }

                    val x = e.x
                    val leftZone = width * 0.33f
                    val rightZone = width * 0.66f

                    when {
                        x < leftZone -> {
                            goToPreviousPage()
                        }
                        x > rightZone -> {
                            goToNextPage()
                        }
                        else -> {
                            toggleUiVisibility()
                        }
                    }
                    return true
                }
            }
        )

// IMPORTANT: attach to the inner RecyclerView so it actually sees taps
        binding.viewPagerPages.getChildAt(0)?.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
            false // still let ViewPager2 handle swipes
        }


        // page indicator behaviour
        binding.viewPagerPages.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    updatePageIndicator(position, totalPages)
                    chapterId?.let { id ->
                        ReadingProgressManager.savePage(requireContext(), id, position)
                    }
                }
            }
        )


        if (!externalUrl.isNullOrBlank()) {
            showExternalChapterMessage(externalUrl!!)
        } else {
            if (chapterId.isNullOrBlank()) {
                binding.viewPagerPages.visibility = View.GONE
                binding.pageIndicatorText.visibility = View.GONE
                binding.infoTextView.visibility = View.VISIBLE
                binding.infoTextView.text = "Invalid chapter id."
                binding.openExternalButton.visibility = View.GONE
            } else {
                observePages()
                viewModel.loadPages(chapterId!!)
            }
        }
    }

    private fun observePages() {
        viewModel.pages.observe(viewLifecycleOwner) { pages ->
            if (pages.isNullOrEmpty()) {
                binding.viewPagerPages.visibility = View.GONE
                binding.pageIndicatorText.visibility = View.GONE
                binding.infoTextView.visibility = View.VISIBLE
                binding.infoTextView.text = "No pages found or error."
                binding.openExternalButton.visibility = View.GONE
            } else {
                binding.viewPagerPages.visibility = View.VISIBLE
                binding.infoTextView.visibility = View.GONE
                binding.openExternalButton.visibility = View.GONE

                pageAdapter.submitList(pages)
                totalPages = pages.size
                val savedPage = ReadingProgressManager.getSavedPage(requireContext(), chapterId!!)
                val targetPage = savedPage.coerceIn(0, pages.size - 1)

                binding.viewPagerPages.setCurrentItem(targetPage, false)
                updatePageIndicator(targetPage, totalPages)

                if (!uiVisible) {
                    binding.pageIndicatorText.visibility = View.GONE
                } else {
                    binding.pageIndicatorText.visibility = View.VISIBLE
                }
            }
        }
    }


    private fun updatePageIndicator(currentIndex: Int, total: Int) {
        if (total <= 0) {
            binding.pageIndicatorText.text = ""
        } else {
            val pageNumber = currentIndex + 1
            binding.pageIndicatorText.text = "Page $pageNumber / $total"
        }
    }

    private fun goToNextPage() {
        val total = pageAdapter.itemCount
        if (total <= 0) return

        val current = binding.viewPagerPages.currentItem
        if (current < total - 1) {
            val next = current + 1
            binding.viewPagerPages.setCurrentItem(next, true)
            updatePageIndicator(next, total)
        }
    }

    private fun goToPreviousPage() {
        val total = pageAdapter.itemCount
        if (total <= 0) return

        val current = binding.viewPagerPages.currentItem
        if (current > 0) {
            val prev = current - 1
            binding.viewPagerPages.setCurrentItem(prev, true)
            updatePageIndicator(prev, total)
        }
    }


    private fun showExternalChapterMessage(url: String) {
        binding.viewPagerPages.visibility = View.GONE
        binding.pageIndicatorText.visibility = View.GONE

        binding.infoTextView.visibility = View.VISIBLE
        binding.infoTextView.text =
            "This chapter is hosted externally (e.g. MangaPlus) and can't be read in the app."

        binding.openExternalButton.visibility = View.VISIBLE
        binding.openExternalButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }
    }

    // ===== UI TOGGLING / FULLSCREEN =====

    private fun toggleUiVisibility() {
        uiVisible = !uiVisible
        if (uiVisible) {
            showSystemUI()
        } else {
            hideSystemUI()
        }
    }

    private fun hideSystemUI() {
        val window = activity?.window ?: return
        val controller = WindowInsetsControllerCompat(window, binding.root)
        controller.hide(WindowInsetsCompat.Type.systemBars())
        controller.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        // hide page indicator
        binding.pageIndicatorText.visibility = View.GONE

        // hide app bottom navigation bar
        val bottomNav =
            activity?.findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNav?.visibility = View.GONE
    }


    private fun showSystemUI() {
        val window = activity?.window ?: return
        val controller = WindowInsetsControllerCompat(window, binding.root)
        controller.show(WindowInsetsCompat.Type.systemBars())

        // show page indicator only if we have pages
        if (pageAdapter.itemCount > 0) {
            binding.pageIndicatorText.visibility = View.VISIBLE
        }

        // show app bottom navigation bar again
        val bottomNav =
            activity?.findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNav?.visibility = View.VISIBLE
    }


    override fun onPause() {
        super.onPause()

        if (!chapterId.isNullOrBlank()) {
            val currentPage = binding.viewPagerPages.currentItem
            ReadingProgressManager.savePage(requireContext(), chapterId!!, currentPage)
        }
    }

}
