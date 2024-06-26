package com.sdin.tourstamprally.model

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import java.io.Serializable


@Keep
data class StoreModel(
    @SerializedName("store_idx")
    val store_idx: Int,
    @SerializedName("store_location_idx")
    val store_location_idx: Int,
    @SerializedName("store_touristspot_idx")
    val store_touristspot_idx: Int,
    @SerializedName("location_name")
    val location_name: String,
    @SerializedName("store_name")
    val store_name: String,
    @SerializedName("store_description")
    val store_description: String,
    @SerializedName("store_info")
    val store_info: String,
    @SerializedName("store_type")
    val store_type: Int,
    @SerializedName("store_curver_img")
    val store_curver_img: String,
    @SerializedName("store_number")
    val store_number: String?,
    @SerializedName("store_address")
    val store_address: String,
    @SerializedName("store_link")
    val store_link: String,
    @SerializedName("store_isfranchise")
    val store_isfranchise: Int,
    @SerializedName("store_latitude")
    val store_latitude: String,
    @SerializedName("store_longitude")
    val store_longitude: String,
    @SerializedName("user_store_interest_idx")
    var user_store_interest_idx: Int,

    var storeSubDto: StoreSubDTO? = null


) : Serializable

data class StoreSubDTO(
    @SerializedName("storeHashTagList")
    val storeHashTagList: List<StoreHashtag>,
    @SerializedName("storeSubimgList")
    val storeSubimgList: List<StoreSubimg>
) : Serializable

data class StoreHashtag(
    @SerializedName("store_hashtag_idx")
    val store_hashtag_idx: Int,
    @SerializedName("store_hashtag_store_idx")
    val store_hashtag_store_idx: Int,
    @SerializedName("store_hashtag_tag")
    val store_hashtag_tag: String
) : Serializable

data class StoreSubimg(
    @SerializedName("store_subimg_idx")
    val store_subimg_idx: Int,
    @SerializedName("store_subimg_store_idx")
    val store_subimg_store_idx: Int,
    @SerializedName("store_subimg_url")
    val store_subimg_url: String
) : Serializable
