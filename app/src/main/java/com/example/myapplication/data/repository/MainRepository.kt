package com.example.myapplication.data.repository

import com.example.myapplication.data.model.ActionRequest
import com.example.myapplication.data.model.AgendaRequest
import com.example.myapplication.data.model.AgendaResponse
//import com.example.myapplication.data.model.AgendaRequest
//import com.example.myapplication.data.model.AgendaResponse
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
import com.example.myapplication.data.model.TaskStatusRequest
import com.example.myapplication.data.model.VenueResponse
import com.example.myapplication.data.network.ApiService
import com.example.myapplication.utils.UiState
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

//    suspend fun fetchMeetingDesc(userId: String): Response<MeetingDescResponse> {
//
//        delay(2000)
//        return api.fetchMeetingDesc(userId)
//    }



    suspend fun doActionOnRoutine(
        routineId: String,
        date: String,
        status: String,
        timeReq: String
    ): Response<CommonResponse> {
        delay(2000)
        return api.doActionOnRoutine(
            ActionRequest(
                routineId,
                date,
                status,
                timeReq
            )
        )
    }

    suspend fun fetchRoutineStatus(
        routineId: String,
        date: String
    ): Response<RoutineStatusResponse> {
        delay(2000)
        return api.fetchRoutineStatus(
            RoutineStatusRequest(
                date,
                routineId
            )
        )
    }

    suspend fun fetchRoutineProcessSteps(routineId: String): Response<RoutineProcessResponse> {
        delay(2000)
        return api.fetchRoutineProcessSteps(
            RoutineProcessRequest(
                routineId
            )
        )
    }

    suspend fun fetchAgenda(meetingDescId: String): Response<AgendaResponse> {
        delay(2000)
        return api.fetchAgenda(
            AgendaRequest(
                meetingDescId
            )
        )
    }

//    suspend fun fetchMeetingDesc(): Response<MeetingDescResponse> {
//        return api.fetchMeetingDesc()
//    }

    suspend fun fetchMeetingDesc(): UiState<MeetingDescResponse> {
        return try {
            val response = api.fetchMeetingDesc()
            if (response.isSuccessful && response.body() != null) {
                UiState.Success(response.body()!!)
            } else {
                UiState.Error("Error: ${response.code()} - ${response.message()}")
            }
        } catch (e: Exception) {
            UiState.Error(e.localizedMessage ?: "Unknown error")
        }
    }

    suspend fun fetchVenueList(): UiState<VenueResponse> {
        return try {
            val response = api.fetchVenue()
            if (response.isSuccessful && response.body() != null) {
                UiState.Success(response.body()!!)
            } else {
                UiState.Error(response.message())
            }
        } catch (e: Exception) {
            UiState.Error(e.localizedMessage ?: "Unknown Error")
        }
    }

    suspend fun fetchEmployeeList(): UiState<SelectEmployeeResponse> {
        return try {
            val response = api.fetchEmployee()
            if (response.isSuccessful && response.body() != null) {
                UiState.Success(response.body()!!)
            } else {
                UiState.Error(response.message())
            }
        } catch (e: Exception) {
            UiState.Error(e.localizedMessage ?: "Unknown Error")
        }
    }





    suspend fun fetchReasons(): Response<ReasonResponse> {
        return api.fetchReasons()
    }

    suspend fun skipReason(
        routineId: String,
        date: String,
        reason: String
    ): Response<SkipReasonResponse> {
        return api.routineSkipReason(SkipReasonRequest(routineId, date, reason))
    }

    suspend fun fetchTasks(empId: String) = api.fetchTasks(empId)

    suspend fun fetchTaskStatus(taskId: String) = api.fetchTaskStatus(TaskStatusRequest(taskId))

    suspend fun doTaskOperation(taskId: String, status: String, timeReq: String) =
        api.doTaskOperation(
            TaskActionRequest(taskId, status, timeReq)
        )



    
}