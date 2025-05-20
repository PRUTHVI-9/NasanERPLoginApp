package com.example.myapplication.data.model

import com.google.gson.annotations.SerializedName

data class MeetingResponse (
    @SerializedName("status")
    val status: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: List<MeetingItem>
)

data class MeetingItem (
    @SerializedName("meeting_desc_id")
    val meetingDescId: String,
    @SerializedName("meeting_name")
    val meetingName: String,
    @SerializedName("schedule_type")
    val scheduleType: String,
    @SerializedName("meeting_date")
    val scheduleDate: String,
    @SerializedName("start_time")
    val startTime: String,
    @SerializedName("end_time")
    val endTime: String,
    @SerializedName("tolerance_date")
    val toleranceDate: String,
    @SerializedName("time_required")
    val timeRequired: String
)