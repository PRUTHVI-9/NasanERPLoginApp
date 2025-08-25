package com.example.myapplication.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.EmployeeResponse
import com.example.myapplication.data.model.MeetingDescResponse
import com.example.myapplication.data.repository.MainRepository
import com.example.myapplication.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {

//    val result = MutableLiveData<EmployeeResponse>()
    val result = MutableLiveData<UiState<EmployeeResponse>>()

    fun fetchEmpDetails(userId: String) {
        viewModelScope.launch {
            try {
                result.value = UiState.Loading
                val response = repository.fetchEmpDetails(userId)
                if (response.isSuccessful) {
                    result.value = UiState.Success(response.body())
                } else {
                    result.value = UiState.Error(response.message())
                }
            } catch (e: Exception) {
                result.value = UiState.Error(e.message ?: "Something went wrong")
            }
        }
    }

//    fun fetchEmpDetails(userId: String) {
//        viewModelScope.launch {
//            try {
//                val response = repository.fetchEmpDetails(userId)
//                if (response.isSuccessful) {
//                    result.value = response.body()
//                }
//            } catch (e: Exception) {
//                Log.d("NASANAPP", e.toString())
//            }
//        }
//    }

    private val _uiState = MutableStateFlow("Initial State")

    // Exposed as immutable
    val uiState: StateFlow<String> = _uiState

    fun updateState(newState: String) {
        _uiState.value = newState
    }

    private val _meetingResult = MutableLiveData<UiState<MeetingDescResponse>>()
    val meetingResult: MutableLiveData<UiState<MeetingDescResponse>> = _meetingResult

    /*fun fetchMeetingDesc(userId: String) {
        viewModelScope.launch {
            _meetingResult.value = UiState.Loading
            val result = repository.fetchMeetingDesc(userId)
            _meetingResult.value = result
        }
    }*/

}





