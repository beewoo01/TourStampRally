package com.sdin.tourstamprally.model

import com.google.gson.annotations.SerializedName

data class UserCurrentCourse(
    @SerializedName("user_current_course_idx")
    val user_current_course_idx: Int,
    @SerializedName("user_current_course_spot_idx")
    val user_current_course_spot_idx : Int,
    @SerializedName("user_current_course_status")
    val user_current_course_status : Int,
    @SerializedName("user_current_course_updatetime")
    val user_current_course_updatetime : String,
    @SerializedName("user_current_course_createtime")
    val user_current_course_createtime : String
)
