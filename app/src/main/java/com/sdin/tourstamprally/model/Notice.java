package com.sdin.tourstamprally.model;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class Notice{

    @SerializedName("notice_idx")
    private int notice_idx;

    @SerializedName("notice_title")
    private String notice_title;

    @SerializedName("notice_content")
    private String notice_content;

    @SerializedName("notice_createtime")
    private String notice_createtime;

    @SerializedName("notice_updatetime")
    private String notice_updatetime;

    @SerializedName("notice_type")
    private int notice_type;

    public Notice(){

    }

    public Notice(int notice_idx, String notice_title, String notice_content, String notice_createtime, String notice_updatetime, int notice_type) {
        this.notice_idx = notice_idx;
        this.notice_title = notice_title;
        this.notice_content = notice_content;
        this.notice_createtime = notice_createtime;
        this.notice_updatetime = notice_updatetime;
        this.notice_type = notice_type;
    }

    public int getNotice_idx() {
        return notice_idx;
    }

    public void setNotice_idx(int notice_idx) {
        this.notice_idx = notice_idx;
    }

    public String getNotice_title() {
        return notice_title;
    }

    public void setNotice_title(String notice_title) {
        this.notice_title = notice_title;
    }

    public String getNotice_content() {
        return notice_content;
    }

    public void setNotice_content(String notice_content) {
        this.notice_content = notice_content;
    }

    public String getNotice_createtime() {
        return notice_createtime;
    }

    public void setNotice_createtime(String notice_createtime) {
        this.notice_createtime = notice_createtime;
    }

    public String getNotice_updatetime() {
        return notice_updatetime;
    }

    public void setNotice_updatetime(String notice_updatetime) {
        this.notice_updatetime = notice_updatetime;
    }

    public int getNotice_type() {
        return notice_type;
    }

    public void setNotice_type(int notice_type) {
        this.notice_type = notice_type;
    }
}
