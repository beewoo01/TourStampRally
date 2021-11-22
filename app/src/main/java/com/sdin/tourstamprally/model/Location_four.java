package com.sdin.tourstamprally.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class Location_four implements Comparable<Tour_Spot>, Parcelable {

    private int myInterCount;

    private int allSpotCount;
    //@SerializedName("popular")
    private int popular;

    //@SerializedName("location_idx")
    private int location_idx;

    //@SerializedName("location_name")
    private String location_name;

    //@SerializedName("location_img")
    private String location_img;

    //@SerializedName("myHistoryCount")
    private int myHistoryCount;

    //@SerializedName("allPointCount")
    private int allPointCount;

    //@SerializedName("touristspotpoint_tag")
    private String touristspotpoint_tag;

    private double touristspot_latitude;
    private double touristspot_longitude;


    public Location_four(){

    }
    public Location_four(int myInterCount, int allSpotCount, int popular, int location_idx, String location_name, String location_img,
                         int myHistoryCount, int allPointCount, String touristspotpoint_tag, double touristspot_latitude, double touristspot_longitude) {
        this.myInterCount = myInterCount;
        this.allSpotCount = allSpotCount;
        this.popular = popular;
        this.location_idx = location_idx;
        this.location_name = location_name;
        this.location_img = location_img;
        this.myHistoryCount = myHistoryCount;
        this.allPointCount = allPointCount;
        this.touristspotpoint_tag = touristspotpoint_tag;
        this.touristspot_latitude = touristspot_latitude;
        this.touristspot_longitude = touristspot_longitude;
    }

    public int getMyInterCount() {
        return myInterCount;
    }

    public void setMyInterCount(int myInterCount) {
        this.myInterCount = myInterCount;
    }

    public int getAllSpotCount() {
        return allSpotCount;
    }

    public void setAllSpotCount(int allSpotCount) {
        this.allSpotCount = allSpotCount;
    }

    public int getPopular() {
        return popular;
    }

    public void setPopular(int popular) {
        this.popular = popular;
    }

    public int getLocation_idx() {
        return location_idx;
    }

    public void setLocation_idx(int location_idx) {
        this.location_idx = location_idx;
    }

    public String getLocation_name() {
        return location_name;
    }

    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }

    public String getLocation_img() {
        return location_img;
    }

    public void setLocation_img(String location_img) {
        this.location_img = location_img;
    }

    public int getMyHistoryCount() {
        return myHistoryCount;
    }

    public void setMyHistoryCount(int myHistoryCount) {
        this.myHistoryCount = myHistoryCount;
    }

    public int getAllPointCount() {
        return allPointCount;
    }

    public void setAllPointCount(int allPointCount) {
        this.allPointCount = allPointCount;
    }

    public String getTouristspotpoint_tag() {
        return touristspotpoint_tag;
    }

    public void setTouristspotpoint_tag(String touristspotpoint_tag) {
        this.touristspotpoint_tag = touristspotpoint_tag;
    }

    public double getTouristspot_latitude() {
        return touristspot_latitude;
    }

    public void setTouristspot_latitude(double touristspot_latitude) {
        this.touristspot_latitude = touristspot_latitude;
    }

    public double getTouristspot_longitude() {
        return touristspot_longitude;
    }

    public void setTouristspot_longitude(double touristspot_longitude) {
        this.touristspot_longitude = touristspot_longitude;
    }

    protected Location_four(Parcel in) {
        myInterCount = in.readInt();
        allSpotCount = in.readInt();
        popular = in.readInt();
        location_idx = in.readInt();
        location_name = in.readString();
        location_img = in.readString();
        myHistoryCount = in.readInt();
        allPointCount = in.readInt();
        touristspotpoint_tag = in.readString();
        touristspot_latitude = in.readDouble();
        touristspot_longitude = in.readDouble();
    }

    public static final Creator<Location_four> CREATOR = new Creator<Location_four>() {
        @Override
        public Location_four createFromParcel(Parcel in) {
            return new Location_four(in);
        }

        @Override
        public Location_four[] newArray(int size) {
            return new Location_four[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(myInterCount);
        parcel.writeInt(allSpotCount);
        parcel.writeInt(popular);
        parcel.writeInt(location_idx);
        parcel.writeString(location_name);
        parcel.writeString(location_img);
        parcel.writeInt(myHistoryCount);
        parcel.writeInt(allPointCount);
        parcel.writeString(touristspotpoint_tag);
        parcel.writeDouble(touristspot_latitude);
        parcel.writeDouble(touristspot_longitude);
    }

    @Override
    public int compareTo(Tour_Spot tour_spot) {
        return 0;
    }
}
