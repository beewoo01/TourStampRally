package com.sdin.tourstamprally.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.security.identity.EphemeralPublicKeyNotFoundException;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

public class Tour_Spot implements Comparable<Tour_Spot>, Serializable {

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

    @SerializedName("touristspot_explan")
    private String touristspot_explan;

    @SerializedName("touristspot_latitude")
    private double touristspot_latitude;

    @SerializedName("touristspot_longitude")
    private double touristspot_longitude;

    @SerializedName("touristspot_img")
    private String touristspot_img;

    @SerializedName("touristspot_tag")
    private String touristspot_tag;

    @SerializedName("touristspot_checkin_count")
    private int touristspot_checkin_count;

    @SerializedName("touristspot_createtime")
    private long touristspot_createtime;

    @SerializedName("touristspot_updatetime")
    private long touristspot_updatetime;


    @SerializedName("touristspotpoint_idx")
    private String touristspotpoint_idx;

    @SerializedName("touristspotpoint_touristspot_idx")
    private String touristspotpoint_touristspot_idx;

    @SerializedName("touristspotpoint_name")
    private String touristspotpoint_name;

    @SerializedName("touristspotpoint_explan")
    private String touristspotpoint_explan;

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

    private int location_percentage;

    private int tourspot_percentage;

    private int location_clearPercent;

    private int tourspot_clearPercent;

    private boolean clear;



    @SerializedName("user_touristspot_interest_idx")
    private int user_touristspot_interest_idx;

    @SerializedName("interest_user_idx")
    private int interest_user_idx;

    @SerializedName("interest_touristspot_idx")
    private int interest_touristspot_idx;

    @SerializedName("interest_createtime")
    private String interest_createtime;

    @SerializedName("interest_updatetime")
    private String interest_updatetime;


    public Tour_Spot() {
    }

    public Tour_Spot(int location_idx, String location_name, String location_img, String location_createtime,
                     String locatio_updatetime, int touristspot_idx, int touristspot_location_location_idx, String touristspot_name,
                     String touristspot_explan, double touristspot_latitude, double touristspot_longitude, String touristspot_img,
                     String touristspot_tag, int touristspot_checkin_count, long touristspot_createtime, long touristspot_updatetime,
                     String touristspotpoint_idx, String touristspotpoint_touristspot_idx, String touristspotpoint_name,
                     String touristspotpoint_explan, String touristspotpoint_latitude, String touristspotpoint_longitude,
                     String touristhistory_idx, String touristhistory_touristspotpoint_idx, String touristhistory_user_idx,
                     long touristhistory_createtime, long touristhistory_updatetime, int location_percentage, int tourspot_percentage,
                     int location_clearPercent, int tourspot_clearPercent, boolean clear, int user_touristspot_interest_idx,
                     int interest_user_idx, int interest_touristspot_idx, String interest_createtime, String interest_updatetime) {
        this.location_idx = location_idx;
        this.location_name = location_name;
        this.location_img = location_img;
        this.location_createtime = location_createtime;
        this.locatio_updatetime = locatio_updatetime;
        this.touristspot_idx = touristspot_idx;
        this.touristspot_location_location_idx = touristspot_location_location_idx;
        this.touristspot_name = touristspot_name;
        this.touristspot_explan = touristspot_explan;
        this.touristspot_latitude = touristspot_latitude;
        this.touristspot_longitude = touristspot_longitude;
        this.touristspot_img = touristspot_img;
        this.touristspot_tag = touristspot_tag;
        this.touristspot_checkin_count = touristspot_checkin_count;
        this.touristspot_createtime = touristspot_createtime;
        this.touristspot_updatetime = touristspot_updatetime;
        this.touristspotpoint_idx = touristspotpoint_idx;
        this.touristspotpoint_touristspot_idx = touristspotpoint_touristspot_idx;
        this.touristspotpoint_name = touristspotpoint_name;
        this.touristspotpoint_explan = touristspotpoint_explan;
        this.touristspotpoint_latitude = touristspotpoint_latitude;
        this.touristspotpoint_longitude = touristspotpoint_longitude;
        this.touristhistory_idx = touristhistory_idx;
        this.touristhistory_touristspotpoint_idx = touristhistory_touristspotpoint_idx;
        this.touristhistory_user_idx = touristhistory_user_idx;
        this.touristhistory_createtime = touristhistory_createtime;
        this.touristhistory_updatetime = touristhistory_updatetime;
        this.location_percentage = location_percentage;
        this.tourspot_percentage = tourspot_percentage;
        this.location_clearPercent = location_clearPercent;
        this.tourspot_clearPercent = tourspot_clearPercent;
        this.clear = clear;
        this.user_touristspot_interest_idx = user_touristspot_interest_idx;
        this.interest_user_idx = interest_user_idx;
        this.interest_touristspot_idx = interest_touristspot_idx;
        this.interest_createtime = interest_createtime;
        this.interest_updatetime = interest_updatetime;
    }

