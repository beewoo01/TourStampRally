package com.sdin.tourstamprally.model

import com.google.gson.annotations.SerializedName

data class CouponModel(
    @SerializedName("coupon_idx")
    var coupon_idx : Int,
    @SerializedName("coupon_user_idx")
    var coupon_user_idx : Int,
    @SerializedName("coupon_number")
    var coupon_number : String,
    @SerializedName("coupon_status")
    var coupon_status : Int,
    @SerializedName("coupon_touristspot_idx")
    var coupon_touristspot_idx : Int,
    @SerializedName("coupon_createtime")
    var coupon_createtime : String,
    @SerializedName("coupon_updatetime")
    var coupon_updatetime : String,

    )

/*
private int coupon_idx;
private int coupon_user_idx;
private String coupon_number;
private int coupon_status;
private int coupon_touristspot_idx;
private String coupon_createtime;
private String coupon_updatetime;*/
