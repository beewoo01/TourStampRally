package com.sdin.tourstamprally.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HashTagModel implements Serializable {

    @SerializedName("touristspot_location_location_idx")
    private int touristspot_location_location_idx;

    @SerializedName("touristspot_idx")
    private int touristspot_idx;

    private String touristspot_tag;

}
