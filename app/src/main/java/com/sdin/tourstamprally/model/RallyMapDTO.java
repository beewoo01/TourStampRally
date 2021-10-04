package com.sdin.tourstamprally.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RallyMapDTO implements Serializable {
    @SerializedName("touristspot_idx")
    private int touristspot_idx;
    @SerializedName("touristspot_name")
    private String touristspot_name;
    @SerializedName("touristspotpoint_idx")
    private int touristspotpoint_idx;
    @SerializedName("touristspot_latitude")
    private double touristspot_latitude;
    @SerializedName("touristspot_longitude")
    private double touristspot_longitude;
    @SerializedName("touristspot_address")
    private String touristspot_address;
    @SerializedName("allCount")
    private int allCount;
    @SerializedName("myCount")
    private int myCount;
    @SerializedName("touristspotpoint_name")
    private String touristspotpoint_name;
    @SerializedName("touristspot_img")
    private String touristspot_img;


}
