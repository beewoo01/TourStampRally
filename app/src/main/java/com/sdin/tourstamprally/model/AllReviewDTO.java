package com.sdin.tourstamprally.model;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
}
