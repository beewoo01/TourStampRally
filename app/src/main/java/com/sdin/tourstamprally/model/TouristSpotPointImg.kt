package com.sdin.tourstamprally.model

import com.google.gson.annotations.SerializedName

data class TouristSpotPointImg(
    @SerializedName("touristspotpoint_img_idx")
    val touristspotpoint_img_idx : Int,
    @SerializedName("touristspotpoint_idx")
    val touristspotpoint_idx : Int,
    @SerializedName("touristspotpoint_img_url")
    val touristspotpoint_img_url : String
)