    public int getLocation_idx() {
        return location_idx;
    }

    public void setLocation_idx(int location_idx) {
        this.location_idx = location_idx;
    }

    public String getLocation_name() {
        return location_name;
    }

    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }

    public String getLocation_img() {
        return location_img;
    }

    public void setLocation_img(String location_img) {
        this.location_img = location_img;
    }

    public String getLocation_createtime() {
        return location_createtime;
    }

    public void setLocation_createtime(String location_createtime) {
        this.location_createtime = location_createtime;
    }

    public String getLocatio_updatetime() {
        return locatio_updatetime;
    }

    public void setLocatio_updatetime(String locatio_updatetime) {
        this.locatio_updatetime = locatio_updatetime;
    }

    public int getTouristspot_idx() {
        return touristspot_idx;
    }

    public void setTouristspot_idx(int touristspot_idx) {
        this.touristspot_idx = touristspot_idx;
    }

    public int getTouristspot_location_location_idx() {
        return touristspot_location_location_idx;
    }

    public void setTouristspot_location_location_idx(int touristspot_location_location_idx) {
        this.touristspot_location_location_idx = touristspot_location_location_idx;
    }

    public String getTouristspot_name() {
        return touristspot_name;
    }

    public void setTouristspot_name(String touristspot_name) {
        this.touristspot_name = touristspot_name;
    }

    public String getTouristspot_explan() {
        return touristspot_explan;
    }

    public void setTouristspot_explan(String touristspot_explan) {
        this.touristspot_explan = touristspot_explan;
    }

    public double getTouristspot_latitude() {
        return touristspot_latitude;
    }

    public void setTouristspot_latitude(double touristspot_latitude) {
        this.touristspot_latitude = touristspot_latitude;
    }

    public double getTouristspot_longitude() {
        return touristspot_longitude;
    }

    public void setTouristspot_longitude(double touristspot_longitude) {
        this.touristspot_longitude = touristspot_longitude;
    }

    public String getTouristspot_img() {
        return touristspot_img;
    }

    public void setTouristspot_img(String touristspot_img) {
        this.touristspot_img = touristspot_img;
    }

    public String getTouristspot_tag() {
        return touristspot_tag;
    }

    public void setTouristspot_tag(String touristspot_tag) {
        this.touristspot_tag = touristspot_tag;
    }

    public int getTouristspot_checkin_count() {
        return touristspot_checkin_count;
    }

    public void setTouristspot_checkin_count(int touristspot_checkin_count) {
        this.touristspot_checkin_count = touristspot_checkin_count;
    }

    public long getTouristspot_createtime() {
        return touristspot_createtime;
    }

    public void setTouristspot_createtime(long touristspot_createtime) {
        this.touristspot_createtime = touristspot_createtime;
    }

    public long getTouristspot_updatetime() {
        return touristspot_updatetime;
    }

    public void setTouristspot_updatetime(long touristspot_updatetime) {
        this.touristspot_updatetime = touristspot_updatetime;
    }

    public String getTouristspotpoint_idx() {
        return touristspotpoint_idx;
    }

    public void setTouristspotpoint_idx(String touristspotpoint_idx) {
        this.touristspotpoint_idx = touristspotpoint_idx;
    }

    public String getTouristspotpoint_touristspot_idx() {
        return touristspotpoint_touristspot_idx;
    }

    public void setTouristspotpoint_touristspot_idx(String touristspotpoint_touristspot_idx) {
        this.touristspotpoint_touristspot_idx = touristspotpoint_touristspot_idx;
    }

    public String getTouristspotpoint_name() {
        return touristspotpoint_name;
    }

    public void setTouristspotpoint_name(String touristspotpoint_name) {
        this.touristspotpoint_name = touristspotpoint_name;
    }

    public String getTouristspotpoint_explan() {
        return touristspotpoint_explan;
    }

    public void setTouristspotpoint_explan(String touristspotpoint_explan) {
        this.touristspotpoint_explan = touristspotpoint_explan;
    }

    public String getTouristspotpoint_latitude() {
        return touristspotpoint_latitude;
    }

    public void setTouristspotpoint_latitude(String touristspotpoint_latitude) {
        this.touristspotpoint_latitude = touristspotpoint_latitude;
    }

    public String getTouristspotpoint_longitude() {
        return touristspotpoint_longitude;
    }

    public void setTouristspotpoint_longitude(String touristspotpoint_longitude) {
        this.touristspotpoint_longitude = touristspotpoint_longitude;
    }

    public String getTouristhistory_idx() {
        return touristhistory_idx;
    }

    public void setTouristhistory_idx(String touristhistory_idx) {
        this.touristhistory_idx = touristhistory_idx;
    }

    public String getTouristhistory_touristspotpoint_idx() {
        return touristhistory_touristspotpoint_idx;
    }

    public void setTouristhistory_touristspotpoint_idx(String touristhistory_touristspotpoint_idx) {
        this.touristhistory_touristspotpoint_idx = touristhistory_touristspotpoint_idx;
    }

    public String getTouristhistory_user_idx() {
        return touristhistory_user_idx;
    }

    public void setTouristhistory_user_idx(String touristhistory_user_idx) {
        this.touristhistory_user_idx = touristhistory_user_idx;
    }

    public long getTouristhistory_createtime() {
        return touristhistory_createtime;
    }

    public void setTouristhistory_createtime(long touristhistory_createtime) {
        this.touristhistory_createtime = touristhistory_createtime;
    }

    public long getTouristhistory_updatetime() {
        return touristhistory_updatetime;
    }

    public void setTouristhistory_updatetime(long touristhistory_updatetime) {
        this.touristhistory_updatetime = touristhistory_updatetime;
    }

    public int getLocation_percentage() {
        return location_percentage;
    }

    public void setLocation_percentage(int location_percentage) {
        this.location_percentage = location_percentage;
    }

    public int getTourspot_percentage() {
        return tourspot_percentage;
    }

    public void setTourspot_percentage(int tourspot_percentage) {
        this.tourspot_percentage = tourspot_percentage;
    }

    public int getLocation_clearPercent() {
        return location_clearPercent;
    }

    public void setLocation_clearPercent(int location_clearPercent) {
        this.location_clearPercent = location_clearPercent;
    }

    public int getTourspot_clearPercent() {
        return tourspot_clearPercent;
    }

    public void setTourspot_clearPercent(int tourspot_clearPercent) {
        this.tourspot_clearPercent = tourspot_clearPercent;
    }

    public boolean isClear() {
        return clear;
    }

    public void setClear(boolean clear) {
        this.clear = clear;
    }

    public int getUser_touristspot_interest_idx() {
        return user_touristspot_interest_idx;
    }

    public void setUser_touristspot_interest_idx(int user_touristspot_interest_idx) {
        this.user_touristspot_interest_idx = user_touristspot_interest_idx;
    }

    public int getInterest_user_idx() {
        return interest_user_idx;
    }

    public void setInterest_user_idx(int interest_user_idx) {
        this.interest_user_idx = interest_user_idx;
    }

    public int getInterest_touristspot_idx() {
        return interest_touristspot_idx;
    }

    public void setInterest_touristspot_idx(int interest_touristspot_idx) {
        this.interest_touristspot_idx = interest_touristspot_idx;
    }

    public String getInterest_createtime() {
        return interest_createtime;
    }

    public void setInterest_createtime(String interest_createtime) {
        this.interest_createtime = interest_createtime;
    }

    public String getInterest_updatetime() {
        return interest_updatetime;
    }

    public void setInterest_updatetime(String interest_updatetime) {
        this.interest_updatetime = interest_updatetime;
    }


    @Override
    public int compareTo(Tour_Spot o) {
        return 0;
    }


}
