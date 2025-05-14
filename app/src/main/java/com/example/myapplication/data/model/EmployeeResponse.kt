package com.example.myapplication.data.model

import com.google.gson.annotations.SerializedName

data class EmployeeResponse (
    @SerializedName("success")
    val success: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: EmployeeItem
)

data class EmployeeItem (
    @SerializedName("name")
    val name: String
)