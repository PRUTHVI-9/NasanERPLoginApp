package com.example.myapplication.data.model

import com.google.gson.annotations.SerializedName

data class LoginRequest (
    @SerializedName("user_id")
    val userId: String,
    @SerializedName("password")
    val password: String
)