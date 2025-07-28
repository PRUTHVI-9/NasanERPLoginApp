package com.example.myapplication.data.model

import com.google.gson.annotations.SerializedName

data class RoutineStatusRequest (
    @SerializedName("date")
    val date: String,
    @SerializedName("routine_id")
    val id: String
)

data class TaskStatusRequest (
    @SerializedName("task_id")
    val id: String
)