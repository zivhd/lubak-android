package com.example.lubak.model

data class PredictionResponse(
    val predictions: List<Prediction>
)

data class Prediction(
    val `class`: Int,
    val confidence: Float,
    val box: List<Float>
)
