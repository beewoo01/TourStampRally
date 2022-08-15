package com.sdin.tourstamprally.model.around

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class AroundTouristSpot(
    @SerializedName("touristspot_idx")
    val touristspot_idx : Int,
    @SerializedName("touristspot_location_location_idx")
    val location_idx : Int,
    @SerializedName("location_name")
    val location_name : String,
    @SerializedName("touristspot_name")
    val touristspot_name : String,
    @SerializedName("touristspot_latitude")
    val touristspot_latitude : String,
    @SerializedName("touristspot_longitude")
    val touristspot_longitude : String,
    @SerializedName("touristspot_img")
    val touristspot_img : String

) : Serializable
