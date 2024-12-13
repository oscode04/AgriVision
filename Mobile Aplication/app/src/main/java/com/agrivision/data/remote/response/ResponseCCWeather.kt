package com.agrivision.data.remote.response

import com.google.gson.annotations.SerializedName

data class ResponseCCWeather(

	@field:SerializedName("kota")
	val kota: String,

	@field:SerializedName("ramalan")
	val ramalan: List<RamalanItem>
)

data class RamalanItem(

    @field:SerializedName("tanggal")
	val tanggal: String,

    @field:SerializedName("ramalan")
	val ramalan: List<RamalanItem>,

    @field:SerializedName("waktu")
	val waktu: String,

    @field:SerializedName("suhu")
	val suhu: String,

    @field:SerializedName("deskripsi")
	val deskripsi: String,

    @field:SerializedName("kelembapan")
	val kelembapan: String
)

data class CuacaItem(
	@field:SerializedName("kota")
	val kota: String,

	@field:SerializedName("temperatur")
	val temperatur: String,

	@field:SerializedName("deskripsi")
	val deskripsi: String,

	@field:SerializedName("kelembapan")
	val kelembapan: String
)

data class Token(

	@field:SerializedName("access_token")
	val accessToken: String? = null
)

data class FertilizerPrediction(

	@field:SerializedName("predicted_fertilizer")
	val predictedFertilizer: String? = null
)

data class ArticleResponse(

	@field:SerializedName("Response")
	val response: List<ArticleResponseItem>
)

data class ArticleResponseItem(

	@field:SerializedName("5. imageURL")
	val jsonMember5ImageURL: String,

	@field:SerializedName("6. content")
	val jsonMember6Content: String,

	@field:SerializedName("4. title")
	val jsonMember4Title: String,

	@field:SerializedName("2. date")
	val jsonMember2Date: String,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("3. author")
	val jsonMember3Author: String
)


data class ChatResponse(

	@field:SerializedName("reply")
	val reply: Reply
)

data class PartsItem(

	@field:SerializedName("text")
	val text: String
)

data class Reply(

	@field:SerializedName("role")
	val role: String,

	@field:SerializedName("parts")
	val parts: List<PartsItem>
)

data class LoginResponse(

	@field:SerializedName("nickname")
	val nickname: String,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("username")
	val username: String
)

data class RegisterResponse(
	@field:SerializedName("message")
	val message: String
)



