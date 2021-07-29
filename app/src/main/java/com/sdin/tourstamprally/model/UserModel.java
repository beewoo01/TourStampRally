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

    /*@SerializedName("historyidx")
    @Expose
    public Integer historyIdx;

    @SerializedName("adminidx")
    @Expose
    public int adminIdx;

    @SerializedName("adminid")
    @Expose
    public String adminId;

    @SerializedName("colorcode")
    @Expose
    public String colorCode;


    @SerializedName("userid")
    @Expose
    public String userId;

    @SerializedName("createdate")
    @Expose
    public String createDate;

    @SerializedName("history_date")
    @Expose
    public String historyDate;

    @SerializedName("updatedate")
    @Expose
    public String updateDate;*/

}
