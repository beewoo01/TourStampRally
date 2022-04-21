package com.sdin.tourstamprally.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class RallyMapModel(
    @SerializedName("touristspot_idx")
    var touristspot_idx: Int,

    @SerializedName("touristspot_name")
    val touristspot_name: String,

    /*@SerializedName("touristspotpoint_idx")
    val touristspotpoint_idx : Int,*/

    @SerializedName("test_touristspotpoint_idx")
    val touristspotpoint_idx : Int?,

    @SerializedName("touristspot_latitude")
    val touristspot_latitude : Double,

    @SerializedName("touristspot_longitude")
    val touristspot_longitude : Double,

    @SerializedName("touristspot_address")
    val touristspot_address: String?,

    @SerializedName("allCount")
    val allCount: Int?,

    @SerializedName("myCount")
    val myCount: Int?,

    @SerializedName("test_touristspotpoint_name")
    val touristspotpoint_name: String?,

    /*@SerializedName("touristspotpoint_name")
    val touristspotpoint_name: String,*/

    @SerializedName("touristspot_img")
    val touristspot_img: String,

    @SerializedName("user_touristspot_interest_idx")
    var user_touristspot_interest_idx: Int?
) : Serializable
