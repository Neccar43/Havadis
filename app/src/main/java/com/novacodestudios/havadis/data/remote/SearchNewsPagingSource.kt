package com.novacodestudios.havadis.data.remote

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.novacodestudios.havadis.data.remote.dto.Article
import com.novacodestudios.havadis.domain.model.NewsSearchOptions

class SearchNewsPagingSource(
    private val newsApi: NewsApi,
    private val options: NewsSearchOptions,
   // private val sources:String,
) : PagingSource<Int, Article>() { // Int değeri sayfayı temsil ediyor
    private var totalNewsCount=0


    // ilk yükleme veya akışı yenileme işleminden sonra çağırdığımız fonksiyon
    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        Log.d(TAG, "getRefreshKey: executing")
        return state.anchorPosition?.let {anchorPosition->
            val anchorPage= state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1)?:anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        Log.d(TAG, "load: executing")
        val page = params.key ?: 1
        return try {
            val newsResponse=newsApi
                .searchNews(
                    page = page,
                    sources = options.sources,
                    domains = options.domains,
                    languageCode = options.language?.code,
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
            Log.e(TAG, "load: error:$e")
            e.printStackTrace()
            LoadResult.Error(throwable = e )
        }
    }

    companion object{
        private const val TAG="SearchNewsPagingSource"
    }
}