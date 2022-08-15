package com.sdin.tourstamprally.model.store_coupon

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class  StoreMyCouponModel(
    @SerializedName("store_name")
    val store_name : String,
    @SerializedName("store_logo_icon")
    val store_logo_icon : String,
    @SerializedName("store_mycoupon_idx")
    val store_mycoupon_idx: Int,
    @SerializedName("store_mycoupon_store_coupon_idx")
    val store_mycoupon_store_coupon_idx : Int,
    @SerializedName("store_mycoupon_state")
    val store_mycoupon_state: Int,
    @SerializedName("store_coupon_number")
    val store_coupon_number : String,
    @SerializedName("store_mycoupon_createtime")
    val store_mycoupon_createtime : String,
    @SerializedName("store_coupon_store_idx")
    val store_coupon_store_idx : Int,
    @SerializedName("store_coupon_name")
    val store_coupon_name : String,
    @SerializedName("store_coupon_expiration_startDate")
    val store_coupon_expiration_startDate : String,
    @SerializedName("store_coupon_expiration_endDate")
    val store_coupon_expiration_endDate : String,

): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(store_name)
        parcel.writeString(store_logo_icon)
        parcel.writeInt(store_mycoupon_idx)
        parcel.writeInt(store_mycoupon_store_coupon_idx)
        parcel.writeInt(store_mycoupon_state)
        parcel.writeString(store_coupon_number)
        parcel.writeString(store_mycoupon_createtime)
        parcel.writeInt(store_coupon_store_idx)
        parcel.writeString(store_coupon_name)
        parcel.writeString(store_coupon_expiration_startDate)
        parcel.writeString(store_coupon_expiration_endDate)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<StoreMyCouponModel> {
        override fun createFromParcel(parcel: Parcel): StoreMyCouponModel {
            return StoreMyCouponModel(parcel)
        }

        override fun newArray(size: Int): Array<StoreMyCouponModel?> {
            return arrayOfNulls(size)
        }
    }


}