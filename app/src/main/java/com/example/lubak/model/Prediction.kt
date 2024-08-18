package com.example.lubak.model

data class PredictionResponse(
    val predictions: List<Prediction>
)

data class Prediction(
    val `class`: String,
    val class_id: Int,
    val confidence: Float,
    val detection_id: String
)

