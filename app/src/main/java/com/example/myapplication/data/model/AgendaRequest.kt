package com.example.myapplication.data.model

import com.google.gson.annotations.SerializedName

data class AgendaRequest (
    @SerializedName("MeetingDescId")
    val meetingDescId: String,
)