package com.example.lubak.model

data class LoginResponse(
    val success: Boolean,
    val message: String,
    val token: String? = null
)