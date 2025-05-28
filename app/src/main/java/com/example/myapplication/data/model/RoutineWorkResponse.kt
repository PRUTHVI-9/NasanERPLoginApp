package com.example.myapplication.data.model

import com.google.gson.annotations.SerializedName

data class RoutineWorkResponse (
    @SerializedName("status")
    val status: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: List<RoutineWorkItem>
)

data class RoutineWorkItem (
    @SerializedName("routine_id")
    val routineId: String,
    @SerializedName("routine_date")
    val routineDate: String,
    @SerializedName("routine_name")
    val routineName: String,
    @SerializedName("schedule_type")
    val scheduleType: String,
    @SerializedName("schedule_date")
    val scheduleDate: String,
    @SerializedName("tolerance_date")
    val toleranceDate: String,
    @SerializedName("time_required")
    val timeRequired: String
)