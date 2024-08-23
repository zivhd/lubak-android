package com.example.lubak.api

import com.example.lubak.model.PredictionResponse
import com.example.lubak.model.RegisterResponse
import com.example.lubak.model.UploadResponse
import com.example.lubak.model.User
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface LubakApiServices {

    @POST(LubakApiRoutes.predict)
    suspend fun predict(
        @Body image: String
    ): Response<PredictionResponse>


    @Multipart
    @POST(LubakApiRoutes.upload)
    suspend fun upload(
        @Part image: MultipartBody.Part
    ): Response<UploadResponse>

    @POST("register")
    fun registerUser(@Body user: User): Call<String>
}
