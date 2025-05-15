package com.example.myapplication.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.RoutineWorkResponse
import com.example.myapplication.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoutineWorkViewModel @Inject constructor(val repository: MainRepository) : ViewModel(){

    val result = MutableLiveData<RoutineWorkResponse>()


    fun fetchRoutineWork(empId: String) {
        viewModelScope.launch {
            try {
                val response = repository.fetchRoutineWork(empId)
                if (response.isSuccessful) {
                    result.value = response.body()
                }
            } catch (e: Exception) {
                Log.e("NASANAPP", e.toString())
            }
        }
    }
}