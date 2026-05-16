package com.example.rahul.weathersnap.presentation.screens.report

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rahul.weathersnap.data.local.ReportEntity
import com.example.rahul.weathersnap.data.repository.WeatherRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

data class CreateReportUiState(
    val cityName: String = "",
    val temp: Float = 0f,
    val condition: String = "",
    val humidity: Int = 0,
    val wind: Float = 0f,
    val pressure: Float = 0f,
    val notes: String = "",
    val capturedImageUri: Uri? = null,
    val originalImageSize: Long = 0,
    val compressedImageSize: Long = 0,
    val isSaving: Boolean = false
)

@HiltViewModel
class CreateReportViewModel @Inject constructor(
    private val repository: WeatherRepositoryImpl,
    private val savedStateHandle: SavedStateHandle,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _state = MutableStateFlow(CreateReportUiState())
    val state: StateFlow<CreateReportUiState> = _state.asStateFlow()

    init {
        _state.update { it.copy(
            cityName = savedStateHandle.get<String>("cityName") ?: "",
            temp = savedStateHandle.get<Float>("temp") ?: 0f,
            condition = savedStateHandle.get<String>("condition") ?: "",
            humidity = savedStateHandle.get<Int>("humidity") ?: 0,
            wind = savedStateHandle.get<Float>("wind") ?: 0f,
            pressure = savedStateHandle.get<Float>("pressure") ?: 0f,
            notes = savedStateHandle.get<String>("notes") ?: "",
            capturedImageUri = savedStateHandle.get<String>("imageUri")?.let { Uri.parse(it) },
            originalImageSize = savedStateHandle.get<Long>("origSize") ?: 0L,
            compressedImageSize = savedStateHandle.get<Long>("compSize") ?: 0L
        ) }
    }

    fun onNotesChange(notes: String) {
        _state.update { it.copy(notes = notes) }
        savedStateHandle["notes"] = notes
    }

    fun onImageCaptured(uri: Uri) {

        viewModelScope.launch {

            try {

                // DELETE OLD IMAGE
                state.value.capturedImageUri?.path?.let {
                    File(it).delete()
                }

                val file =
                    File(uri.path ?: return@launch)

                val originalSize =
                    file.length()

                val compressedFile =
                    compressImage(file)

                val compressedSize =
                    compressedFile.length()

                val compressedUri =
                    Uri.fromFile(compressedFile)

                _state.update {

                    it.copy(

                        capturedImageUri = compressedUri,

                        originalImageSize = originalSize,

                        compressedImageSize = compressedSize
                    )
                }

                savedStateHandle["imageUri"] =
                    compressedUri.toString()

                savedStateHandle["origSize"] =
                    originalSize

                savedStateHandle["compSize"] =
                    compressedSize

                // DELETE ORIGINAL FILE
                if (
                    file.absolutePath !=
                    compressedFile.absolutePath
                ) {

                    file.delete()
                }

            } catch (e: Exception) {

                e.printStackTrace()
            }
        }
    }

    private suspend fun compressImage(file: File): File = withContext(Dispatchers.IO) {
        val bitmap = BitmapFactory.decodeFile(file.absolutePath)
        val compressedFile = File(context.cacheDir, "compressed_${System.currentTimeMillis()}.jpg")
        val out = FileOutputStream(compressedFile)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, out)
        out.flush()
        out.close()
        compressedFile
    }

    fun saveReport(onSuccess: () -> Unit) {
        val currentState = _state.value
        if (currentState.capturedImageUri == null) return

        viewModelScope.launch {
            _state.update { it.copy(isSaving = true) }
            
            // Move image to permanent storage
            val permanentFile = File(context.filesDir, "report_${System.currentTimeMillis()}.jpg")
            val sourceFile = File(currentState.capturedImageUri.path ?: return@launch)
            sourceFile.copyTo(permanentFile)
            sourceFile.delete()

            val report = ReportEntity(
                cityName = currentState.cityName,
                temperature = currentState.temp.toDouble(),
                condition = currentState.condition,
                humidity = currentState.humidity,
                windSpeed = currentState.wind.toDouble(),
                pressure = currentState.pressure.toDouble(),
                notes = currentState.notes,
                imagePath = permanentFile.absolutePath,
                originalSize = currentState.originalImageSize,
                compressedSize = currentState.compressedImageSize
            )
            repository.saveReport(report)
            onSuccess()
        }
    }
}
