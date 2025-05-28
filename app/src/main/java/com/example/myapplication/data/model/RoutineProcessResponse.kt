package com.example.myapplication.data.model

import com.google.gson.annotations.SerializedName

data class RoutineProcessResponse (
    @SerializedName("status")
    val status: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: List<RoutineProcessItem>
)

data class RoutineProcessItem (
    @SerializedName("id")
    val id: String,
    @SerializedName("process")
    val process: String
)