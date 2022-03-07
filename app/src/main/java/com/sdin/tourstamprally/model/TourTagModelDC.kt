package com.sdin.tourstamprally.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class TourTagModelDC(
    @SerializedName("hashTag")
    val hashTag : String?,
    @SerializedName("location_idx")
    val location_idx : Int
) : Serializable