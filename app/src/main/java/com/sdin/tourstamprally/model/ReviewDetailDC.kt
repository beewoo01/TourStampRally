package com.sdin.tourstamprally.model

import com.google.gson.annotations.SerializedName

data class ReviewDetailDC(

        @SerializedName("touristspot_img")
        val touristspot_img: String,
        @SerializedName("touristspot_name")
        val touristspot_name: String,
        @SerializedName("touristspot_latitude")
        val touristspot_latitude: Double,
        @SerializedName("touristspot_longitud")
        val touristspot_longitud: Double,

        @SerializedName("review_contents")
        val review_contents: String,
        @SerializedName("review_score")
        val review_score: Float,
        @SerializedName("review_updatetime")
        val review_updatetime: String,

        @SerializedName("user_name")
        val user_name: String,
        @SerializedName("user_profile")
        val user_profile: String,

        @SerializedName("interestCount")
        val interestCount: Int,
        @SerializedName("commentCount")
        val commentCount: Int,
        @SerializedName("interestStatus")
        val interestStatus: Int,
)
