package com.sdin.tourstamprally.model.event

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class EventModel(
    @SerializedName("event_user_idx")
    val event_user_idx : Int,
    @SerializedName("event_coupon_idx")
    val event_coupon_idx : Int,
    @SerializedName("event_coupon_num")
    val event_coupon_num : String
) : Serializable