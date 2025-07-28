package com.example.myapplication.data.model

import com.google.gson.annotations.SerializedName

data class SkipReasonRequest (
    @SerializedName("routine_id")
    val routineId: String,
    @SerializedName("date")
    val date: String,
    @SerializedName("reason")
    val reason: String
)

data class SkipReasonResponse (
    @SerializedName("status")
    val status: Boolean,
    @SerializedName("message")
    val message: String
)