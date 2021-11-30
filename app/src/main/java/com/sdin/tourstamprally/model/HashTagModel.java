package com.sdin.tourstamprally.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class HashTagModel implements Serializable {

    @SerializedName("touristspot_location_location_idx")
    private int touristspot_location_location_idx;

    @SerializedName("touristspot_idx")
    private int touristspot_idx;

    @SerializedName("touristspot_tag")
    private String touristspot_tag;

    public HashTagModel(){

    }

    public HashTagModel(int touristspot_location_location_idx, int touristspot_idx, String touristspot_tag) {
        this.touristspot_location_location_idx = touristspot_location_location_idx;
        this.touristspot_idx = touristspot_idx;
        this.touristspot_tag = touristspot_tag;
    }

    public int getTouristspot_location_location_idx() {
        return touristspot_location_location_idx;
    }

    public void setTouristspot_location_location_idx(int touristspot_location_location_idx) {
        this.touristspot_location_location_idx = touristspot_location_location_idx;
    }

    public int getTouristspot_idx() {
        return touristspot_idx;
    }

    public void setTouristspot_idx(int touristspot_idx) {
        this.touristspot_idx = touristspot_idx;
    }

    public String getTouristspot_tag() {
        return touristspot_tag;
    }

    public void setTouristspot_tag(String touristspot_tag) {
        this.touristspot_tag = touristspot_tag;
    }
}
