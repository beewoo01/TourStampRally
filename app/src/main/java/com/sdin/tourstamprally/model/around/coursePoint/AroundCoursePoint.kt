package com.sdin.tourstamprally.model.around.coursePoint

import com.google.gson.annotations.SerializedName

data class AroundCoursePoint(
    @SerializedName("touristspot_point_sub_idx")
    val point_sub_idx : Int,

    @SerializedName("touristspot_point_sub_course_num")
    val course_num : Int,

    @SerializedName("test_touristspotpoint_idx")
    val spotpoint_idx : Int,

    @SerializedName("test_touristspotpoint_name")
    val spotpoint_name : String,

    @SerializedName("test_touristspotpoint_explan")
    val spotpoint_explan : String,

    @SerializedName("test_touristspotpoint_latitude")
    val spotpoint_latitude: String,

    @SerializedName("test_touristspotpoint_longitude")
    val spotpoint_longitude : String,

    @SerializedName("test_touristspotpoint_curver_img")
    val spotpoint_curver_img : String,

    @SerializedName("test_touristspotpoint_number")
    val spotpoint_number : String,

    @SerializedName("test_touristspotpoint_address")
    val spotpoint_address : String,

    @SerializedName("test_touristspotpoint_videolink")
    val spotpoint_videolink : String,

    @SerializedName("isFinish")
    val isFinish : Int
)
