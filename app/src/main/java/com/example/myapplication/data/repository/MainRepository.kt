package com.example.myapplication.data.repository

import com.example.myapplication.data.model.EmployeeResponse
import com.example.myapplication.data.model.LoginResponse
import com.example.myapplication.data.network.ApiService
import retrofit2.Response
import javax.inject.Inject

class MainRepository @Inject constructor(val api: ApiService) {

    suspend fun login(userId: String, password: String): Response<LoginResponse> {
        return api.login(userId, password)
    }

    suspend fun fetchEmpDetails(userId: String): Response<EmployeeResponse> {
        return api.fetchEmpDetails(userId)
    }
}