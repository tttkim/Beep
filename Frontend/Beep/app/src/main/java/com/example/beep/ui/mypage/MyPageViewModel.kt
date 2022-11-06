package com.example.beep.ui.mypage

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beep.data.dto.mypage.PresetResponse
import com.example.beep.domain.GetUserMessagePresetUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(private val getUserMessagePresetUseCase: GetUserMessagePresetUseCase) :
    ViewModel() {
    private val uid = 5

    init {
        getUserMessagePreset()
    }

    private val _userMessagePresetList: MutableStateFlow<List<PresetResponse>> = MutableStateFlow(listOf())
    val userMessagePresetList get() = _userMessagePresetList.value

    private fun getUserMessagePreset() {
        viewModelScope.launch {
            getUserMessagePresetUseCase.execute(uid)
                .collectLatest { _userMessagePresetList.value = it }
        }
    }
}