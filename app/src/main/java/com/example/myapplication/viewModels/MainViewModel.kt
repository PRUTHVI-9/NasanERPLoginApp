package com.example.myapplication.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.EmployeeResponse
import com.example.myapplication.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {

    val result = MutableLiveData<EmployeeResponse>()

    fun fetchEmpDetails(userId: String) {
        viewModelScope.launch {
            try {
                val response = repository.fetchEmpDetails(userId)
                if (response.isSuccessful) {
                    result.value = response.body()
                }
            } catch (e: Exception) {
                Log.d("NASANAPP", e.toString())
            }
        }
    }
}