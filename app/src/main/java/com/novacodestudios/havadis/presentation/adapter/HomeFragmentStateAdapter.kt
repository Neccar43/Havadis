package com.novacodestudios.havadis.presentation.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.novacodestudios.havadis.presentation.fragments.CategoryTabFragment
import com.novacodestudios.havadis.util.Category

class HomeFragmentStateAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int =Category.entries.size

    override fun createFragment(position: Int): Fragment {
        val fragment=CategoryTabFragment()
        val category=Category.entries[position].value
        fragment.arguments= Bundle().apply {
            putString(CategoryTabFragment.ARG_CATEGORY,category)
        }
        return fragment
    }
}