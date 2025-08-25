package com.example.myapplication.data.model

import com.google.gson.annotations.SerializedName

data class VenueResponse (

    @SerializedName("venues")
    val venue: List<VenueList>
)

data class VenueList(
    @SerializedName("VenueId")
    val venueId: String,

    @SerializedName("VenueName")
    val venueName: String
)

