package com.novacodestudios.havadis.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.liveData
import com.novacodestudios.havadis.data.remote.dto.Article
import com.novacodestudios.havadis.data.remote.dto.sourceDto.Source
import com.novacodestudios.havadis.domain.model.NewsSearchOptions
import com.novacodestudios.havadis.domain.model.TopHeadlinesOptions
import com.novacodestudios.havadis.domain.preferences.UserPreferences
import com.novacodestudios.havadis.domain.repository.NewsRepository
import com.novacodestudios.havadis.util.Category
import com.novacodestudios.havadis.util.Country
import com.novacodestudios.havadis.util.Language
import com.novacodestudios.havadis.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: NewsRepository,
    private val preferences: UserPreferences
) : ViewModel() {
    private val _selectedArticle = MutableLiveData<Article>()
    val selectedArticle: LiveData<Article>
        get() = _selectedArticle


    private val _searchNews = MutableLiveData<PagingData<Article>>()
    val searchNews: LiveData<PagingData<Article>>
        get() = _searchNews




     fun getTopHeadlineNews(
         country: Country?=null,
         category: Category?=null,
         sources: String?=null,
         query: String?=null
     ): LiveData<PagingData<Article>> =
        repository.getTopHeadLinesWithPaging(
            options =
            TopHeadlinesOptions(
                country = country,
                category = category,
                sources = sources,
                searchQuery = query, // TODO: Arama özelliği ekle
            )
        ).cachedIn(viewModelScope)


    fun searchNews(query: String = "",sources:String?=null,domains:String?=null,language: Language?=null) = repository
        .searchNews(
            options =
            NewsSearchOptions(
                sources = sources,
                domains = domains,
                language = language,
                searchQuery = query,
            )
        )
        .cachedIn(viewModelScope)

    suspend fun addArticleToBookmark(article: Article): Resource<LiveData<Boolean>> {
        return repository.addArticleToBookmark(article)
    }

    suspend fun removeArticleFromBookmark(url: String): Resource<LiveData<Boolean>> {
        return repository.removeArticleToBookmark(url)
    }

    fun setSelectedArticle(article: Article) {
        Log.d(TAG, "setSelectedArticle: $article")
        _selectedArticle.postValue(article)
    }

    private companion object {
        const val TAG = "ViewModel"
    }

    suspend fun isArticleAvailable(url: String): Boolean {
        return repository.isArticleAvailable(url)
    }

    fun getBookmarkedArticlesWithPaging(): LiveData<PagingData<Article>> {
        return Pager(PagingConfig(10)) {
            repository.getArticlesWithPaging()
        }.liveData.cachedIn(viewModelScope)
    }

    suspend fun getSources(
        category: Category? = null,
        //language:[en, no, it, ar, ud, de, pt, es, fr, he, ru, sv, nl, zh]
        language: String? = null,
        //countries:[us, au, no, it, sa, pk, gb, de, br, ca, es, ar, fr, in, is, ru, se, za, ie, nl, zh]
        country: Country? = null,
    ): Resource<LiveData<List<Source>>> {
        return repository.getSources(category, language, country)
    }

    fun setDarkMode(darkMode: Boolean) {
        viewModelScope.launch {
            preferences.setDarkMode(darkMode)
        }

    }

    fun getDarkMode(): Flow<Boolean> {
        return preferences.getDarkMode
    }

    fun setWifiOnly(imageWifiOnly: Boolean) {
        viewModelScope.launch {
            Log.d(TAG, "setWifiOnly: $imageWifiOnly")
            preferences.setImageWifiOnly(imageWifiOnly)
        }
    }

    fun getWifiOnly() = preferences.getImageWifiOnly

    fun setShowNotification(showNotification: Boolean) {
        viewModelScope.launch {
            preferences.setShowNotification(showNotification)
        }
    }

    fun getShowNotification() = preferences.getShowNotification

    fun getCountry() = preferences.getCountry

    fun setCountry(country: Country) {
        viewModelScope.launch {
            preferences.setCountry(country)
        }
    }

    fun getLanguage() = preferences.getLanguage

    fun setLanguage(language: Language) {
        viewModelScope.launch {
            preferences.setLanguage(language)
        }
    }


}