package com.example.myapplication.data.model

import com.google.gson.annotations.SerializedName

data class EmployeeResponse(
    @SerializedName("status")
    val status: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: List<EmployeeItem>
)

data class EmployeeItem(
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("middle_name")
    val middleName: String,
    @SerializedName("last_name")
    val lastName: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("blood_group")
    val bloodGroup: String,
    @SerializedName("city")
    val city: String,
    @SerializedName("phone_number")
    val phoneNumber: String
)