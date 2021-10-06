package com.sdin.tourstamprally.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ReviewWriter(
        @SerializedName("spotIdx")
        val spotIdx: Int,
        @SerializedName("spotName")
        val spotName: String,
        @SerializedName("isFirst")
        val isFirst: Boolean,
        @SerializedName("review_idx")
        val review_idx: Int,
        @SerializedName("review_score")
        val review_score: Float,
        @SerializedName("review_contents")
        val review_contents : String,

        ) : Serializable {

    constructor(spotIdx: Int, spotName: String, isFirst: Boolean)
            : this(spotIdx, spotName, isFirst, 0, 0F, "")


}
