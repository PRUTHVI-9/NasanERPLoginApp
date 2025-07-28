package com.example.myapplication.data.model

import com.google.gson.annotations.SerializedName

data class TaskResponse (
    @SerializedName("status")
    val status: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: TaskData?
)

data class TaskData (
    @SerializedName("assigned_to_self")
    val assignedToSelfData: List<TaskItem>,
    @SerializedName("assigned_to_others")
    val assignedToOthersData: List<TaskItem>,
    @SerializedName("assigned_by_others")
    val assignedByOthersData: List<TaskItem>,
    @SerializedName("task_verification")
    val taskVerificationData: List<TaskItem>,
)

data class TaskItem (
    @SerializedName("task_id")
    val taskId: String? = null,
    @SerializedName("task_name")
    val taskName: String? = null,
    @SerializedName("scheduled_date")
    val scheduledDate: String? = null,
    @SerializedName("tolerance_date")
    val toleranceDate: String? = null,
    @SerializedName("time_required")
    val timeRequired: Int? = null,
    @SerializedName("assigned_to")
    val assignedTo: String? = null,
    @SerializedName("completion_date")
    val completionDate: String? = null,
    @SerializedName("start_date")
    val startDate: String? = null,
    @SerializedName("responsibility")
    val responsibility: String? = null,
)