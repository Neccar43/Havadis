package com.novacodestudios.havadis.di

import android.content.Context
import androidx.room.Room
import androidx.work.WorkManager
import com.novacodestudios.havadis.data.local.ArticleDao
import com.novacodestudios.havadis.data.local.NewsDatabase
import com.novacodestudios.havadis.data.preferences.UserPreferencesImpl
import com.novacodestudios.havadis.data.remote.NewsApi
import com.novacodestudios.havadis.data.repository.NewsRepositoryImpl
import com.novacodestudios.havadis.domain.preferences.UserPreferences
import com.novacodestudios.havadis.domain.repository.NewsRepository
import com.novacodestudios.havadis.util.Constants.BASE_URL
import com.novacodestudios.havadis.util.Constants.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NewsModule {

    @Singleton
    @Provides
    fun injectRepository(api: NewsApi, dao: ArticleDao): NewsRepository =
        NewsRepositoryImpl(api, dao)

    @Singleton
        @Provides
        fun injectPreferences(@ApplicationContext context: Context):UserPreferences=UserPreferencesImpl(context)

    @Singleton
    @Provides
    fun injectNewsApi(): NewsApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build().create(NewsApi::class.java)

    @Singleton
    @Provides
    fun injectDatabase(@ApplicationContext context: Context): NewsDatabase =
        Room.databaseBuilder(context, NewsDatabase::class.java, DATABASE_NAME).build()

    @Singleton
    @Provides
    fun injectArticleDao(database: NewsDatabase) = database.articleDao()

    @Singleton
    @Provides
    fun injectWorkManager(@ApplicationContext context: Context): WorkManager =
        WorkManager.getInstance(context)

}