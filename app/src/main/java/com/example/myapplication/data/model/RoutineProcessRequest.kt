package com.example.myapplication.data.model

import com.google.gson.annotations.SerializedName

data class RoutineProcessRequest (
    @SerializedName("routine_id")
    val routineId: String
)