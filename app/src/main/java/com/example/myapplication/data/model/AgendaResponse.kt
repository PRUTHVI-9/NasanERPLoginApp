package com.example.myapplication.data.model

import com.google.gson.annotations.SerializedName


data class AgendaResponse (
    @SerializedName("agenda")
    val agenda : List<AgendaList>
)

data class AgendaList(

    @SerializedName("Agenda")
    val agenda: String,
    @SerializedName("Goal")
    val goal: String,
)