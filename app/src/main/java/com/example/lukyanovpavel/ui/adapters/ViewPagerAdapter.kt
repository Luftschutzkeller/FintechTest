package com.example.lukyanovpavel.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(
    listFragment: List<Fragment>,
    frag: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(frag, lifecycle) {

    private val list = listFragment

    override fun getItemCount(): Int = list.size

    override fun createFragment(position: Int): Fragment = list[position]
}