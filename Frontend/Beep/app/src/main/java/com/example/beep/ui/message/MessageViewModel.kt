package com.example.beep.ui.message

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.beep.data.dto.DataModel
import com.example.beep.domain.retrofit.RetrofitUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MessageViewModel @Inject constructor(private val retrofitUseCase: RetrofitUseCase) :
    ViewModel() {
    val inputName = MutableLiveData<String>("defaultName")
    val inputJob = MutableLiveData<String>("defaultJob")

    val receivedName = MutableLiveData<String>()
    val receivedJob = MutableLiveData<String>()

    fun postTest(dataModel: DataModel) {
        viewModelScope.launch(Dispatchers.IO) {
            retrofitUseCase.postData(dataModel).collectLatest {
                receivedName.postValue(it.name)
                receivedJob.postValue(it.job)
                Log.d("ViewModel", "${receivedName.value} ${receivedJob.value}")

            }
        }
    }

    fun getTest() {
        viewModelScope.launch(Dispatchers.IO) {
            retrofitUseCase.getData().collectLatest {
                Log.d("ViewModel", it.toString())
             }
        }
    }

    fun viewModelTest() {
        Log.d("ViewModel", "${inputName.value} ${inputJob.value}")
    }
}