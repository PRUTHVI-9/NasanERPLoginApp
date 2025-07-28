package com.example.myapplication.data.model

import com.google.gson.annotations.SerializedName

data class ActionRequest(
    @SerializedName("routine_id")
    val routineId: String,
    @SerializedName("date")
    val date: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("time_req")
    val timeReq: String
)

data class TaskActionRequest (
    @SerializedName("task_id")
    val routineId: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("time_req")
    val timeReq: String
)
data class CommonResponse(
    @SerializedName("status")
    val status: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("action")
    val action: String
)