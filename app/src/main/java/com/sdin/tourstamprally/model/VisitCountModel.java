package com.sdin.tourstamprally.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class VisitCountModel {

    @SerializedName("touristspotpoint_touristspot_idx")
    @Expose
    private String touristspotpoint_touristspot_idx;

    @SerializedName("allcount")
    @Expose
    private String allcount;

    @SerializedName("mycount")
    @Expose
    private String mycount;

    public VisitCountModel() {

    }

    public VisitCountModel(String touristspotpoint_touristspot_idx, String allcount, String mycount) {
        this.touristspotpoint_touristspot_idx = touristspotpoint_touristspot_idx;
        this.allcount = allcount;
        this.mycount = mycount;
    }

    public String getTouristspotpoint_touristspot_idx() {
        return touristspotpoint_touristspot_idx;
    }

    public void setTouristspotpoint_touristspot_idx(String touristspotpoint_touristspot_idx) {
        this.touristspotpoint_touristspot_idx = touristspotpoint_touristspot_idx;
    }

    public String getAllcount() {
        return allcount;
    }

    public void setAllcount(String allcount) {
        this.allcount = allcount;
    }

    public String getMycount() {
        return mycount;
    }

    public void setMycount(String mycount) {
        this.mycount = mycount;
    }
}
