package com.sdin.tourstamprally.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class HistorySpotModel(

    @SerializedName("allCount")
    val allCount : Int,

    @SerializedName("myCount")
    val myCount : Int,

    @SerializedName("location_idx")
    val location_idx : Int,

    @SerializedName("location_name")
    val location_name: String,

    @SerializedName("touristspot_idx")
    val touristspot_idx : Int,

    @SerializedName("touristspot_name")
    val touristspot_name: String,

    @SerializedName("touristspot_explan")
    val touristspot_explan: String,

    @SerializedName("touristspot_latitude")
    val touristspot_latitude : Double,

    @SerializedName("touristspot_longitude")
    val touristspot_longitude : Double,

    @SerializedName("touristspot_img")
    val touristspot_img: String,

    @SerializedName("test_touristhistory_updatetime")
    val touristhistory_updatetime: String,

    @SerializedName("review_idx")
    var review_idx : Int,

    @SerializedName("review_score")
    val review_score : Float,

    @SerializedName("review_contents")
    var review_contents: String?
) : Serializable
