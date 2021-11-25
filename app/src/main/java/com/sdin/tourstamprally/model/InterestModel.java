package com.sdin.tourstamprally.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class InterestModel {
    private int inter_idx;
    private int ts_idx;
    private int location_idx;

    private String ts_name;
    private String ts_explan;
    private String ts_tag;

    private int ts_type;
    private String ts_img;
    private String location_name;


    public InterestModel() {

    }

    public InterestModel(int inter_idx, int ts_idx, int location_idx, String ts_name,
                         String ts_explan, String ts_tag, int ts_type, String ts_img, String location_name) {
        this.inter_idx = inter_idx;
        this.ts_idx = ts_idx;
        this.location_idx = location_idx;
        this.ts_name = ts_name;
        this.ts_explan = ts_explan;
        this.ts_tag = ts_tag;
        this.ts_type = ts_type;
        this.ts_img = ts_img;
        this.location_name = location_name;
    }

    public int getInter_idx() {
        return inter_idx;
    }

    public void setInter_idx(int inter_idx) {
        this.inter_idx = inter_idx;
    }

    public int getTs_idx() {
        return ts_idx;
    }

    public void setTs_idx(int ts_idx) {
        this.ts_idx = ts_idx;
    }

    public int getLocation_idx() {
        return location_idx;
    }

    public void setLocation_idx(int location_idx) {
        this.location_idx = location_idx;
    }

    public String getTs_name() {
        return ts_name;
    }

    public void setTs_name(String ts_name) {
        this.ts_name = ts_name;
    }

    public String getTs_explan() {
        return ts_explan;
    }

    public void setTs_explan(String ts_explan) {
        this.ts_explan = ts_explan;
    }

    public String getTs_tag() {
        return ts_tag;
    }

    public void setTs_tag(String ts_tag) {
        this.ts_tag = ts_tag;
    }

    public int getTs_type() {
        return ts_type;
    }

    public void setTs_type(int ts_type) {
        this.ts_type = ts_type;
    }

    public String getTs_img() {
        return ts_img;
    }

    public void setTs_img(String ts_img) {
        this.ts_img = ts_img;
    }

    public String getLocation_name() {
        return location_name;
    }

    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }
}
