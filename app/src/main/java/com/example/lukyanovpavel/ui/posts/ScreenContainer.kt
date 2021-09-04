package com.example.lukyanovpavel.ui.posts

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.lukyanovpavel.R
import com.example.lukyanovpavel.databinding.ScreenContainerBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScreenContainer : Fragment(R.layout.screen_container) {
    private var _binding: ScreenContainerBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = ScreenContainerBinding.bind(view)
        initUi()
    }

    private fun initUi() {
        with(binding) {
            mainPager.bind(
                tabLayout,
                requireContext(),
                layoutInflater,
                childFragmentManager,
                viewLifecycleOwner.lifecycle
            )
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    companion object {
        fun newInstance(): Fragment = ScreenContainer()
    }
}