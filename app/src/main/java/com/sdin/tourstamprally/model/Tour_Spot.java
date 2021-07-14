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
    private String location_createtime;

    @SerializedName("locatio_updatetime")
    private String locatio_updatetime;


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
    private String touristspot_createtime;

    @SerializedName("touristspot_updatetime")
    private String touristspot_updatetime;


    @SerializedName("touristspotpoint_idx")
    private int touristspotpoint_idx;

    @SerializedName("touristspotpoint_touristspot_idx")
    private int touristspotpoint_touristspot_idx;

    @SerializedName("touristspotpoint_latitude")
    private String touristspotpoint_latitude;

    @SerializedName("touristspotpoint_longitude")
    private String touristspotpoint_longitude;


    @SerializedName("touristhistory_idx")
    private int touristhistory_idx;

    @SerializedName("touristhistory_touristspotpoint_idx")
    private int touristhistory_touristspotpoint_idx;

    @SerializedName("touristhistory_user_idx")
    private int touristhistory_user_idx;

    @SerializedName("touristhistory_createtime")
    private int touristhistory_createtime;


    @SerializedName("touristhistory_updatetime")
    private int touristhistory_updatetime;

    private boolean clear;






    /*private int spotIdx;
    private int location_idx;
    private String spotName;
    private String spotLatitude;
    private String spotlongitude;
    private String spotUserIdx;
    private String spotTag;
    private String imgUrl;
    private boolean isClear;
    private String createTime;
    private String updateTime;
    private String clearTime;

    public Tour_Spot(int spotIdx, int location_idx, String spotName, String spotLatitude, String spotlongitude, String spotTag, String imgUrl, boolean isClear, String createTime, String updateTime) {
        this.spotIdx = spotIdx;
        this.location_idx = location_idx;
        this.spotName = spotName;
        this.spotLatitude = spotLatitude;
        this.spotlongitude = spotlongitude;
        this.spotTag = spotTag;
        this.imgUrl = imgUrl;
        this.isClear = isClear;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public Tour_Spot(int spotIdx, int location_idx, String spotName, String spotLatitude, String spotlongitude, String spotTag, String imgUrl, boolean isClear) {
        this.spotIdx = spotIdx;
        this.location_idx = location_idx;
        this.spotName = spotName;
        this.spotLatitude = spotLatitude;
        this.spotlongitude = spotlongitude;
        this.spotTag = spotTag;
        this.imgUrl = imgUrl;
        this.isClear = isClear;
    }*/

    @Override
    public int compareTo(Tour_Spot o) {
        return 0;
        //SELECT a.location_name, b.touristspot_name , b.touristspot_latitude, b.touristspot_longitude
        // FROM bsr.location AS a
        // JOIN bsr.touristspot AS b
        // ON a.location_idx = b.touristspot_location_location_idx
    }


}
