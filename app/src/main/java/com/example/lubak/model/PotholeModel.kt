package com.example.lubak.model

data class PotholeModel(
    val id: Int,
    val latitude: Float,
    val longitude: Float,
    val created_by_id: Int,
    val confidence_level: Float,
    val file_name: String?
)

interface PotholeCallback {
    fun onSuccess(message:String)
    fun onError(errorMessage: String)
}

