package com.example.myapplication.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.CommonResponse
import com.example.myapplication.data.model.ReasonResponse
import com.example.myapplication.data.model.RoutineStatusResponse
import com.example.myapplication.data.model.RoutineWorkResponse
import com.example.myapplication.data.repository.MainRepository
import com.example.myapplication.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class WorkDetailsViewModel @Inject constructor(
    private val repository: MainRepository
): ViewModel() {

    val result = MutableLiveData<UiState<CommonResponse>>()
    val resultStatus = MutableLiveData<UiState<RoutineStatusResponse>>()
    val resultReasons = MutableLiveData<UiState<ReasonResponse>>()

    fun doActionOnRoutine(routineId: String, date: String, status: String, timeReq: String) {
        viewModelScope.launch {
            try {
                result.value = UiState.Loading
                val response = repository.doActionOnRoutine(routineId, date, status, timeReq)
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

    fun fetchRoutineStatus(routineId: String, date: String) {
        viewModelScope.launch {
            try {
                resultStatus.value = UiState.Loading
                val response = repository.fetchRoutineStatus(routineId, date)
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

    fun fetchReasons() {
        viewModelScope.launch {
            try {
                resultReasons.value = UiState.Loading
                val response = repository.fetchReasons()
                if (response.isSuccessful) {
                    resultReasons.value = UiState.Success(response.body())
                } else {
                    resultReasons.value = UiState.Error(response.message())
                }
            } catch (e: Exception) {
                resultReasons.value = UiState.Error(e.message ?: "Something went wrong!")
            }
        }
    }


}