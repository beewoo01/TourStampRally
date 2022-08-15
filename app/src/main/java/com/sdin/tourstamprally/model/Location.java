package com.sdin.tourstamprally.model;

import com.google.gson.annotations.SerializedName;

public class Location {
    @SerializedName("location_idx")
    private int location_idx;
    @SerializedName("location_name")
    private String location_name;
    @SerializedName("location_img")
    private String location_img;
    @SerializedName("location_createtime")
    private String location_createtime;
    @SerializedName("location_updatetime")
    private String location_updatetime;

    public Location(){

    }

    public Location(int location_idx, String location_name){
        this.location_idx = location_idx;
        this.location_name = location_name;
    }

    public Location(int location_idx, String location_name, String location_img, String location_createtime, String location_updatetime) {
        this.location_idx = location_idx;
        this.location_name = location_name;
        this.location_img = location_img;
        this.location_createtime = location_createtime;
        this.location_updatetime = location_updatetime;
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

    public String getLocation_updatetime() {
        return location_updatetime;
    }

    public void setLocation_updatetime(String location_updatetime) {
        this.location_updatetime = location_updatetime;
    }

    @Override
    public String toString() {
        return "Location{" +
                "location_idx=" + location_idx +
                ", location_name='" + location_name + '\'' +
                ", location_img='" + location_img + '\'' +
                ", location_createtime='" + location_createtime + '\'' +
                ", location_updatetime='" + location_updatetime + '\'' +
                '}';
    }
}
