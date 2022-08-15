package com.sdin.tourstamprally.model;

import com.google.gson.annotations.SerializedName;

public class TaggingnoCourseDTO {

    @SerializedName("touristspot_point_sub_idx")
    private int touristspot_point_sub_idx;

    @SerializedName("touristspot_location_location_idx")
    private int touristspot_location_location_idx;

    @SerializedName("test_touristspotpoint_name")
    private String test_touristspotpoint_name;

    @SerializedName("touristspotpoint_tag")
    private String touristspotpoint_tag;

    @SerializedName("test_touristspotpoint_tagging_info")
    private String test_touristspotpoint_tagging_info;

    @SerializedName("location_name")
    private String location_name;

    @SerializedName("touristspot_name")
    private String touristspot_name;

    @SerializedName("test_touristspotpoint_latitude")
    private double test_touristspotpoint_latitude;

    @SerializedName("test_touristspotpoint_longitude")
    private double test_touristspotpoint_longitude;

    @SerializedName("touristspot_idx")
    private int touristspot_idx;

    public int getTouristspot_point_sub_idx() {
        return touristspot_point_sub_idx;
    }

    public void setTouristspot_point_sub_idx(int touristspot_point_sub_idx) {
        this.touristspot_point_sub_idx = touristspot_point_sub_idx;
    }

    public int getTouristspot_location_location_idx() {
        return touristspot_location_location_idx;
    }

    public void setTouristspot_location_location_idx(int touristspot_location_location_idx) {
        this.touristspot_location_location_idx = touristspot_location_location_idx;
    }

    public String getTest_touristspotpoint_name() {
        return test_touristspotpoint_name;
    }

    public void setTest_touristspotpoint_name(String test_touristspotpoint_name) {
        this.test_touristspotpoint_name = test_touristspotpoint_name;
    }

    public String getTouristspotpoint_tag() {
        return touristspotpoint_tag;
    }

    public void setTouristspotpoint_tag(String touristspotpoint_tag) {
        this.touristspotpoint_tag = touristspotpoint_tag;
    }

    public String getTest_touristspotpoint_tagging_info() {
        return test_touristspotpoint_tagging_info;
    }

    public void setTest_touristspotpoint_tagging_info(String test_touristspotpoint_tagging_info) {
        this.test_touristspotpoint_tagging_info = test_touristspotpoint_tagging_info;
    }

    public String getLocation_name() {
        return location_name;
    }

    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }

    public String getTouristspot_name() {
        return touristspot_name;
    }

    public void setTouristspot_name(String touristspot_name) {
        this.touristspot_name = touristspot_name;
    }

    public double getTest_touristspotpoint_latitude() {
        return test_touristspotpoint_latitude;
    }

    public void setTest_touristspotpoint_latitude(double test_touristspotpoint_latitude) {
        this.test_touristspotpoint_latitude = test_touristspotpoint_latitude;
    }

    public double getTest_touristspotpoint_longitude() {
        return test_touristspotpoint_longitude;
    }

    public void setTest_touristspotpoint_longitude(double test_touristspotpoint_longitude) {
        this.test_touristspotpoint_longitude = test_touristspotpoint_longitude;
    }

    public int getTouristspot_idx() {
        return touristspot_idx;
    }

    public void setTouristspot_idx(int touristspot_idx) {
        this.touristspot_idx = touristspot_idx;
    }

    public TaggingnoCourseDTO() {
    }

    public TaggingnoCourseDTO(int touristspot_point_sub_idx, int touristspot_location_location_idx, String test_touristspotpoint_name, String touristspotpoint_tag, String test_touristspotpoint_tagging_info, String location_name, String touristspot_name, double test_touristspotpoint_latitude, double test_touristspotpoint_longitude, int touristspot_idx) {
        this.touristspot_point_sub_idx = touristspot_point_sub_idx;
        this.touristspot_location_location_idx = touristspot_location_location_idx;
        this.test_touristspotpoint_name = test_touristspotpoint_name;
        this.touristspotpoint_tag = touristspotpoint_tag;
        this.test_touristspotpoint_tagging_info = test_touristspotpoint_tagging_info;
        this.location_name = location_name;
        this.touristspot_name = touristspot_name;
        this.test_touristspotpoint_latitude = test_touristspotpoint_latitude;
        this.test_touristspotpoint_longitude = test_touristspotpoint_longitude;
        this.touristspot_idx = touristspot_idx;
    }
}
