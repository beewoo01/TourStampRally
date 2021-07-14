package com.sdin.tourstamprally.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TouristSpotPoint{

    @SerializedName("touristspotpoint_idx")
    @Expose
    private int touristspotpoint_idx;

    @SerializedName("touristspotpoint_touristspot_idx")
    @Expose
    private int touristspotpoint_touristspot_idx;

    @SerializedName("touristspotpoint_latitude")
    @Expose
    private String touristspotpoint_latitude;

    @SerializedName("touristspotpoint_longitude")
    @Expose
    private String touristspotpoint_longitude;

}
