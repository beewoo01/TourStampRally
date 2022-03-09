package com.sdin.tourstamprally.model

import com.google.gson.annotations.Expose
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
    val review_contents: String?,

    @SerializedName("review_imgs")
    var review_imgs: MutableList<ReviewImg>?

) : Serializable {

    constructor(spotIdx: Int, spotName: String, isFirst: Boolean)
            : this(spotIdx, spotName, isFirst, 0, 0F, "", null)

}

data class ReviewImg(
    @Expose(serialize = true, deserialize = true)
    @SerializedName("review_img_idx")
    val reviewImgIdx: Int,

    @Expose(serialize = true, deserialize = true)
    @SerializedName("review_Img_review_idx")
    val review_Img_review_idx: Int,

    @Expose(serialize = true, deserialize = true)
    @SerializedName("review_img_url")
    var review_img_url: String?,

    var img : Any?,
    var imgName : String? = null
) : Serializable
