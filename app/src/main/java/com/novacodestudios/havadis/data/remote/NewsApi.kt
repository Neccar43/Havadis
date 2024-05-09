package com.novacodestudios.havadis.data.remote

import com.novacodestudios.havadis.data.remote.dto.NewsResponse
import com.novacodestudios.havadis.data.remote.dto.sourceDto.SourceResponse
import com.novacodestudios.havadis.util.Constants.API_KEY
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("everything")
    suspend fun searchNews(
        @Query("page") page:Int,
        @Query("sources") sources:String?=null,
        @Query("domains") domains:String?=null, //techcrunch.com gibi.
        @Query("language") languageCode:String?=null,
        @Query("q") searchQuery: String,
        @Query("apikey") apikey:String=API_KEY
    ):NewsResponse

    @GET("top-headlines")
    suspend fun getTopHedLinesNews(
        @Query("page") page:Int,
        @Query("country") countryCode: String?=null,
        @Query("category") category:String?=null,
        @Query("sources") sources:String?=null,
        @Query("q") searchQuery: String?=null,
        @Query("apiKey") apiKey: String = API_KEY
    ):NewsResponse

    @GET("top-headlines/sources")
    suspend fun getSources(
        @Query("category") category:String?=null,
        @Query("language") languageCode:String?=null,
        @Query("country") countryCode: String?=null,
        @Query("apikey") apikey: String= API_KEY
    ): SourceResponse
}