package com.novacodestudios.havadis.data.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.liveData
import com.novacodestudios.havadis.data.local.ArticleDao
import com.novacodestudios.havadis.data.remote.NewsApi
import com.novacodestudios.havadis.data.remote.SearchNewsPagingSource
import com.novacodestudios.havadis.data.remote.TopHeadlinesPagingSource
import com.novacodestudios.havadis.data.remote.dto.Article
import com.novacodestudios.havadis.data.remote.dto.sourceDto.Source
import com.novacodestudios.havadis.domain.model.NewsSearchOptions
import com.novacodestudios.havadis.domain.model.TopHeadlinesOptions
import com.novacodestudios.havadis.domain.repository.NewsRepository
import com.novacodestudios.havadis.util.Category
import com.novacodestudios.havadis.util.Country
import com.novacodestudios.havadis.util.Resource
import javax.inject.Inject


class NewsRepositoryImpl @Inject constructor(
    private val newsApi: NewsApi,
    private val articleDao:ArticleDao,
) : NewsRepository {
    private companion object{
        private const val TAG="NewsRepositoryImpl"
    }
    override fun searchNews(options: NewsSearchOptions): LiveData<PagingData<Article>> {
        Log.d(TAG, "searchNews: ${options.searchQuery}")
        val pager=
         Pager(
                config = PagingConfig(pageSize = 10),  // her sayfada 10 haber
                pagingSourceFactory = {
                    SearchNewsPagingSource(
                        newsApi = newsApi,
                       options = options,
                    )
                }
            ).liveData
        Log.d(TAG, "searchNews: ${pager.value}")
        return pager


    }

    override fun getTopHeadLinesWithPaging(options: TopHeadlinesOptions): LiveData<PagingData<Article>> {
        return Pager(
                config = PagingConfig(pageSize = 10),  // her sayfada 10 haber
                pagingSourceFactory = {
                    TopHeadlinesPagingSource(
                        newsApi = newsApi,
                        options = options,
                    )
                }
            ).liveData

    }

    override suspend fun getSources(
        category: Category?,
        languageCode: String?,
        countryCode: Country?
    ): Resource<LiveData<List<Source>>> {
        return try {
            val sources=newsApi.getSources(category?.name, languageCode, countryCode?.name).sources
            Resource.success(liveData { emit(sources) })
        } catch (e: Exception) {
            Resource.error(e)
        }
    }

    override suspend fun addArticleToBookmark(article: Article): Resource<LiveData<Boolean>> {
        return try {
            articleDao.insertArticle(article)
            Resource.success(liveData { emit(true) })
        } catch (e: Exception) {
            Resource.error(e)
        }

    }

    override suspend fun removeArticleToBookmark(url: String): Resource<LiveData<Boolean>> {
        return try {
            articleDao.deleteArticle(url)
            Resource.success(liveData { emit(true) })
        } catch (e: Exception) {
            Resource.error(e)
        }
    }

    override suspend fun getArticlesFromBookmark(): Resource<LiveData<List<Article>>> {
        return try {
            val articles= articleDao.getArticles()
            Resource.success(articles)
        } catch (e: Exception) {
            Resource.error(e)
        }
    }

    override  fun getArticlesWithPaging(): PagingSource<Int, Article> {
        return articleDao.getArticlesWithPaging()
    }

    override suspend fun isArticleAvailable(url: String): Boolean {
        return try {
            val count=articleDao.isArticleAvailable(url)
            count != 0
        } catch (e: Exception) {
            Log.e(TAG, "isArticleAvailable: $e")
            false
        }
    }



}