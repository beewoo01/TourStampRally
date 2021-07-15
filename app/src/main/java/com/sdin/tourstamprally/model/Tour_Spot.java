package com.sdin.tourstamprally.model;

import android.security.identity.EphemeralPublicKeyNotFoundException;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Tour_Spot implements Comparable<Tour_Spot>{

    @SerializedName("location_idx")
    private int location_idx;

    @SerializedName("location_name")
    private String location_name;

    @SerializedName("location_img")
    private String location_img;

    @SerializedName("location_createtime")
    private long location_createtime;

    @SerializedName("locatio_updatetime")
    private long locatio_updatetime;


    @SerializedName("touristspot_idx")
    private int touristspot_idx;

    @SerializedName("touristspot_location_location_idx")
    private int touristspot_location_location_idx;

    @SerializedName("touristspot_name")
    private String touristspot_name;

    @SerializedName("touristspot_latitude")
    private String touristspot_latitude;

    @SerializedName("touristspot_longitude")
    private String touristspot_longitude;

    @SerializedName("touristspot_tag")
    private String touristspot_tag;

    @SerializedName("touristspot_checkin_count")
    private int touristspot_checkin_count;

    @SerializedName("touristspot_createtime")
    private long touristspot_createtime;

    @SerializedName("touristspot_updatetime")
    private long touristspot_updatetime;


    @SerializedName("touristspotpoint_idx")
    private int touristspotpoint_idx;

    @SerializedName("touristspotpoint_touristspot_idx")
    private int touristspotpoint_touristspot_idx;

    @SerializedName("touristspotpoint_latitude")
    private String touristspotpoint_latitude;

    @SerializedName("touristspotpoint_longitude")
    private String touristspotpoint_longitude;


    @SerializedName("touristhistory_idx")
    private String touristhistory_idx;

    @SerializedName("touristhistory_touristspotpoint_idx")
    private String touristhistory_touristspotpoint_idx;

    @SerializedName("touristhistory_user_idx")
    private String touristhistory_user_idx;

    @SerializedName("touristhistory_createtime")
    private long touristhistory_createtime;


    @SerializedName("touristhistory_updatetime")
    private long touristhistory_updatetime;

    private boolean clear;



    @Override
    public int compareTo(Tour_Spot o) {
        return 0;
    }


}
