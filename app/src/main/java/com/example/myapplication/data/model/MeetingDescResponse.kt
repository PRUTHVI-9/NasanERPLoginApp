package com.example.myapplication.data.model

import com.google.gson.annotations.SerializedName

data class MeetingDescResponse(

    @SerializedName("meetings")
    val meetings: List<MeetingList>
)


data class MeetingList(
    @SerializedName("MeetingDescId")
    val meetingDescId: String,

    @SerializedName("MeetingDesc")
    val meetingName: String
)