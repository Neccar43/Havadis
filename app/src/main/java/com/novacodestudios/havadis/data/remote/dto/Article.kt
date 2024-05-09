package com.novacodestudios.havadis.data.remote.dto

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Article(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    val author: String?,
    val content: String?,
    val description: String?,
    val publishedAt: String,
    val source: Source,
    val title: String,
    val url: String,
    val urlToImage: String?,
    var userId:String?
)

