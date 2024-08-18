package com.example.lubak.api

import com.example.lubak.api.LubakApiRoutes.BASE_URL
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {


    private val client = OkHttpClient.Builder().build()

    val instance: LubakApiServices by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(LubakApiServices::class.java)
    }
}
