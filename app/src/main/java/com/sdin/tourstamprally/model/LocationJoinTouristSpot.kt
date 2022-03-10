package com.sdin.tourstamprally.model

import com.google.gson.annotations.SerializedName

data class LocationJoinTouristSpot(
    @SerializedName("location_idx")
    val location_idx : Int,
    @SerializedName("location_name")
    val location_name : String,
    @SerializedName("touristspot_idx")
    val touristspot_idx : Int,
    @SerializedName("touristspot_name")
    val touristspot_name : String
)