package com.agrivision.data.remote.retrofit

import com.agrivision.data.companion.FertilizerData
import com.agrivision.data.companion.LoginRequest
import com.agrivision.data.companion.RegisterRequest
import com.agrivision.data.companion.VerifyRequest
import com.agrivision.data.companion.message
import com.agrivision.data.remote.response.ArticleResponseItem
import com.agrivision.data.remote.response.ChatResponse
import com.agrivision.data.remote.response.FertilizerPrediction
import com.agrivision.data.remote.response.LoginResponse
import com.agrivision.data.remote.response.RegisterResponse
import com.agrivision.data.remote.response.ResponseCCWeather
import com.agrivision.data.remote.response.Token
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET("weather/forecast")
    suspend fun getWeatherForecast(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double
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

    @POST("chatbot")
    suspend fun getChatResponse(
        @Body message: message
    ): Response<ChatResponse>

    @GET("articles/{id}")
    suspend fun getArticlesDetail(
        @Path("id") id: String
    ): Response<ArticleResponseItem>


    @POST("users/register")
    suspend fun registerUser(
        @Body body: RegisterRequest
    ): Response<RegisterResponse>


    @POST("users/verify-email")
    suspend fun verifyEmail(
        @Body body: VerifyRequest
    ): Response<RegisterResponse>


    @POST("users/login")
    suspend fun loginUser(
        @Body body: LoginRequest
    ): Response<LoginResponse>


}
