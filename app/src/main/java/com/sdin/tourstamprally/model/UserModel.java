package com.sdin.tourstamprally.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class UserModel implements Serializable {

    @SerializedName("user_idx")
    @Expose
    private int userIdx;

    @SerializedName("user_phone")
    @Expose
    private String phone;

    @SerializedName("user_name")
    @Expose
    private String name;

    @SerializedName("user_password")
    @Expose
    private String password;

    @SerializedName("user_email")
    @Expose
    private String email;

    @SerializedName("user_location")
    @Expose
    private String location;

    @SerializedName("user_profile")
    @Expose
    private String user_profile;

    @SerializedName("agree2")
    @Expose
    private boolean agree2;

    @SerializedName("agree1")
    @Expose
    private boolean agree1;

    @SerializedName("user_enable")
    @Expose
    private String enable;

    public UserModel(){

    }

    public UserModel(int userIdx, String phone, String name, String password, String email, String location, String user_profile) {
        this.userIdx = userIdx;
        this.phone = phone;
        this.name = name;
        this.password = password;
        this.email = email;
        this.location = location;
        this.user_profile = user_profile;
    }

    public UserModel(int userIdx, String phone, String name, String email, String location, String user_profile) {
        this.userIdx = userIdx;
        this.phone = phone;
        this.name = name;
        this.email = email;
        this.location = location;
        this.user_profile = user_profile;
    }

    public UserModel(int userIdx, String phone, String name, String password, String email, String location, String user_profile, boolean agree2, boolean agree1, String enable) {
        this.userIdx = userIdx;
        this.phone = phone;
        this.name = name;
        this.password = password;
        this.email = email;
        this.location = location;
        this.user_profile = user_profile;
        this.agree2 = agree2;
        this.agree1 = agree1;
        this.enable = enable;
    }

    public int getUserIdx() {
        return userIdx;
    }

    public void setUserIdx(int userIdx) {
        this.userIdx = userIdx;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUser_profile() {
        return user_profile;
    }

    public void setUser_profile(String user_profile) {
        this.user_profile = user_profile;
    }

    public boolean isAgree2() {
        return agree2;
    }

    public void setAgree2(boolean agree2) {
        this.agree2 = agree2;
    }

    public boolean isAgree1() {
        return agree1;
    }

    public void setAgree1(boolean agree1) {
        this.agree1 = agree1;
    }

    public String getEnable() {
        return enable;
    }

    public void setEnable(String enable) {
        this.enable = enable;
    }

}
