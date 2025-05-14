package com.example.myapplication.data.model

import com.google.gson.annotations.SerializedName

data class LoginResponse (
    @SerializedName("success")
    val success: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("emp_id")
    val empId: String
)