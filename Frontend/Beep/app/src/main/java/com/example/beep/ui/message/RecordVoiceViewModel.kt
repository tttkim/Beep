package com.example.beep.ui.message

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beep.domain.S3UseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import java.io.File
import java.io.FileInputStream
import javax.inject.Inject

sealed interface UiState<out T : Any> {
    data class Success<out T : Any>(val data: T) : UiState<T>
    object Error : UiState<Nothing>
    object Loading : UiState<Nothing>
}

@HiltViewModel
class RecordVoiceViewModel @Inject constructor(private val s3UseCase: S3UseCase) : ViewModel() {
    var recordVoiceUiState: UiState<String> by mutableStateOf(UiState.Success("Initial State"))
    fun postIntroduce(filepath: String, togglePopup: () -> Unit) {
        viewModelScope.launch {
            recordVoiceUiState = UiState.Loading
            val file = File(filepath)
            val fis = FileInputStream(file)
            val byteArray = fis.readBytes()
            val partFile = MultipartBody.Part.createFormData(
                "voice",
                "${System.currentTimeMillis()}record.mp3",
                byteArray.toRequestBody(contentType = "multipart/form-data".toMediaTypeOrNull())
            )
            val result = s3UseCase.postIntroduceUseCase(partFile)
            if (result.status == "OK") {
                recordVoiceUiState =
                    UiState.Success(result.data)
                togglePopup()
            } else {
                recordVoiceUiState = UiState.Error
            }
        }
    }
}