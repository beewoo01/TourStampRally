package com.sdin.tourstamprally.model

import com.google.gson.annotations.SerializedName

data class ReveiwCommentsDC(
        @SerializedName("review_comment_idx")
        val review_comment_idx : Int,
        @SerializedName("review_comment_content")
        val review_comment_content : String,
        @SerializedName("review_comment_user_idx")
        val review_comment_user_idx : Int,
        @SerializedName("review_comment_updatetime")
        val review_comment_updatetime : String,
        @SerializedName("user_name")
        val user_name : String,
        @SerializedName("user_profile")
        val user_profile : String
)
