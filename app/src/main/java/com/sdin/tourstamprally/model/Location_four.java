package com.sdin.tourstamprally.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Location_four implements Comparable<Tour_Spot>, Parcelable {

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


    protected Location_four(Parcel in) {
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
