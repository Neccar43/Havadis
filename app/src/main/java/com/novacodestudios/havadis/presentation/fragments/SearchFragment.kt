package com.novacodestudios.havadis.presentation.fragments

import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.asFlow
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.novacodestudios.havadis.R
import com.novacodestudios.havadis.data.remote.dto.Article
import com.novacodestudios.havadis.data.remote.dto.sourceDto.Source
import com.novacodestudios.havadis.databinding.FragmentSearchBinding
import com.novacodestudios.havadis.presentation.MainActivity
import com.novacodestudios.havadis.presentation.MainViewModel
import com.novacodestudios.havadis.presentation.adapter.ArticleAdapter
import com.novacodestudios.havadis.presentation.adapter.CategoryAdapter
import com.novacodestudios.havadis.presentation.adapter.SourceAdapter
import com.novacodestudios.havadis.presentation.base.BaseFragment
import com.novacodestudios.havadis.util.Resource
import com.novacodestudios.havadis.util.collectLast
import com.novacodestudios.havadis.util.toGroupedSourceMap
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>() {
    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSearchBinding = FragmentSearchBinding.inflate(inflater, container, false)

    private lateinit var viewModel: MainViewModel
    private val adapter = ArticleAdapter(
        onlyWifiSelected = false,
        action = {
            viewModel.setSelectedArticle(article = it)
            SearchFragmentDirections.actionSearchFragment2ToPreviewFragment()
        }
    )

    @OptIn(FlowPreview::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        subscribeSources()

        val categoryList = listOf(
            CategoryItem(R.drawable.category_business, "Business"),
            CategoryItem(R.drawable.category_entertainment, "Entertainment"),
            CategoryItem(R.drawable.category_general, "General"),
            CategoryItem(R.drawable.category_health, "Health"),
            CategoryItem(R.drawable.category_science, "Science"),
            CategoryItem(R.drawable.category_sports, "Sports"),
            CategoryItem(R.drawable.category_technology, "Technology")
        )
        setCategoryAdapter(categoryList)


        val searchView = binding.searchView
        val searchBar = binding.searchTopBar

        searchView.setupWithSearchBar(searchBar)

        // enter veya arama simgesine basıldıktan sonra çalışır
        searchView.editText.setOnEditorActionListener { textView, i, keyEvent ->
            searchBar.setText(searchView.text)
            Log.d(TAG, "setOnEditorActionListener: ${searchBar.text}")
            searchView.hide()
            false
        }

        //her klavye tuşunu basıldığında çalışır
        searchView.editText.addTextChangedListener {
            searchBar.setText(searchView.text)
            collectLast(
                flow = viewModel.searchNews(searchBar.text.toString())
                    .asFlow()
                    .debounce(500L),
                action = ::setArticles
            )
        }

        var searchJob: Job? = null

        searchView.editText.addTextChangedListener { text ->
            val searchText = text.toString()
            searchJob?.cancel()
            searchJob = lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    delay(500L)
                    viewModel.searchNews(searchText)
                        .asFlow()
                        .collectLatest(::setArticles)
                }
            }
        }






        getWifiOnly()
        binding.rvSearch.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.rvSearch.adapter = adapter


        /*viewModel.searchNews.observe(viewLifecycleOwner) { pagingData ->
            pagingData?.let {
                lifecycleScope.launch {
                    adapter.submitData(it)
                }
            }
        }*/


    }

    private suspend fun setArticles(articlePagingData: PagingData<Article>) {
        adapter.submitData(articlePagingData)
    }

    private fun setCategoryAdapter(categoryList: List<CategoryItem>) {
        binding.rvCategory.adapter = CategoryAdapter(categoryList)
        binding.rvCategory.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
    }

    private fun subscribeSources() {
        lifecycleScope.launch {
            when (val resource = viewModel.getSources()) {
                is Resource.Error -> {
                    Log.e(TAG, "subscribeSources: ${resource.exception}")
                }

                Resource.Loading -> {
                    Log.d(TAG, "subscribeSources: sources is Loading")
                }

                is Resource.Success -> {
                    resource.data.observe(viewLifecycleOwner, Observer { sources ->
                        setSourceAdapter(sources)
                    })
                }
            }
        }
    }

    private fun setSourceAdapter(sources: List<Source>) {
        val adapter = SourceAdapter(sources.toGroupedSourceMap())
        binding.rvSource.adapter = adapter
        binding.rvSearch.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun getWifiOnly() {
        lifecycleScope.launch {
            viewModel.getWifiOnly().collectLatest { onlyWifiSelected ->
                adapter.onlyWifiSelected = onlyWifiSelected
            }
        }

    }

    companion object {
        private const val TAG = "SearchFragment"
    }

    data class CategoryItem(val imageResource: Int, val name: String)


}