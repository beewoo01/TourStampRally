package com.sdin.tourstamprally.model;

import java.io.Serializable;


public class TourTagModel implements Serializable {
    private String hashTag;
    private int location_idx;

    public TourTagModel(){

    }
    public TourTagModel(String hashTag, int location_idx) {
        this.hashTag = hashTag;
        this.location_idx = location_idx;
    }

    public String getHashTag() {
        return hashTag;
    }

    public void setHashTag(String hashTag) {
        this.hashTag = hashTag;
    }

    public int getLocation_idx() {
        return location_idx;
    }

    public void setLocation_idx(int location_idx) {
        this.location_idx = location_idx;
    }
}
