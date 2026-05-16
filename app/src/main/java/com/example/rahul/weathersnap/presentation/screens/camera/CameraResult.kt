package com.example.rahul.weathersnap.presentation.screens.camera

import android.net.Uri
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

sealed class CameraResult {
    object Idle : CameraResult()
    data class Success(val uri: Uri) : CameraResult()
}

object CameraResultStore {
    private val _result = MutableStateFlow<CameraResult>(CameraResult.Idle)
    val result = _result.asStateFlow()

    fun setResult(uri: Uri) {
        _result.value = CameraResult.Success(uri)
    }

    fun reset() {
        _result.value = CameraResult.Idle
    }
}
