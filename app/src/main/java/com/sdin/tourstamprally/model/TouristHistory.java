package com.sdin.tourstamprally.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TouristHistory implements Serializable {

    @SerializedName("touristhistory_idx")
    @Expose
    private int touristhistory_idx;

    @SerializedName("touristhistory_touristspotpoint_idx")
    @Expose
    private int touristhistory_touristspotpoint_idx;

    @SerializedName("touristhistory_user_idx")
    @Expose
    private int touristhistory_user_idx;

    @SerializedName("touristhistory_createtime")
    @Expose
    private int touristhistory_createtime;

    @SerializedName("touristhistory_updatetime")
    @Expose
    private int touristhistory_updatetime;

}
