package com.agrivision.data.retrofit

import com.agrivision.data.FertilizerData
import com.agrivision.data.response.ArticleResponse
import com.agrivision.data.response.ArticleResponseItem
import com.agrivision.data.response.FertilizerPrediction
import com.agrivision.data.response.ResponseCCWeather
import com.agrivision.data.response.Token
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("weather/forecast")
    suspend fun getWeatherForecast(
        @Query("lat") latitude: Double,    // Parameter latitude
        @Query("lon") longitude: Double    // Parameter longitude
    ): Response<ResponseCCWeather>

    @POST("/token")
    suspend fun getToken(): Response<Token>

    @POST("/kalkulator-pupuk")
    suspend fun getFertilizerPred(
        @Header("Authorization") accessToken: String,
        @Body fertilizerBody: FertilizerData
    ): Response<FertilizerPrediction>

    @GET("articles/all")
    suspend fun getArticles(): Response<List<ArticleResponseItem>>

    @GET("articles/{id}")
    suspend fun getArticlesDetail(
        @Path("id") id:String
    ): Response<ArticleResponseItem>
}
