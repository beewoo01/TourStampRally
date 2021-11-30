package com.sdin.tourstamprally.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class TouristHistory implements Serializable {

    @SerializedName("touristhistory_idx")
    @Expose
    private int touristhistory_idx;

    @SerializedName("touristhistory_touristspotpoint_idx")
    @Expose
    private int touristhistory_touristspotpoint_idx;

    @SerializedName("touristhistory_user_idx")
    @Expose
    private int touristhistory_user_idx;

    @SerializedName("touristhistory_createtime")
    @Expose
    private int touristhistory_createtime;

    @SerializedName("touristhistory_updatetime")
    @Expose
    private int touristhistory_updatetime;


    public TouristHistory() {

    }

    public TouristHistory(int touristhistory_idx, int touristhistory_touristspotpoint_idx, int touristhistory_user_idx, int touristhistory_createtime, int touristhistory_updatetime) {
        this.touristhistory_idx = touristhistory_idx;
        this.touristhistory_touristspotpoint_idx = touristhistory_touristspotpoint_idx;
        this.touristhistory_user_idx = touristhistory_user_idx;
        this.touristhistory_createtime = touristhistory_createtime;
        this.touristhistory_updatetime = touristhistory_updatetime;
    }

    public int getTouristhistory_idx() {
        return touristhistory_idx;
    }

    public void setTouristhistory_idx(int touristhistory_idx) {
        this.touristhistory_idx = touristhistory_idx;
    }

    public int getTouristhistory_touristspotpoint_idx() {
        return touristhistory_touristspotpoint_idx;
    }

    public void setTouristhistory_touristspotpoint_idx(int touristhistory_touristspotpoint_idx) {
        this.touristhistory_touristspotpoint_idx = touristhistory_touristspotpoint_idx;
    }

    public int getTouristhistory_user_idx() {
        return touristhistory_user_idx;
    }

    public void setTouristhistory_user_idx(int touristhistory_user_idx) {
        this.touristhistory_user_idx = touristhistory_user_idx;
    }

    public int getTouristhistory_createtime() {
        return touristhistory_createtime;
    }

    public void setTouristhistory_createtime(int touristhistory_createtime) {
        this.touristhistory_createtime = touristhistory_createtime;
    }

    public int getTouristhistory_updatetime() {
        return touristhistory_updatetime;
    }

    public void setTouristhistory_updatetime(int touristhistory_updatetime) {
        this.touristhistory_updatetime = touristhistory_updatetime;
    }

}
