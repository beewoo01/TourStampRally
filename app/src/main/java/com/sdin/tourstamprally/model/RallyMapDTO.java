package com.sdin.tourstamprally.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


public class RallyMapDTO implements Serializable {
    @SerializedName("touristspot_idx")
    private int touristspot_idx;
    @SerializedName("touristspot_name")
    private String touristspot_name;
    @SerializedName("touristspotpoint_idx")
    private int touristspotpoint_idx;
    @SerializedName("touristspot_latitude")
    private double touristspot_latitude;
    @SerializedName("touristspot_longitude")
    private double touristspot_longitude;
    @SerializedName("touristspot_address")
    private String touristspot_address;
    @SerializedName("allCount")
    private int allCount;
    @SerializedName("myCount")
    private int myCount;
    @SerializedName("touristspotpoint_name")
    private String touristspotpoint_name;
    @SerializedName("touristspot_img")
    private String touristspot_img;

    public RallyMapDTO() {
    }

    public RallyMapDTO(int touristspot_idx, String touristspot_name, int touristspotpoint_idx, double touristspot_latitude,
                       double touristspot_longitude, String touristspot_address, int allCount, int myCount,
                       String touristspotpoint_name, String touristspot_img) {
        this.touristspot_idx = touristspot_idx;
        this.touristspot_name = touristspot_name;
        this.touristspotpoint_idx = touristspotpoint_idx;
        this.touristspot_latitude = touristspot_latitude;
        this.touristspot_longitude = touristspot_longitude;
        this.touristspot_address = touristspot_address;
        this.allCount = allCount;
        this.myCount = myCount;
        this.touristspotpoint_name = touristspotpoint_name;
        this.touristspot_img = touristspot_img;
    }


    public int getTouristspot_idx() {
        return touristspot_idx;
    }

    public void setTouristspot_idx(int touristspot_idx) {
        this.touristspot_idx = touristspot_idx;
    }

    public String getTouristspot_name() {
        return touristspot_name;
    }

    public void setTouristspot_name(String touristspot_name) {
        this.touristspot_name = touristspot_name;
    }

    public int getTouristspotpoint_idx() {
        return touristspotpoint_idx;
    }

    public void setTouristspotpoint_idx(int touristspotpoint_idx) {
        this.touristspotpoint_idx = touristspotpoint_idx;
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

    public String getTouristspot_address() {
        return touristspot_address;
    }

    public void setTouristspot_address(String touristspot_address) {
        this.touristspot_address = touristspot_address;
    }

    public int getAllCount() {
        return allCount;
    }

    public void setAllCount(int allCount) {
        this.allCount = allCount;
    }

    public int getMyCount() {
        return myCount;
    }

    public void setMyCount(int myCount) {
        this.myCount = myCount;
    }

    public String getTouristspotpoint_name() {
        return touristspotpoint_name;
    }

    public void setTouristspotpoint_name(String touristspotpoint_name) {
        this.touristspotpoint_name = touristspotpoint_name;
    }

    public String getTouristspot_img() {
        return touristspot_img;
    }

    public void setTouristspot_img(String touristspot_img) {
        this.touristspot_img = touristspot_img;
    }
}
