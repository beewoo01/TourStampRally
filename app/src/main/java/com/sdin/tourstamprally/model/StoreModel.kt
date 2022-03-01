package com.sdin.tourstamprally.model

import android.os.Parcel
import android.os.Parcelable
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

    var storeSubDto: StoreSubDTO? = null


) : Serializable {

    /*override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(store_idx)
        dest.writeInt(store_location_idx)
        dest.writeInt(store_touristspot_idx)
        dest.writeString(location_name)
        dest.writeString(store_name)
        dest.writeString(store_description)
        dest.writeString(store_info)
        dest.writeInt(store_type)
        dest.writeString(store_curver_img)
        dest.writeString(store_number)
        dest.writeString(store_address)
        dest.writeString(store_link)
        dest.writeInt(store_isfranchise)
        dest.writeString(store_latitude)
        dest.writeString(store_longitude)
    }

    companion object CREATOR : Parcelable.Creator<StoreModel> {
        override fun createFromParcel(parcel: Parcel): StoreModel {
            return StoreModel(parcel)
        }

        override fun newArray(size: Int): Array<StoreModel?> {
            return arrayOfNulls(size)
        }
    }

    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        null
        )
*/
}

data class StoreSubDTO(
    @SerializedName("storeHashTagList")
    val storeHashTagList: List<StoreHashtag>,
    @SerializedName("storeSubimgList")
    val storeSubimgList: List<StoreSubimg>
)

data class StoreHashtag(
    @SerializedName("store_hashtag_idx")
    val store_hashtag_idx: Int,
    @SerializedName("store_hashtag_store_idx")
    val store_hashtag_store_idx: Int,
    @SerializedName("store_hashtag_tag")
    val store_hashtag_tag: String
)

data class StoreSubimg(
    @SerializedName("store_subimg_idx")
    val store_subimg_idx: Int,
    @SerializedName("store_subimg_store_idx")
    val store_subimg_store_idx: Int,
    @SerializedName("store_subimg_url")
    val store_subimg_url: String
)
