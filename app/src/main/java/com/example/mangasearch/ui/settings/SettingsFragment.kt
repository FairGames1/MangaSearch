package com.example.mangasearch.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.mangasearch.data.local.SettingsManager
import com.example.mangasearch.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private var ignoreSwitchChanges = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ignoreSwitchChanges = true
        binding.switchDarkMode.isChecked =
            SettingsManager.isDarkModeEnabled(requireContext())
        binding.switchSafeMode.isChecked =
            SettingsManager.isSafeMode(requireContext())
        ignoreSwitchChanges = false

        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            if (ignoreSwitchChanges) return@setOnCheckedChangeListener

            SettingsManager.setDarkModeEnabled(requireContext(), isChecked)

            AppCompatDelegate.setDefaultNightMode(
                if (isChecked) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
            )

            // optional: see section 2 about blink
            requireActivity().recreate()
        }

        binding.switchSafeMode.setOnCheckedChangeListener { _, isChecked ->
            if (ignoreSwitchChanges) return@setOnCheckedChangeListener

            SettingsManager.setSafeMode(requireContext(), isChecked)

            // no recreate needed for this setting
            // you can just inform user: "Restart search / reload list"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
