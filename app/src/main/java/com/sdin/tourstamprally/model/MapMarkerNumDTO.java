package com.sdin.tourstamprally.model;

import com.google.gson.annotations.SerializedName;

public class MapMarkerNumDTO {
    @SerializedName("test_touristspotpoint_idx")
    private int test_touristspotpoint_idx;

    @SerializedName("touristspot_point_sub_touristspot_idx")
    private int touristspot_point_sub_touristspot_idx;

    @SerializedName("touristspot_point_sub_course_num")
    private int touristspot_point_sub_course_num;

    @SerializedName("test_touristspotpoint_name")
    private String test_touristspotpoint_name;

    public int getTest_touristspotpoint_idx() {
        return test_touristspotpoint_idx;
    }

    public void setTest_touristspotpoint_idx(int test_touristspotpoint_idx) {
        this.test_touristspotpoint_idx = test_touristspotpoint_idx;
    }

    public int getTouristspot_point_sub_touristspot_idx() {
        return touristspot_point_sub_touristspot_idx;
    }

    public void setTouristspot_point_sub_touristspot_idx(int touristspot_point_sub_touristspot_idx) {
        this.touristspot_point_sub_touristspot_idx = touristspot_point_sub_touristspot_idx;
    }

    public int getTouristspot_point_sub_course_num() {
        return touristspot_point_sub_course_num;
    }

    public void setTouristspot_point_sub_course_num(int touristspot_point_sub_course_num) {
        this.touristspot_point_sub_course_num = touristspot_point_sub_course_num;
    }

    public String getTest_touristspotpoint_name() {
        return test_touristspotpoint_name;
    }

    public void setTest_touristspotpoint_name(String test_touristspotpoint_name) {
        this.test_touristspotpoint_name = test_touristspotpoint_name;
    }

    public MapMarkerNumDTO() {
    }

    public MapMarkerNumDTO(int test_touristspotpoint_idx, int touristspot_point_sub_touristspot_idx, int touristspot_point_sub_course_num, String test_touristspotpoint_name) {
        this.test_touristspotpoint_idx = test_touristspotpoint_idx;
        this.touristspot_point_sub_touristspot_idx = touristspot_point_sub_touristspot_idx;
        this.touristspot_point_sub_course_num = touristspot_point_sub_course_num;
        this.test_touristspotpoint_name = test_touristspotpoint_name;
    }
}
