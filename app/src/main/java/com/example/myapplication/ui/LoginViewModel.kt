package com.example.myapplication.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.LoginResponse
import com.example.myapplication.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {

    val result = MutableLiveData<LoginResponse>()

    fun login(userId: String, password: String) {
        viewModelScope.launch {
            try {
                val response = repository.login(userId, password)
                if (response.isSuccessful) {
                    result.value = response.body()
                }
            } catch (e: Exception) {
                Log.d("NASANAPP", e.toString())
            }
        }
    }
}