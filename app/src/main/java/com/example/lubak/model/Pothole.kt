package com.example.lubak.model

class Pothole(
    private val id: Int,
    private val coordinates: Pair<Double, Double>,
    private val streetName: String,
    private val cityName: String,
    private val user: User,
    private var isActive: Boolean,
    private val predictionConfidence: Float,
) {
    fun deactivate() {
        isActive = false
    }

    fun activate() {
        isActive = true
    }


}
