package com.example.myapplication.viewModels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.model.LoginResponse
import com.example.myapplication.data.repository.MainRepository
import com.example.myapplication.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {


    val result = MutableLiveData<UiState<LoginResponse>>()

    fun login(userId: String, password: String) {
        viewModelScope.launch {
            try {


                result.value = UiState.Loading
                val response = repository.login(userId, password)

                if (response.isSuccessful) {
                    result.value = UiState.Success(response.body())
                } else {
                    result.value = UiState.Error(response.message())
                }
            } catch (e: Exception) {
                result.value = UiState.Error("Something went wrong!")
            }
        }
    }
}