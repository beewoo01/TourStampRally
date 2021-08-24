package com.sdin.tourstamprally.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
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

}
