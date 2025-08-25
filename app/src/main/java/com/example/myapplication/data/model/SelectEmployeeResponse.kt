package com.example.myapplication.data.model

import com.google.gson.annotations.SerializedName

data class SelectEmployeeResponse(
    @SerializedName("employees")
    val employees: List<EmployeeList>
)

data class EmployeeList(
    @SerializedName("EmployeeId")
    val employeeId :String,

    @SerializedName("FullName")
    val fullName:  String
)