package com.sdin.tourstamprally.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TouristSpot {

    @SerializedName("touristspot_idx")
    @Expose
    private int touristspot_idx;

    @SerializedName("touristspot_location_location_idx")
    @Expose
    private int touristspot_location_location_idx;

    @SerializedName("touristspot_name")
    @Expose
    private String touristspot_name;

    @SerializedName("touristspot_latitude")
    @Expose
    private String touristspot_latitude;

    @SerializedName("touristspot_longitude")
    @Expose
    private String touristspot_longitude;

    @SerializedName("touristspot_tag")
    @Expose
    private String touristspot_tag;

    @SerializedName("touristspot_checkin_count")
    @Expose
    private int touristspot_checkin_count;

    public TouristSpot(){

    }

    public TouristSpot(int touristspot_idx, int touristspot_location_location_idx, String touristspot_name,
                       String touristspot_latitude, String touristspot_longitude, String touristspot_tag, int touristspot_checkin_count) {
        this.touristspot_idx = touristspot_idx;
        this.touristspot_location_location_idx = touristspot_location_location_idx;
        this.touristspot_name = touristspot_name;
        this.touristspot_latitude = touristspot_latitude;
        this.touristspot_longitude = touristspot_longitude;
        this.touristspot_tag = touristspot_tag;
        this.touristspot_checkin_count = touristspot_checkin_count;
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

    public String getTouristspot_latitude() {
        return touristspot_latitude;
    }

    public void setTouristspot_latitude(String touristspot_latitude) {
        this.touristspot_latitude = touristspot_latitude;
    }

    public String getTouristspot_longitude() {
        return touristspot_longitude;
    }

    public void setTouristspot_longitude(String touristspot_longitude) {
        this.touristspot_longitude = touristspot_longitude;
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
}
