package com.sdin.tourstamprally.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class RallyMapModel(
    @SerializedName("touristspot_idx")
    var touristspot_idx: Int,

    @SerializedName("touristspot_name")
    val touristspot_name: String,

    @SerializedName("test_touristspotpoint_idx")
    val touristspotpoint_idx: Int?,

    @SerializedName("touristspot_latitude")
    var touristspot_latitude: Double,

    @SerializedName("touristspot_longitude")
    var touristspot_longitude: Double,

    @SerializedName("touristspot_address")
    val touristspot_address: String?,

    @SerializedName("allCount")
    val allCount: Int?,

    @SerializedName("myCount")
    val myCount: Int?,

    @SerializedName("test_touristspotpoint_name")
    val touristspotpoint_name: String?,

    @SerializedName("touristspot_img")
    val touristspot_img: String,

    @SerializedName("user_touristspot_interest_idx")
    var user_touristspot_interest_idx: Int?
) : Serializable {

    constructor(
        touristspot_idx: Int,
        touristspot_name: String,
        touristspot_latitude: Double,
        touristspot_longitude: Double,
        touristspot_img: String
    ) : this(
        touristspot_idx = touristspot_idx,
        touristspot_name = touristspot_name,
        touristspotpoint_idx = null,
        touristspot_latitude = touristspot_latitude,
        touristspot_longitude = touristspot_longitude,
        touristspot_address = null,
        allCount = null,
        myCount = null,
        touristspotpoint_name = null,
        touristspot_img = touristspot_img,
        user_touristspot_interest_idx = null
    )
}
