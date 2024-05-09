package com.novacodestudios.havadis.data.remote

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.novacodestudios.havadis.data.remote.dto.Article
import com.novacodestudios.havadis.domain.model.TopHeadlinesOptions

class TopHeadlinesPagingSource(
    private val newsApi: NewsApi,
    private val options: TopHeadlinesOptions,
) : PagingSource<Int, Article>() { // Int değeri sayfayı temsil ediyor
    private var totalNewsCount=0

    // ilk yükleme veya akışı yenileme işleminden sonra çağırdığımız fonksiyon
    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return state.anchorPosition?.let {anchorPosition->
            val anchorPage= state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1)?:anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        val page = params.key ?: 1
        return try {
            val newsResponse=newsApi
                .getTopHedLinesNews(
                    page = page,
                    countryCode = options.country?.countryCode,
                    category = options.category?.value,
                    //sources = options.sources,
                    searchQuery = options.searchQuery,
                )
            Log.d(TAG, "load: ${newsResponse.status}")
            totalNewsCount+=newsResponse.articles.size
            // Bu api bazen aynı haberi birden fazla verebildiği için aynı habereri filtreliyoruz
            val articles=newsResponse.articles.distinctBy { it.title  }
            LoadResult.Page(
                data = articles,
                nextKey = if (totalNewsCount == newsResponse.totalResults) null else page + 1,
                prevKey = null
            )
        } catch (e: Exception) {
            e.printStackTrace()
            LoadResult.Error(throwable = e )
        }
    }

    companion object{
        private const val TAG="TopHeadlinesPagingSource"
    }
}