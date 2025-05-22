package com.example.myapplication.data.repository

import com.example.myapplication.data.model.EmployeeResponse
import com.example.myapplication.data.model.LoginRequest
import com.example.myapplication.data.model.LoginResponse
import com.example.myapplication.data.model.MeetingResponse
import com.example.myapplication.data.model.RoutineWorkResponse
import com.example.myapplication.data.network.ApiService
import kotlinx.coroutines.delay
import retrofit2.Response
import javax.inject.Inject

class MainRepository @Inject constructor(val api: ApiService) {

    suspend fun login(userId: String, password: String): Response<LoginResponse> {
        delay(2000)
        return api.login(LoginRequest(userId, password))
    }

    suspend fun fetchEmpDetails(userId: String): Response<EmployeeResponse> {
        return api.fetchEmpDetails(userId)
    }

    suspend fun fetchRoutineWork(userId: String): Response<RoutineWorkResponse> {
        return api.fetchRoutineWork(userId)
    }

    suspend fun fetchPendingRoutines(userId: String): Response<RoutineWorkResponse> {
        delay(2000)
        return api.fetchPendingRoutines(userId)
    }

    suspend fun fetchRecon(userId: String): Response<RoutineWorkResponse> {
        delay(2000)
        return api.fetchRecon(userId)
    }

    suspend fun fetchMeetings(userId: String): Response<MeetingResponse> {
        delay(2000)
        return api.fetchMeetings(userId)
    }
}