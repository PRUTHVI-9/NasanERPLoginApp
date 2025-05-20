package com.example.myapplication.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.RoutineWorkResponse
import com.example.myapplication.data.repository.MainRepository
import com.example.myapplication.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReconViewModel @Inject constructor(private val repository: MainRepository) : ViewModel(){

//    val result = MutableLiveData<RoutineWorkResponse>()
    val result = MutableLiveData<UiState<RoutineWorkResponse>>()

    fun fetchRecon(empId: String) {
        viewModelScope.launch {
            try {
                result.value = UiState.Loading
                val response = repository.fetchRecon(empId)
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


//    fun fetchRecon(empId: String) {
//        viewModelScope.launch {
//            try {
//                val response = repository.fetchRecon(empId)
//                if (response.isSuccessful) {
//                    result.value = response.body()
//                }
//            } catch (e: Exception) {
//                Log.e("NASANAPP", e.toString())
//            }
//        }
//    }
}