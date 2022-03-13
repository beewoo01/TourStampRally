package com.sdin.tourstamprally.model.course

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SelectCourseModel(
    @SerializedName("location_idx")
    val location_idx : Int,
    @SerializedName("location_name")
    val location_name : String,
    @SerializedName("touristspot_idx")
    val touristspot_idx : Int,
    @SerializedName("touristspot_name")
    val touristspot_name : String,
    @SerializedName("touristspot_address")
    val touristspot_address : String,
    @SerializedName("touristspot_img")
    val touristspot_img : String
) : Serializable