package com.example.lubak.model

import android.location.Location
import com.google.gson.annotations.SerializedName
import retrofit2.Response
import java.time.LocalDateTime

data class User(
    val id: Int = 0,
    val email: String,
    val password: String,
    @SerializedName("username") val userName: String,
    @SerializedName("first_name") val firstName: String,
    @SerializedName("last_name") val lastName: String,
    @SerializedName("created_at") val createdAt: String? // Change to String
)








