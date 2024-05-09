package com.novacodestudios.havadis.presentation.fragments

import FooterAdapter
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.novacodestudios.havadis.R
import com.novacodestudios.havadis.databinding.FragmentBookmarkBinding
import com.novacodestudios.havadis.presentation.MainActivity
import com.novacodestudios.havadis.presentation.MainViewModel
import com.novacodestudios.havadis.presentation.adapter.ArticleAdapter
import com.novacodestudios.havadis.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BookmarkFragment :BaseFragment<FragmentBookmarkBinding>() {
    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentBookmarkBinding =FragmentBookmarkBinding.inflate(inflater,container,false)
    private lateinit var viewModel: MainViewModel
    private val adapter:ArticleAdapter = ArticleAdapter(
        onlyWifiSelected = false,
        action = { article ->
            viewModel.setSelectedArticle(article)
            BookmarkFragmentDirections.actionBookmarkFragment2ToPreviewFragment()
        })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel=(activity as MainActivity).viewModel
        binding.rvBookmark.layoutManager = LinearLayoutManager(requireContext())
        getWifiOnly()
        setAdapter()

        viewModel.getBookmarkedArticlesWithPaging().observe(viewLifecycleOwner, Observer {
            lifecycleScope.launch {
                adapter.submitData(it)
            }
        })
    }

    private fun setAdapter() {
        binding.rvBookmark.adapter = adapter
    }

    private fun getWifiOnly(){
        lifecycleScope.launch {
            viewModel.getWifiOnly().collectLatest { onlyWifiSelected ->
                adapter.onlyWifiSelected = onlyWifiSelected
            }
        }

    }

}