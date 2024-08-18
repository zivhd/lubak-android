package com.example.lubak.api

import com.example.lubak.model.PredictionResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface LubakApiServices {

    @Multipart
    @POST(LubakApiRoutes.predict)
    suspend fun predict(
        @Part image: MultipartBody.Part
    ): Response<PredictionResponse>

}
