package com.sdin.tourstamprally.model;

import android.security.identity.EphemeralPublicKeyNotFoundException;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Tour_Spot {

    public int spotIdx;
    public int location_idx;
    public String spotName;
    public String spotLatitude;
    public String spotlongitude;
    public String spotUserIdx;
    public String spotTag;
    public String imgUrl;
    public boolean isClear;
    public String createTime;
    public String updateTime;

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
}
