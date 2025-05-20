package com.example.myapplication.data.network

import com.example.myapplication.data.model.EmployeeResponse
import com.example.myapplication.data.model.LoginResponse
import com.example.myapplication.data.model.RoutineWorkResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @FormUrlEncoded
    @POST("/api/login.php")
    suspend fun login(
        @Field("user_id") userId: String,
        @Field("password") password: String
    ): Response<LoginResponse>


    @GET("/api/emp_details.php")
    suspend fun fetchEmpDetails(
        @Query("emp_id") empId: String
    ): Response<EmployeeResponse>

    @GET("/api/fetch_routine_works.php")
    suspend fun fetchRoutineWork(
        @Query("emp_id") empId: String
    ): Response<RoutineWorkResponse>

    @GET("/api/fetch_recon.php")
    suspend fun fetchRecon(
        @Query("emp_id") empId: String
    ): Response<RoutineWorkResponse>
}