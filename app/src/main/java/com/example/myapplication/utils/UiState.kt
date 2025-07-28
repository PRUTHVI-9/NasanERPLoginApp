package com.example.myapplication.utils


sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T?) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}

//sealed class Resource<out T> {
//    data class Success<T>(val data: T?): Resource<T>()
//    data class Error(val message: String): Resource<Nothing>()
//    object Loading : Resource<Nothing>()
//}


