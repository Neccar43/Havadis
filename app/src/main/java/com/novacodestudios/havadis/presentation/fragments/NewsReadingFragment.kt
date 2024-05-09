package com.novacodestudios.havadis.presentation.fragments

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.navigation.fragment.navArgs
import com.novacodestudios.havadis.R
import com.novacodestudios.havadis.databinding.FragmentNewsReadingBinding
import com.novacodestudios.havadis.presentation.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewsReadingFragment :BaseFragment<FragmentNewsReadingBinding>() {
    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentNewsReadingBinding =FragmentNewsReadingBinding.inflate(inflater,container,false)

    private val args:NewsReadingFragmentArgs by navArgs()

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.wvNewsReading.apply {
            loadUrl(args.url)
            settings.javaScriptEnabled=true

            webViewClient=object :WebViewClient(){
                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    val url = request?.url.toString()
                    view?.loadUrl(url)
                    return true
                }

               /* @Deprecated("Deprecated in Java")
                override fun onReceivedError(view: WebView?, errorCode: Int, description: String?, failingUrl: String?) {
                    super.onReceivedError(view, errorCode, description, failingUrl)
                    // Hata durumunda yapılacak işlemler
                    Log.e("WebView", "Error: $description")
                }

                override fun onReceivedHttpError(view: WebView?, request: WebResourceRequest?, errorResponse: WebResourceResponse?) {
                    super.onReceivedHttpError(view, request, errorResponse)
                    // HTTP hata durumunda yapılacak işlemler
                    Log.e("WebView", "HTTP Error: ${errorResponse?.statusCode}")
                }*/

                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    binding.progressBar.visibility = View.VISIBLE
                    Log.d("WebView", "Page started loading: $url")
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    binding.progressBar.visibility = View.GONE
                    Log.d("WebView", "Page finished loading: $url")
                }
            }
        }
    }
}