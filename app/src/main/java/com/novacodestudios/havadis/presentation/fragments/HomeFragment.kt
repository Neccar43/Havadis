package com.novacodestudios.havadis.presentation.fragments

import FooterAdapter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.asFlow
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayoutMediator
import com.novacodestudios.havadis.data.remote.dto.Article
import com.novacodestudios.havadis.databinding.FragmentHomeBinding
import com.novacodestudios.havadis.presentation.MainActivity
import com.novacodestudios.havadis.presentation.MainViewModel
import com.novacodestudios.havadis.presentation.adapter.ArticleAdapter
import com.novacodestudios.havadis.presentation.adapter.HomeFragmentStateAdapter
import com.novacodestudios.havadis.presentation.base.BaseFragment
import com.novacodestudios.havadis.util.Category
import com.novacodestudios.havadis.util.capitalizeFirstLetter
import com.novacodestudios.havadis.util.collect
import com.novacodestudios.havadis.util.collectLast
import com.novacodestudios.havadis.util.gone
import com.novacodestudios.havadis.util.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    private lateinit var viewModel: MainViewModel

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        setupViewPager()
    }
    private fun setupViewPager(){
        val viewPager=binding.vpHome
        val adapter = HomeFragmentStateAdapter(this)
        viewPager.adapter = adapter
        TabLayoutMediator(binding.tlHome,viewPager){ tab, position ->
            tab.text = Category.entries[position].value.capitalizeFirstLetter()
        }.attach()
    }




    companion object {
        private const val TAG = "Home Fragment"
    }
}