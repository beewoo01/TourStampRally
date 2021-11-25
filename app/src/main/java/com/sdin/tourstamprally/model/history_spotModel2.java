package com.sdin.tourstamprally.model;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


public class history_spotModel2 {

    @SerializedName("allCount")
    private int allCount;

    @SerializedName("myCount")
    private int myCount;

    @SerializedName("location_idx")
    private int location_idx;

    @SerializedName("location_name")
    private String location_name;

    @SerializedName("touristspot_idx")
    private int touristspot_idx;

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

    @SerializedName("touristhistory_updatetime")
    private String touristhistory_updatetime;

    @SerializedName("review_idx")
    private int review_idx;

    @SerializedName("review_score")
    private float review_score;

    @SerializedName("review_contents")
    private String review_contents;


    public history_spotModel2() {
    }

    public history_spotModel2(int allCount, int myCount, int location_idx, String location_name, int touristspot_idx, String touristspot_name, String touristspot_explan, double touristspot_latitude, double touristspot_longitude,
                              String touristspot_img, String touristhistory_updatetime, int review_idx, float review_score, String review_contents) {
        this.allCount = allCount;
        this.myCount = myCount;
        this.location_idx = location_idx;
        this.location_name = location_name;
        this.touristspot_idx = touristspot_idx;
        this.touristspot_name = touristspot_name;
        this.touristspot_explan = touristspot_explan;
        this.touristspot_latitude = touristspot_latitude;
        this.touristspot_longitude = touristspot_longitude;
        this.touristspot_img = touristspot_img;
        this.touristhistory_updatetime = touristhistory_updatetime;
        this.review_idx = review_idx;
        this.review_score = review_score;
        this.review_contents = review_contents;
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

    public String getTouristhistory_updatetime() {
        return touristhistory_updatetime;
    }

    public void setTouristhistory_updatetime(String touristhistory_updatetime) {
        this.touristhistory_updatetime = touristhistory_updatetime;
    }

    public int getReview_idx() {
        return review_idx;
    }

    public void setReview_idx(int review_idx) {
        this.review_idx = review_idx;
    }

    public float getReview_score() {
        return review_score;
    }

    public void setReview_score(float review_score) {
        this.review_score = review_score;
    }

    public String getReview_contents() {
        return review_contents;
    }

    public void setReview_contents(String review_contents) {
        this.review_contents = review_contents;
    }
}
