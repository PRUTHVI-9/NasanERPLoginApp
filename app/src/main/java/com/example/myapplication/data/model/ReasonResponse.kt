package com.example.myapplication.data.model

import com.google.gson.annotations.SerializedName

data class ReasonResponse (
    @SerializedName("status")
    val status: Boolean,
    @SerializedName("data")
    val data: List<ReasonItem>
)

data class ReasonItem (
    @SerializedName("reason_id")
    val reasonId: String,
    @SerializedName("reason_name")
    val reasonName: String
)