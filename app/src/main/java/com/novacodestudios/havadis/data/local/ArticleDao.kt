package com.novacodestudios.havadis.data.local

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.novacodestudios.havadis.data.remote.dto.Article
import retrofit2.http.GET

@Dao
interface ArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticle(article: Article)

    @Query("SELECT * FROM Article")
    fun getArticles():LiveData<List<Article>>

    // PagingSource oluşturulması
    @Query("SELECT * FROM Article")
    fun getArticlesWithPaging(): PagingSource<Int, Article>

    @Update
    suspend fun updateArticle(article: Article)

    @Query("DELETE FROM Article WHERE :url=url")
    suspend fun deleteArticle(url:String)

    @Query("SELECT COUNT(*) FROM Article WHERE url = :url")
    suspend fun isArticleAvailable(url: String): Int
}