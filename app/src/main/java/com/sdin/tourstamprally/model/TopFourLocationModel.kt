package com.sdin.tourstamprally.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Keep
data class TopFourLocationModel(
    @SerializedName("myInterCount")
    var myInterCount: Int = 0,
    @SerializedName("allSpotCount")
    val allSpotCount: Int = 0,
    @SerializedName("popular")
    val popular: Int = 0,
    @SerializedName("location_idx")
    val location_idx: Int = 0,
    @SerializedName("location_name")
    val location_name: String,
    @SerializedName("location_img")
    val location_img: String,
    @SerializedName("myHistoryCount")
    val myHistoryCount: Int = 0,
    @SerializedName("allPointCount")
    val allPointCount: Int = 0,
    @SerializedName("touristspot_latitude")
    val touristspot_latitude: Double = 0.0,
    @SerializedName("touristspot_longitude+")
    val touristspot_longitude : Double = 0.0,
    @SerializedName("touristspotpoint_createtime")
    val touristspotpoint_createtime : String

) : Serializable