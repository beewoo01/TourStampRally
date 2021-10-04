package com.sdin.tourstamprally.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TouristSpotPoint implements Serializable {

    @SerializedName("touristspotpoint_idx")
    @Expose
    private int touristspotpoint_idx;

    /*@SerializedName("touristspotpoint_touristspot_idx")
    @Expose
    private int touristspotpoint_touristspot_idx;*/

    @SerializedName("touristspotpoint_latitude")
    @Expose
    private double touristspotpoint_latitude;

    @SerializedName("touristspotpoint_longitude")
    @Expose
    private double touristspotpoint_longitude;

    @SerializedName("touristspotpoint_name")
    @Expose
    private String touristspotpoint_name;

    @SerializedName("touristspotpoint_explan")
    @Expose
    private String touristspotpoint_explan;

    /*@SerializedName("touristspotpoint_tag")
    @Expose
    private String touristspotpoint_tag;


    @SerializedName("touristspotpoint_taggin_info")
    @Expose
    private String touristspotpoint_taggin_info;*/


    @SerializedName("touristspotpoint_img")
    @Expose
    private String touristspotpoint_img;

    @SerializedName("touristspotpoint_contactinfo")
    @Expose
    private String touristspotpoint_contactinfo;

    @SerializedName("touristspotpoint_course_number")
    @Expose
    private int touristspotpoint_course_number;



    /*아래 부터는 History 테이블 같이 쓰일곳이 있어 놔둠*/

    @SerializedName("touristhistory_idx")
    @Expose
    private int touristhistory_idx;

    @SerializedName("touristspotpoint_address")
    @Expose
    private String touristspotpoint_address;

    @SerializedName("touristspotpoint_link")
    @Expose
    private String touristspotpoint_link;

    /*@SerializedName("touristhistory_touristspotpoint_idx")
    @Expose
    private String touristhistory_touristspotpoint_idx;

    @SerializedName("touristhistory_user_idx")
    @Expose
    private String touristhistory_user_idx;

    @SerializedName("touristhistory_createtime")
    @Expose
    private long touristhistory_createtime;

    @SerializedName("touristhistory_updatetime")
    @Expose
    private long touristhistory_updatetime;*/

}
