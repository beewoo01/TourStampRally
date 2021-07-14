package com.sdin.tourstamprally.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TouristSpot {

    @SerializedName("touristspot_idx")
    @Expose
    private int touristspot_idx;

    @SerializedName("touristspot_location_location_idx")
    @Expose
    private int touristspot_location_location_idx;

    @SerializedName("touristspot_name")
    @Expose
    private String touristspot_name;

    @SerializedName("touristspot_latitude")
    @Expose
    private String touristspot_latitude;

    @SerializedName("touristspot_longitude")
    @Expose
    private String touristspot_longitude;

    @SerializedName("touristspot_tag")
    @Expose
    private String touristspot_tag;

    @SerializedName("touristspot_checkin_count")
    @Expose
    private int touristspot_checkin_count;
}
