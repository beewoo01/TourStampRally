package com.sdin.tourstamprally.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class TouristSpotPointDC(

    @SerializedName("test_touristspotpoint_idx")
    var touristspotpoint_idx: Int,

    @SerializedName("test_touristspotpoint_latitude")
    var touristspotpoint_latitude: Double,

    @SerializedName("test_touristspotpoint_longitude")
    val touristspotpoint_longitude: Double,

    @SerializedName("test_touristspotpoint_name")
    val touristspotpoint_name: String,

    @SerializedName("test_touristspotpoint_explan")
    val touristspotpoint_explan: String,


    @SerializedName("test_touristspotpoint_detail_explan")
    val touristspotpoint_detail_explan: String?,


    @SerializedName("test_touristspotpoint_curver_img")
    val touristSpotPointCurverImg: String?,

    @SerializedName("test_touristspotpoint_number")
    val touristspotpoint_contactinfo: String?,

    @SerializedName("touristspot_point_sub_course_num")
    val touristspotpoint_course_number: Int,

/*아래 부터는 History 테이블 같이 쓰일곳이 있어 놔둠*/
    @SerializedName("test_touristhistory_idx")
    val touristhistory_idx: Int,

    @SerializedName("test_touristspotpoint_address")
    val touristspotpoint_address: String?,

    @SerializedName("test_touristspotpoint_link")
    val touristspotpoint_link: String?,

    @SerializedName("test_touristspotpoint_videolink")
    val touristspotpoint_videolink: String?,

    @SerializedName("touristSpotPointImgList")
    var touristSpotPointImgModelList : List<TouristSpotPointImg>?

) : Serializable

data class TouristSpotPointImg(
    @SerializedName("touristspotpoint_img_idx")
    val touristspotpoint_img_idx : Int,
    @SerializedName("touristspotpoint_idx")
    val touristspotpoint_idx : Int,
    @SerializedName("touristspotpoint_img_url")
    val touristspotpoint_img_url : String
) : Serializable