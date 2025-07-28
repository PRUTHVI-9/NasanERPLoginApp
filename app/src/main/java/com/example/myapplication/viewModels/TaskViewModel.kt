package com.example.myapplication.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.RoutineWorkResponse
import com.example.myapplication.data.model.TaskResponse
import com.example.myapplication.data.repository.MainRepository
import com.example.myapplication.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(val repository: MainRepository): ViewModel(){


    val result = MutableLiveData<UiState<TaskResponse>>()

    fun fetchTasks(empId: String) {
        viewModelScope.launch {
            try {
                result.value = UiState.Loading
                val response = repository.fetchTasks(empId)
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


}