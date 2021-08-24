package com.sdin.tourstamprally.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class VisitCountModel {

    @SerializedName("touristspotpoint_touristspot_idx")
    @Expose
    private String touristspotpoint_touristspot_idx;

    @SerializedName("allcount")
    @Expose
    private String allcount;

    @SerializedName("mycount")
    @Expose
    private String mycount;

}
