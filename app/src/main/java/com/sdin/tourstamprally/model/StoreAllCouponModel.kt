package com.sdin.tourstamprally.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class StoreAllCouponModel(
    @SerializedName("store_coupon_idx")
    val store_coupon_idx : Int,
    @SerializedName("store_idx")
    val store_idx : Int,
    @SerializedName("store_location_idx")
    val store_location_idx : Int,
    @SerializedName("store_touristspot_idx")
    val store_touristspot_idx : Int,
    @SerializedName("store_logo_icon")
    val store_logo_icon : String?,
    @SerializedName("store_coupon_name")
    val store_coupon_name : String,
    @SerializedName("store_coupon_expiration_startDate")
    val store_coupon_expiration_startDate : String,
    @SerializedName("store_coupon_expiration_endDate")
    val store_coupon_expiration_endDate : String,
    @SerializedName("store_type")
    val store_type : Int,
    @SerializedName("location_name")
    val location_name : String,
    @SerializedName("touristspot_name")
    val touristspot_name : String,
    @SerializedName("store_latitude")
    val store_latitude : Double,
    @SerializedName("store_longitude")
    val store_longitude : Double
) : Serializable