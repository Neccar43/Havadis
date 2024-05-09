package com.novacodestudios.havadis.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.novacodestudios.havadis.R
import com.novacodestudios.havadis.data.remote.dto.Article
import com.novacodestudios.havadis.databinding.NewsRowBinding
import com.novacodestudios.havadis.util.loadImageWithWifiPreference

class ArticleAdapter(var onlyWifiSelected: Boolean, private val action: (Article) -> NavDirections) :
    PagingDataAdapter<Article, NewsViewHolder>(ARTICLE_COMPARATOR) {
    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        return NewsViewHolder.create(onlyWifiSelected,parent, action)
    }


}

class NewsViewHolder(
    private val onlyWifiSelected: Boolean,
    private val binding: NewsRowBinding,
    private val action: (Article) -> NavDirections
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(article: Article) {
        binding.apply {
            // TODO: Burada img url nin null gelmesi durumunda place holder göster
            ivNewsRow.loadImageWithWifiPreference(article.urlToImage,binding.ivNewsRow.context,onlyWifiSelected) /*{
                listener(
                    onError = { request, result ->
                        Log.e(
                            TAG,
                            "coil error: ${result.throwable} image url:${article.urlToImage} article:$article"
                        )

                    }
                )
            }*/
            tvRowTitle.text = article.title
            tvRowSource.text = article.source.name
            tvRowPublishDate.text = article.publishedAt

            newsRowLayout.setOnClickListener {
                it.findNavController().navigate(action.invoke(article))
            }
        }

    }

    companion object {
        private const val TAG = "NewsViewHolder"
        fun create(onlyWifiSelected: Boolean,parent: ViewGroup, action: (Article) -> NavDirections): NewsViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.news_row, parent, false)
            val binding = NewsRowBinding.bind(view)
            return NewsViewHolder(onlyWifiSelected,binding, action)
        }
    }

}

val ARTICLE_COMPARATOR = object : DiffUtil.ItemCallback<Article>() {
    override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem == newItem
    }

}