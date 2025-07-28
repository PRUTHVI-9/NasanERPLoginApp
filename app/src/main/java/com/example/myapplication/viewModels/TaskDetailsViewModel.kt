package com.example.myapplication.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.CommonResponse
import com.example.myapplication.data.model.RoutineStatusResponse
import com.example.myapplication.data.model.TaskStatusResponse
import com.example.myapplication.data.repository.MainRepository
import com.example.myapplication.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskDetailsViewModel @Inject constructor(val repository: MainRepository) : ViewModel() {

    val result = MutableLiveData<UiState<CommonResponse>>()
    val resultStatus = MutableLiveData<UiState<TaskStatusResponse>>()

    fun doTaskOperation(taskId: String, status: String, timeReq: String) {
        viewModelScope.launch {
            try {
                result.value = UiState.Loading
                val response = repository.doTaskOperation(taskId, status, timeReq)
                if (response.isSuccessful) {
                    result.value = UiState.Success(response.body())
                } else {
                    result.value = UiState.Error(response.message())
                }
            } catch (e: Exception) {
                result.value = UiState.Error(e.message ?: "Something went wrong!")
            }
        }
    }

    fun fetchTaskStatus(taskId: String) {
        viewModelScope.launch {
            try {
                resultStatus.value = UiState.Loading
                val response = repository.fetchTaskStatus(taskId)
                if (response.isSuccessful) {
                    resultStatus.value = UiState.Success(response.body())
                } else {
                    resultStatus.value = UiState.Error(response.message())
                }
            } catch (e: Exception) {
                resultStatus.value = UiState.Error(e.message ?: "Something went wrong!")
            }
        }
    }
}