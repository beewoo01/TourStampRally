package com.sdin.tourstamprally.model

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Keep
data class TopFourLocationModel(
    @SerializedName("myInterCount")
    var myInterCount: Int = 0,
    @SerializedName("allSpotCount")
    val allSpotCount: Int = 0,
    @SerializedName("popular")
    val popular: Int = 0,
    @SerializedName("location_idx")
    val location_idx: Int = 0,
    @SerializedName("location_name")
    val location_name: String,
    @SerializedName("location_img")
    val location_img: String,
    @SerializedName("myHistoryCount")
    val myHistoryCount: Int = 0,
    @SerializedName("allPointCount")
    val allPointCount: Int = 0,
    @SerializedName("touristspot_latitude")
    val touristspot_latitude: Double = 0.0,
    @SerializedName("touristspot_longitude")
    val touristspot_longitude : Double = 0.0,
    @SerializedName("touristspotpoint_createtime")
    val touristspotpoint_createtime : String?

) : Serializable , Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readString().toString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(myInterCount)
        parcel.writeInt(allSpotCount)
        parcel.writeInt(popular)
        parcel.writeInt(location_idx)
        parcel.writeString(location_name)
        parcel.writeString(location_img)
        parcel.writeInt(myHistoryCount)
        parcel.writeInt(allPointCount)
        parcel.writeDouble(touristspot_latitude)
        parcel.writeDouble(touristspot_longitude)
        parcel.writeString(touristspotpoint_createtime)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TopFourLocationModel> {
        override fun createFromParcel(parcel: Parcel): TopFourLocationModel {
            return TopFourLocationModel(parcel)
        }

        override fun newArray(size: Int): Array<TopFourLocationModel?> {
            return arrayOfNulls(size)
        }
    }
}