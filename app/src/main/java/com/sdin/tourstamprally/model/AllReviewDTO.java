package com.sdin.tourstamprally.model;

import com.google.gson.annotations.SerializedName;

public class AllReviewDTO{

    @SerializedName("location_idx")
    private int location_idx;

    @SerializedName("location_name")
    private String location_name;

    @SerializedName("touristspot_idx")
    private int touristspot_idx;

    @SerializedName("touristspot_name")
    private String touristspot_name;

    @SerializedName("touristspot_img")
    private String touristspot_img;

    @SerializedName("review_idx")
    private int review_idx;

    @SerializedName("review_score")
    private float review_score;

    @SerializedName("review_contents")
    private String review_contents;

    @SerializedName("user_idx")
    private int user_idx;

    @SerializedName("user_name")
    private String user_name;

    @SerializedName("user_profile")
    private String user_profile;

    public AllReviewDTO() {
    }

    public AllReviewDTO(int location_idx, String location_name, int touristspot_idx, String touristspot_name, String touristspot_img, int review_idx, float review_score, String review_contents, int user_idx, String user_name, String user_profile) {
        this.location_idx = location_idx;
        this.location_name = location_name;
        this.touristspot_idx = touristspot_idx;
        this.touristspot_name = touristspot_name;
        this.touristspot_img = touristspot_img;
        this.review_idx = review_idx;
        this.review_score = review_score;
        this.review_contents = review_contents;
        this.user_idx = user_idx;
        this.user_name = user_name;
        this.user_profile = user_profile;
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

    public String getTouristspot_img() {
        return touristspot_img;
    }

    public void setTouristspot_img(String touristspot_img) {
        this.touristspot_img = touristspot_img;
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
}
