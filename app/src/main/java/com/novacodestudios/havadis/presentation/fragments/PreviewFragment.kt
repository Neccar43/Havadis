package com.novacodestudios.havadis.presentation.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ShareCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.transition.Visibility
import coil.load
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.novacodestudios.havadis.R
import com.novacodestudios.havadis.data.remote.dto.Article
import com.novacodestudios.havadis.databinding.FragmentPreviewBinding
import com.novacodestudios.havadis.presentation.MainActivity
import com.novacodestudios.havadis.presentation.MainViewModel
import com.novacodestudios.havadis.presentation.base.BaseFragment
import com.novacodestudios.havadis.util.Resource
import com.novacodestudios.havadis.util.loadImageWithWifiPreference
import com.novacodestudios.havadis.util.snackbar
import com.novacodestudios.havadis.util.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PreviewFragment :BaseFragment<FragmentPreviewBinding>() {
    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPreviewBinding = FragmentPreviewBinding.inflate(inflater,container,false)

    private lateinit var  viewModel: MainViewModel
    private lateinit var bottomNavigation: BottomNavigationView

    @SuppressLint("QueryPermissionsNeeded")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel // bu kısm fragmentlerde böyle olmalı
        bottomNavigation=(activity as MainActivity).findViewById(R.id.bottomNavigationView)
        bottomNavigation.visibility=View.GONE
        subscribe()
    }
    private fun addBookmark(article: Article){
        lifecycleScope.launch {
            val resource = viewModel.addArticleToBookmark(article)
            when (resource) {
                is Resource.Error -> {
                    Log.e(TAG, "addBookmark: ${resource.exception}")
                    requireContext().toast("Bookmarking failed")
                }
                Resource.Loading -> {}
                is Resource.Success -> {
                    requireContext().toast("Bookmark added successfully")
                }
            }
        }
    }

    private fun removeBookmark(url:String){
        lifecycleScope.launch {
            val resource = viewModel.removeArticleFromBookmark(url)
            when (resource) {
                is Resource.Error -> {
                    Log.e(TAG, "addBookmark: ${resource.exception}")
                    requireContext().toast("failed")
                }
                Resource.Loading -> {}
                is Resource.Success -> {
                    requireContext().toast("Bookmark removed successfully")
                }
            }
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun setViews(article: Article){
        binding.apply {
            setImageView(article.urlToImage)
            tvTitle.text=article.title
            tvDescription.text=article.description ?:"Description not available"

            topAppBar.setNavigationOnClickListener {
                it.findNavController().popBackStack()
                bottomNavigation.visibility=View.VISIBLE
            }


            topAppBar.setOnMenuItemClickListener {menuItem->
                when (menuItem.itemId) {
                    R.id.bookmark -> {
                        Log.d(TAG, "seth: $article")

                        lifecycleScope.launch {
                            menuItem.icon = if (viewModel.isArticleAvailable(article.url)) {
                                removeBookmark(article.url)
                                ContextCompat.getDrawable(requireContext(), R.drawable.bookmark_outlined)
                            } else {
                                addBookmark(article)
                                ContextCompat.getDrawable(requireContext(), R.drawable.bookmark_filled)
                            }
                        }

                        true
                    }
                    R.id.share->{
                        val shareIntent = ShareCompat.IntentBuilder.from(requireActivity())
                            .setType("text/plain")
                            .setText(article.url)
                            .intent
                        if (shareIntent.resolveActivity(requireActivity().packageManager) != null) {
                            startActivity(shareIntent)
                        }
                        true
                    }
                    R.id.open_browser->{
                        val action=PreviewFragmentDirections
                            .actionPreviewFragmentToNewsReadingFragment(article.url)
                        findNavController().navigate(action)
                        true
                    }
                    else -> {false}
                }
            }
            topAppBar.menu.findItem(R.id.bookmark)?.apply {
                lifecycleScope.launch {
                    icon = if (viewModel.isArticleAvailable(article.url)) {
                        ContextCompat.getDrawable(requireContext(), R.drawable.bookmark_filled)
                    } else {
                        ContextCompat.getDrawable(requireContext(), R.drawable.bookmark_outlined)
                    }
                }

            }
        }
    }

    private fun setImageView(imageUrl: String?) {
        // TODO: Null olması durumunda veya hata olması durumunda placeholder göster
        lifecycleScope.launch {
            viewModel.getWifiOnly().collectLatest {
                binding.ivNewsRow.loadImageWithWifiPreference(imageUrl,requireContext(),it)
            }
        }
    }

    private companion object{
        const val TAG="PreviewFragment"
    }

    private fun subscribe(){
        viewModel.selectedArticle.observe(viewLifecycleOwner, Observer {
            setViews(it)
        })
    }
}