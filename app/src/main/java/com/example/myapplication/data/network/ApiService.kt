package com.example.myapplication.data.network

import com.example.myapplication.data.model.ActionRequest
import com.example.myapplication.data.model.AgendaRequest
import com.example.myapplication.data.model.AgendaResponse
import com.example.myapplication.data.model.CommonResponse
import com.example.myapplication.data.model.EmployeeResponse
import com.example.myapplication.data.model.LoginRequest
import com.example.myapplication.data.model.LoginResponse
import com.example.myapplication.data.model.MeetingDescResponse
import com.example.myapplication.data.model.MeetingResponse
import com.example.myapplication.data.model.ReasonResponse
import com.example.myapplication.data.model.RoutineProcessRequest
import com.example.myapplication.data.model.RoutineProcessResponse
import com.example.myapplication.data.model.RoutineStatusRequest
import com.example.myapplication.data.model.RoutineStatusResponse
import com.example.myapplication.data.model.RoutineWorkResponse
import com.example.myapplication.data.model.SelectEmployeeResponse
import com.example.myapplication.data.model.SkipReasonRequest
import com.example.myapplication.data.model.SkipReasonResponse
import com.example.myapplication.data.model.TaskActionRequest
import com.example.myapplication.data.model.TaskResponse
import com.example.myapplication.data.model.TaskStatusRequest
import com.example.myapplication.data.model.TaskStatusResponse
import com.example.myapplication.data.model.VenueResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {

    @FormUrlEncoded
    @POST("/api/login.php")
    suspend fun login(
        @Field("user_id") userId: String,
        @Field("password") password: String
    ): Response<LoginResponse>

    @Headers("Content-Type: application/json")
    @POST("/api/login.php")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): Response<LoginResponse>

    @GET("/api/emp_details.php")
    suspend fun fetchEmpDetails(
        @Query("emp_id") empId: String
    ): Response<EmployeeResponse>

    @GET("/api/fetch_routine_works.php")
    suspend fun fetchRoutineWork(
        @Query("emp_id") empId: String
    ): Response<RoutineWorkResponse>

    @GET("/api/fetch_pending_routines.php")
    suspend fun fetchPendingRoutines(
        @Query("emp_id") empId: String
    ): Response<RoutineWorkResponse>

    @GET("/api/fetch_recon.php")
    suspend fun fetchRecon(
        @Query("emp_id") empId: String
    ): Response<RoutineWorkResponse>

    @GET("/api/fetch_meetings.php")
    suspend fun fetchMeetings(
        @Query("emp_id") empId: String
    ): Response<MeetingResponse>

    @Headers("Content-Type: application/json")
    @POST("/api/routine_ops.php")
    suspend fun doActionOnRoutine(
        @Body actionRequest: ActionRequest
    ): Response<CommonResponse>

    @Headers("Content-Type: application/json")
    @POST("/api/fetch_routine_status.php")
    suspend fun fetchRoutineStatus(
        @Body request: RoutineStatusRequest
    ): Response<RoutineStatusResponse>

    @Headers("Content-Type: application/json")
    @POST("/api/fetch_routine_process.php")
    suspend fun fetchRoutineProcessSteps(
        @Body request: RoutineProcessRequest
    ): Response<RoutineProcessResponse>

    @GET("/api/fetch_reasons.php")
    suspend fun fetchReasons(): Response<ReasonResponse>


    @POST("/api/routine_skip_reason.php")
    suspend fun routineSkipReason(
        @Body request: SkipReasonRequest
    ): Response<SkipReasonResponse>

    @GET("/api/fetch_tasks.php")
    suspend fun fetchTasks(
        @Query("emp_id") empId: String
    ): Response<TaskResponse>

    @Headers("Content-Type: application/json")
    @POST("/api/task_ops.php")
    suspend fun doTaskOperation(
        @Body actionRequest: TaskActionRequest
    ): Response<CommonResponse>

    @Headers("Content-Type: application/json")
    @POST("/api/fetch_task_status.php")
    suspend fun fetchTaskStatus(
        @Body request: TaskStatusRequest
    ): Response<TaskStatusResponse>


    @GET("api/fetch_MeetingDesc.php")
    suspend fun fetchMeetingDesc(): Response<MeetingDescResponse>

    @GET("api/fetch_venue.php")
    suspend fun fetchVenue(): Response<VenueResponse>

    @GET("api/fetch_Employees.php")
    suspend fun fetchEmployee(): Response<SelectEmployeeResponse>

    @Headers("Content-Type: application/json")
    @POST("api/fetch_MeetingAgenda.php")
      suspend fun fetchAgenda(
        @Body request: AgendaRequest
      ): Response<AgendaResponse>
}

