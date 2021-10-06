package com.sdin.tourstamprally.model;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
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


}
