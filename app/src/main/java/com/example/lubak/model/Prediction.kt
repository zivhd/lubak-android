package com.example.lubak.model

data class PredictionResponse(
    val predictions: PredictionsDetails
)

data class PredictionsDetails(
    val predictions: List<Prediction>
)

data class Prediction(
    val objects: List<ObjectDetail>
)

data class ObjectDetail(
    val bottom_center: List<Int>,
    val bounding_box: List<Int>,
    val `class`: Int
)
