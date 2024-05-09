package com.novacodestudios.havadis.domain.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.novacodestudios.havadis.data.remote.dto.Article
import com.novacodestudios.havadis.data.remote.dto.sourceDto.Source
import com.novacodestudios.havadis.domain.model.NewsSearchOptions
import com.novacodestudios.havadis.domain.model.TopHeadlinesOptions
import com.novacodestudios.havadis.util.Category
import com.novacodestudios.havadis.util.Country
import com.novacodestudios.havadis.util.Resource

interface NewsRepository {

    fun searchNews(
        options: NewsSearchOptions
    ): LiveData<PagingData<Article>>

    fun getTopHeadLinesWithPaging(options:TopHeadlinesOptions):LiveData<PagingData<Article>>

   suspend fun getSources(
       category:Category?,
       languageCode:String?,
       country: Country?,
    ):Resource<LiveData<List<Source>>>

    suspend fun addArticleToBookmark(article: Article):Resource<LiveData<Boolean>>

    suspend fun removeArticleToBookmark(url: String):Resource<LiveData<Boolean>>

    suspend fun getArticlesFromBookmark():Resource<LiveData<List<Article>>>

     fun getArticlesWithPaging():PagingSource<Int,Article>

    suspend fun isArticleAvailable(url:String):Boolean


}