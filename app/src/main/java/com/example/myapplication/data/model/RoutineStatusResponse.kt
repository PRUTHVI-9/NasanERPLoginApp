package com.example.myapplication.data.model

import com.google.gson.annotations.SerializedName

data class RoutineStatusResponse (
    @SerializedName("status")
    val status: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: List<RoutineStatusItem>
)
data class RoutineStatusItem (
    @SerializedName("id")
    val id: String,
    @SerializedName("routine_id")
    val routineId: String,
    @SerializedName("routine_date")
    val routineDate: String,
    @SerializedName("start_time")
    val startTime: String,
    @SerializedName("end_time")
    val endTime: String,
    @SerializedName("total_duration")
    val total: String,
    @SerializedName("required_duration")
    val required: String,
    @SerializedName("status")
    val status: String
)