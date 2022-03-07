package com.sdin.tourstamprally.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class AllReviewModel(
    @SerializedName("location_idx")
    val location_idx: Int,

    @SerializedName("location_name")
    val location_name: String,

    @SerializedName("touristspot_idx")
    val touristspot_idx: Int,

    @SerializedName("touristspot_name")
    val touristspot_name: String,

    @SerializedName("touristspot_img")
    val touristspot_img: String,

    @SerializedName("review_idx")
    val review_idx: Int,

    @SerializedName("review_score")
    val review_score: Float,

    @SerializedName("review_contents")
    val review_contents: String,

    @SerializedName("user_idx")
    val user_idx : Int,

    @SerializedName("user_name")
    val user_name: String,

    @SerializedName("user_profile")
    val user_profile: String
) : Serializable

