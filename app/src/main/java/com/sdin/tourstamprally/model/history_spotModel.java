package com.sdin.tourstamprally.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class history_spotModel {

    @SerializedName("location_name")
    @Expose
    private String location_name;

    @SerializedName("touristspot_idx")
    @Expose
    private int touristspot_idx;

    @SerializedName("touristspot_name")
    @Expose
    private String touristspot_name;

    @SerializedName("touristspot_explan")
    @Expose
    private String touristspot_explan;

    @SerializedName("touristspot_latitude")
    @Expose
    private String touristspot_latitude;

    @SerializedName("touristspot_longitude")
    @Expose
    private String touristspot_longitude;

    @SerializedName("touristspot_img")
    @Expose
    private String touristspot_img;

    @SerializedName("touristhistory_updatetime")
    @Expose
    private String touristhistory_updatetime;

    @SerializedName("review_idx")
    @Expose
    private int review_idx;

    @SerializedName("review_touristspot_touristspot_idx")
    @Expose
    private int review_touristspot_touristspot_idx;

    @SerializedName("review_user_user_idx")
    @Expose
    private int review_user_user_idx;

    @SerializedName("review_score")
    @Expose
    private float review_score;

    @SerializedName("review_contents")
    @Expose
    private String review_contents;

    @SerializedName("user_idx")
    @Expose
    private int user_idx;

    @SerializedName("user_name")
    @Expose
    private String user_name;

    @SerializedName("user_profile")
    @Expose
    private String user_profile;


    private int percent;

    public history_spotModel(){

    }

    public history_spotModel(String location_name, int touristspot_idx, String touristspot_name,
                             String touristspot_explan, String touristspot_latitude, String touristspot_longitude,
                             String touristspot_img, String touristhistory_updatetime, int review_idx, int review_touristspot_touristspot_idx,
                             int review_user_user_idx, float review_score, String review_contents, int user_idx,
                             String user_name, String user_profile, int percent) {

        this.location_name = location_name;
        this.touristspot_idx = touristspot_idx;
        this.touristspot_name = touristspot_name;
        this.touristspot_explan = touristspot_explan;
        this.touristspot_latitude = touristspot_latitude;
        this.touristspot_longitude = touristspot_longitude;
        this.touristspot_img = touristspot_img;
        this.touristhistory_updatetime = touristhistory_updatetime;
        this.review_idx = review_idx;
        this.review_touristspot_touristspot_idx = review_touristspot_touristspot_idx;
        this.review_user_user_idx = review_user_user_idx;
        this.review_score = review_score;
        this.review_contents = review_contents;
        this.user_idx = user_idx;
        this.user_name = user_name;
        this.user_profile = user_profile;
        this.percent = percent;
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

    public int getReview_touristspot_touristspot_idx() {
        return review_touristspot_touristspot_idx;
    }

    public void setReview_touristspot_touristspot_idx(int review_touristspot_touristspot_idx) {
        this.review_touristspot_touristspot_idx = review_touristspot_touristspot_idx;
    }

    public int getReview_user_user_idx() {
        return review_user_user_idx;
    }

    public void setReview_user_user_idx(int review_user_user_idx) {
        this.review_user_user_idx = review_user_user_idx;
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

    public int getUser_idx() {
        return user_idx;
    }

    public void setUser_idx(int user_idx) {
        this.user_idx = user_idx;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_profile() {
        return user_profile;
    }

    public void setUser_profile(String user_profile) {
        this.user_profile = user_profile;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }
}
