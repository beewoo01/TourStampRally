package com.sdin.tourstamprally.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.security.identity.EphemeralPublicKeyNotFoundException;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Tour_Spot implements Comparable<Tour_Spot>, Serializable {

    @SerializedName("location_idx")
    private int location_idx;

    @SerializedName("location_name")
    private String location_name;

    @SerializedName("location_img")
    private String location_img;

    @SerializedName("location_createtime")
    private String location_createtime;

    @SerializedName("locatio_updatetime")
    private String locatio_updatetime;


    @SerializedName("touristspot_idx")
    private int touristspot_idx;

    @SerializedName("touristspot_location_location_idx")
    private int touristspot_location_location_idx;

    @SerializedName("touristspot_name")
    private String touristspot_name;

    @SerializedName("touristspot_explan")
    private String touristspot_explan;

    @SerializedName("touristspot_latitude")
    private double touristspot_latitude;

    @SerializedName("touristspot_longitude")
    private double touristspot_longitude;

    @SerializedName("touristspot_img")
    private String touristspot_img;

    @SerializedName("touristspot_tag")
    private String touristspot_tag;

    @SerializedName("touristspot_checkin_count")
    private int touristspot_checkin_count;

    @SerializedName("touristspot_createtime")
    private long touristspot_createtime;

    @SerializedName("touristspot_updatetime")
    private long touristspot_updatetime;


    @SerializedName("touristspotpoint_idx")
    private String touristspotpoint_idx;

    @SerializedName("touristspotpoint_touristspot_idx")
    private String touristspotpoint_touristspot_idx;

    @SerializedName("touristspotpoint_name")
    private String touristspotpoint_name;

    @SerializedName("touristspotpoint_explan")
    private String touristspotpoint_explan;

    @SerializedName("touristspotpoint_latitude")
    private String touristspotpoint_latitude;

    @SerializedName("touristspotpoint_longitude")
    private String touristspotpoint_longitude;


    @SerializedName("touristhistory_idx")
    private String touristhistory_idx;

    @SerializedName("touristhistory_touristspotpoint_idx")
    private String touristhistory_touristspotpoint_idx;

    @SerializedName("touristhistory_user_idx")
    private String touristhistory_user_idx;

    @SerializedName("touristhistory_createtime")
    private long touristhistory_createtime;


    @SerializedName("touristhistory_updatetime")
    private long touristhistory_updatetime;

    private int location_percentage;

    private int tourspot_percentage;

    private int location_clearPercent;

    private int tourspot_clearPercent;

    private boolean clear;



    @SerializedName("user_touristspot_interest_idx")
    private int user_touristspot_interest_idx;

    @SerializedName("interest_user_idx")
    private int interest_user_idx;

    @SerializedName("interest_touristspot_idx")
    private int interest_touristspot_idx;

    @SerializedName("interest_createtime")
    private String interest_createtime;

    @SerializedName("interest_updatetime")
    private String interest_updatetime;




    /*public static final Creator<Item2> CREATOR = new Creator<Item2>() {
        @Override
        public Item2 createFromParcel(Parcel in) {
            return new Item2(in);
        }

        @Override
        public Item2[] newArray(int size) {
            return new Item2[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(index);
        dest.writeString(address);
        dest.writeString(title);
    }*/


    @Override
    public int compareTo(Tour_Spot o) {
        return 0;
    }


}
