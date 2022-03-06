package com.sdin.tourstamprally.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class TouristSpotPointDC(
    @SerializedName("touristspotpoint_idx")
    var touristspotpoint_idx: Int,

    @SerializedName("touristspotpoint_latitude")
    var touristspotpoint_latitude: Double,

    @SerializedName("touristspotpoint_longitude")
    val touristspotpoint_longitude: Double,

    @SerializedName("touristspotpoint_name")
    val touristspotpoint_name: String,

    @SerializedName("touristspotpoint_explan")
    val touristspotpoint_explan: String,


    @SerializedName("touristspotpoint_detail_explan")
    val touristspotpoint_detail_explan: String?,


    @SerializedName("touristspotpoint_img")
    val touristspotpoint_img: String?,

    @SerializedName("touristspotpoint_contactinfo")
    val touristspotpoint_contactinfo: String?,

    @SerializedName("touristspotpoint_course_number")
    val touristspotpoint_course_number: Int,


/*아래 부터는 History 테이블 같이 쓰일곳이 있어 놔둠*/

/*아래 부터는 History 테이블 같이 쓰일곳이 있어 놔둠*/
    @SerializedName("touristhistory_idx")
    val touristhistory_idx: Int,

    @SerializedName("touristspotpoint_address")
    val touristspotpoint_address: String?,

    @SerializedName("touristspotpoint_link")
    val touristspotpoint_link: String?,

    @SerializedName("touristspotpoint_videolink")
    val touristspotpoint_videolink: String?
) : Serializable
