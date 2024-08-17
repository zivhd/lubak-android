package com.example.lubak.state

data class CameraState(
    val hasCameraPermission: Boolean = false,
    val isImageCaptured: Boolean = false) {
}