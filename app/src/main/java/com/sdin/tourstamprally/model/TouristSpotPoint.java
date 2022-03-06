package com.sdin.tourstamprally.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class TouristSpotPoint implements Serializable {

    @SerializedName("touristspotpoint_idx")
    @Expose
    private int touristspotpoint_idx;

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


    @SerializedName("touristspotpoint_detail_explan")
    @Expose
    private String touristspotpoint_detail_explan;


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


    @SerializedName("touristspotpoint_videolink")
    @Expose
    private String touristspotpoint_videolink;


    public TouristSpotPoint() {

    }

    public TouristSpotPoint(int touristspotpoint_idx, double touristspotpoint_latitude, double touristspotpoint_longitude, String touristspotpoint_name,
                            String touristspotpoint_explan, String touristspotpoint_detail_explan, String touristspotpoint_img, String touristspotpoint_contactinfo,
                            int touristspotpoint_course_number, int touristhistory_idx, String touristspotpoint_address, String touristspotpoint_link, String touristspotpoint_videolink) {
        this.touristspotpoint_idx = touristspotpoint_idx;
        this.touristspotpoint_latitude = touristspotpoint_latitude;
        this.touristspotpoint_longitude = touristspotpoint_longitude;
        this.touristspotpoint_name = touristspotpoint_name;
        this.touristspotpoint_explan = touristspotpoint_explan;
        this.touristspotpoint_detail_explan = touristspotpoint_detail_explan;
        this.touristspotpoint_img = touristspotpoint_img;
        this.touristspotpoint_contactinfo = touristspotpoint_contactinfo;
        this.touristspotpoint_course_number = touristspotpoint_course_number;
        this.touristhistory_idx = touristhistory_idx;
        this.touristspotpoint_address = touristspotpoint_address;
        this.touristspotpoint_link = touristspotpoint_link;
        this.touristspotpoint_videolink = touristspotpoint_videolink;
    }

    public double getTouristspotpoint_latitude() {
        return touristspotpoint_latitude;
    }

    public double getTouristspotpoint_longitude() {
        return touristspotpoint_longitude;
    }

    public String getTouristspotpoint_name() {
        return touristspotpoint_name;
    }

    public String getTouristspotpoint_explan() {
        return touristspotpoint_explan;
    }

    public String getTouristspotpoint_detail_explan() {
        return touristspotpoint_detail_explan;
    }

    public String getTouristspotpoint_img() {
        return touristspotpoint_img;
    }

    public String getTouristspotpoint_contactinfo() {
        return touristspotpoint_contactinfo;
    }

    public int getTouristhistory_idx() {
        return touristhistory_idx;
    }

    public String getTouristspotpoint_videolink() {
        return touristspotpoint_videolink;
    }

}
