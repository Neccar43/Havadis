package com.novacodestudios.havadis.presentation.fragments

import FooterAdapter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.asFlow
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.novacodestudios.havadis.data.remote.dto.Article
import com.novacodestudios.havadis.databinding.FragmentHomeBinding
import com.novacodestudios.havadis.presentation.MainActivity
import com.novacodestudios.havadis.presentation.MainViewModel
import com.novacodestudios.havadis.presentation.adapter.ArticleAdapter
import com.novacodestudios.havadis.presentation.base.BaseFragment
import com.novacodestudios.havadis.util.collect
import com.novacodestudios.havadis.util.collectLast
import com.novacodestudios.havadis.util.gone
import com.novacodestudios.havadis.util.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {
    private lateinit var viewModel: MainViewModel
    private var adapter: ArticleAdapter = ArticleAdapter(
        onlyWifiSelected = false,
        action = { article ->
            viewModel.setSelectedArticle(article)
            HomeFragmentDirections.actionHomeFragment2ToPreviewFragment()
        })

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding = FragmentHomeBinding.inflate(inflater, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        binding.rvHome.layoutManager = LinearLayoutManager(requireContext())
        getWifiOnly()
        setAdapter()
        // TODO: tek seferde çok fazla istek atıyor bunu düzelt
        lifecycleScope.launch {
            viewModel.getCountry().collectLatest { country ->
                collectLast(viewModel.getTopHeadlineNews(country = country).asFlow(), ::setArticles)
            }
        }


    }

    private fun setAdapter() {
        collect(flow = adapter.loadStateFlow
            .distinctUntilChangedBy { it.source.refresh }
            .map { it.refresh },
            action = ::setState
        )
        binding.rvHome.adapter = adapter.withLoadStateFooter(FooterAdapter(adapter::retry))
    }

    private suspend fun setArticles(articlePagingData: PagingData<Article>) {
        adapter.submitData(articlePagingData)
    }

    private fun setState(loadState: LoadState) {
        when (loadState) {
            is LoadState.Error -> {
                disableShimmer()
                Log.e(
                    TAG,
                    "setState: load state is ${loadState.error.localizedMessage}"
                )
                binding.linearRetry.visible()
                binding.tvHomeError.text = loadState.error.localizedMessage
                binding.btnHomeRetry.setOnClickListener {
                    adapter.retry()
                    binding.linearRetry.gone()
                }
            }

            LoadState.Loading -> {
                Log.d(TAG, "setState: loading")
                binding.shimmerLayout.visible()
                binding.shimmerLayout.startShimmer()
            }

            is LoadState.NotLoading -> {
                Log.d(TAG, "setState: not loading")
                disableShimmer()
            }
        }
    }

    private fun disableShimmer() {
        binding.shimmerLayout.apply {
            stopShimmer()
            gone()
        }
    }


    private fun getWifiOnly() {
        lifecycleScope.launch {
            viewModel.getWifiOnly().collectLatest { onlyWifiSelected ->
                adapter.onlyWifiSelected = onlyWifiSelected
            }
        }

    }

    companion object {
        private const val TAG = "Home Fragment"
    }
}