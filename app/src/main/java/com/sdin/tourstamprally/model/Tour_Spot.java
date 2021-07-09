package com.sdin.tourstamprally.model;

import android.security.identity.EphemeralPublicKeyNotFoundException;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Tour_Spot implements Comparable<Tour_Spot>{

    private int spotIdx;
    private int location_idx;
    private String spotName;
    private String spotLatitude;
    private String spotlongitude;
    private String spotUserIdx;
    private String spotTag;
    private String imgUrl;
    private boolean isClear;
    private String createTime;
    private String updateTime;
    private String clearTime;

    public Tour_Spot(int spotIdx, int location_idx, String spotName, String spotLatitude, String spotlongitude, String spotTag, String imgUrl, boolean isClear, String createTime, String updateTime) {
        this.spotIdx = spotIdx;
        this.location_idx = location_idx;
        this.spotName = spotName;
        this.spotLatitude = spotLatitude;
        this.spotlongitude = spotlongitude;
        this.spotTag = spotTag;
        this.imgUrl = imgUrl;
        this.isClear = isClear;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public Tour_Spot(int spotIdx, int location_idx, String spotName, String spotLatitude, String spotlongitude, String spotTag, String imgUrl, boolean isClear) {
        this.spotIdx = spotIdx;
        this.location_idx = location_idx;
        this.spotName = spotName;
        this.spotLatitude = spotLatitude;
        this.spotlongitude = spotlongitude;
        this.spotTag = spotTag;
        this.imgUrl = imgUrl;
        this.isClear = isClear;
    }

    @Override
    public int compareTo(Tour_Spot o) {
        return 0;
        //SELECT a.location_name, b.touristspot_name , b.touristspot_latitude, b.touristspot_longitude
        // FROM bsr.location AS a
        // JOIN bsr.touristspot AS b
        // ON a.location_idx = b.touristspot_location_location_idx
    }


}
