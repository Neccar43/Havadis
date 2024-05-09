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
import com.novacodestudios.havadis.data.remote.dto.Article
import com.novacodestudios.havadis.databinding.FragmentTabBinding
import com.novacodestudios.havadis.presentation.MainActivity
import com.novacodestudios.havadis.presentation.MainViewModel
import com.novacodestudios.havadis.presentation.adapter.ArticleAdapter
import com.novacodestudios.havadis.presentation.base.BaseFragment
import com.novacodestudios.havadis.util.Category
import com.novacodestudios.havadis.util.collect
import com.novacodestudios.havadis.util.collectLast
import com.novacodestudios.havadis.util.gone
import com.novacodestudios.havadis.util.visible
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class CategoryTabFragment : BaseFragment<FragmentTabBinding>() {
    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentTabBinding = FragmentTabBinding.inflate(inflater, container, false)

    companion object {
        const val ARG_CATEGORY = "category"
        private const val TAG = "TabFragment"
    }

    private lateinit var viewModel: MainViewModel

    private var adapter: ArticleAdapter = ArticleAdapter(
        onlyWifiSelected = false,
        action = { article ->
            viewModel.setSelectedArticle(article)
            HomeFragmentDirections.actionHomeFragmentToPreviewFragment()
        })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        val categoryArgs = arguments?.takeIf { it.containsKey(ARG_CATEGORY) }?.run {
            getString(ARG_CATEGORY)
        }
        val category = Category.fromCategoryValue(categoryArgs)

        binding.rvHome.layoutManager = LinearLayoutManager(requireContext())
        getWifiOnly()
        setAdapter()
        lifecycleScope.launch {
            viewModel.getCountry().collectLatest { country ->
                collectLast(
                    viewModel.getTopHeadlineNews(country = country, category = category).asFlow(),
                    ::setArticles
                )
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
}