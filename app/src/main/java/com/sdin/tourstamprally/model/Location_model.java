package com.sdin.tourstamprally.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class Location_model {

    private String name;
    private String explan;
    private String imgUrl;
    private String createTime;
    private String upDataTime;

    public Location_model(){

    }

    public Location_model(String name, String explan, String imgUrl, String createTime, String upDataTime) {
        this.name = name;
        this.explan = explan;
        this.imgUrl = imgUrl;
        this.createTime = createTime;
        this.upDataTime = upDataTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExplan() {
        return explan;
    }

    public void setExplan(String explan) {
        this.explan = explan;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpDataTime() {
        return upDataTime;
    }

    public void setUpDataTime(String upDataTime) {
        this.upDataTime = upDataTime;
    }
}
