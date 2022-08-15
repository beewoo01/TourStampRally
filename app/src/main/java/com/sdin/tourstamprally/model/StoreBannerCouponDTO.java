package com.sdin.tourstamprally.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class StoreBannerCouponDTO implements Serializable, Parcelable {
    @SerializedName("store_coupon_idx")
    private int store_coupon_idx;

    @SerializedName("store_coupon_store_idx")
    private int store_coupon_store_idx;

    @SerializedName("store_coupon_name")
    private String store_coupon_name;

    @SerializedName("store_coupon_number")
    private String store_coupon_number;

    @SerializedName("store_coupon_expiration_startDate")
    private String store_coupon_expiration_startDate;

    @SerializedName("store_coupon_expiration_endDate")
    private String store_coupon_expiration_endDate;

    @SerializedName("store_coupon_enabled")
    private int store_coupon_enabled;

    @SerializedName("store_coupon_isSpecial")
    private int store_coupon_isSpecial;

    @SerializedName("store_coupon_banner_img")
    private String store_coupon_banner_img;

    @SerializedName("store_coupon_createtime")
    private String store_coupon_createtime;

    @SerializedName("store_coupon_updatetime")
    private String store_coupon_updatetime;

    @SerializedName("store_name")
    private String store_name;

    @SerializedName("store_logo_icon")
    private String store_logo_icon;

    protected StoreBannerCouponDTO(Parcel in) {
        store_coupon_idx = in.readInt();
        store_coupon_store_idx = in.readInt();
        store_coupon_name = in.readString();
        store_coupon_number = in.readString();
        store_coupon_expiration_startDate = in.readString();
        store_coupon_expiration_endDate = in.readString();
        store_coupon_enabled = in.readInt();
        store_coupon_isSpecial = in.readInt();
        store_coupon_banner_img = in.readString();
        store_coupon_createtime = in.readString();
        store_coupon_updatetime = in.readString();
        store_name = in.readString();
        store_logo_icon = in.readString();
    }

    public static final Creator<StoreBannerCouponDTO> CREATOR = new Creator<StoreBannerCouponDTO>() {
        @Override
        public StoreBannerCouponDTO createFromParcel(Parcel in) {
            return new StoreBannerCouponDTO(in);
        }

        @Override
        public StoreBannerCouponDTO[] newArray(int size) {
            return new StoreBannerCouponDTO[size];
        }
    };

    public int getStore_coupon_idx() {
        return store_coupon_idx;
    }

    public void setStore_coupon_idx(int store_coupon_idx) {
        this.store_coupon_idx = store_coupon_idx;
    }

    public int getStore_coupon_store_idx() {
        return store_coupon_store_idx;
    }

    public void setStore_coupon_store_idx(int store_coupon_store_idx) {
        this.store_coupon_store_idx = store_coupon_store_idx;
    }

    public String getStore_coupon_name() {
        return store_coupon_name;
    }

    public void setStore_coupon_name(String store_coupon_name) {
        this.store_coupon_name = store_coupon_name;
    }

    public String getStore_coupon_number() {
        return store_coupon_number;
    }

    public void setStore_coupon_number(String store_coupon_number) {
        this.store_coupon_number = store_coupon_number;
    }

    public String getStore_coupon_expiration_startDate() {
        return store_coupon_expiration_startDate;
    }

    public void setStore_coupon_expiration_startDate(String store_coupon_expiration_startDate) {
        this.store_coupon_expiration_startDate = store_coupon_expiration_startDate;
    }

    public String getStore_coupon_expiration_endDate() {
        return store_coupon_expiration_endDate;
    }

    public void setStore_coupon_expiration_endDate(String store_coupon_expiration_endDate) {
        this.store_coupon_expiration_endDate = store_coupon_expiration_endDate;
    }

    public int getStore_coupon_enabled() {
        return store_coupon_enabled;
    }

    public void setStore_coupon_enabled(int store_coupon_enabled) {
        this.store_coupon_enabled = store_coupon_enabled;
    }

    public int getStore_coupon_isSpecial() {
        return store_coupon_isSpecial;
    }

    public void setStore_coupon_isSpecial(int store_coupon_isSpecial) {
        this.store_coupon_isSpecial = store_coupon_isSpecial;
    }

    public String getStore_coupon_banner_img() {
        return store_coupon_banner_img;
    }

    public void setStore_coupon_banner_img(String store_coupon_banner_img) {
        this.store_coupon_banner_img = store_coupon_banner_img;
    }

    public String getStore_coupon_createtime() {
        return store_coupon_createtime;
    }

    public void setStore_coupon_createtime(String store_coupon_createtime) {
        this.store_coupon_createtime = store_coupon_createtime;
    }

    public String getStore_coupon_updatetime() {
        return store_coupon_updatetime;
    }

    public void setStore_coupon_updatetime(String store_coupon_updatetime) {
        this.store_coupon_updatetime = store_coupon_updatetime;
    }

    public String getStore_name() {
        return store_name;
    }

    public void setStore_name(String store_name) {
        this.store_name = store_name;
    }

    public String getStore_logo_icon() {
        return store_logo_icon;
    }

    public void setStore_logo_icon(String store_logo_icon) {
        this.store_logo_icon = store_logo_icon;
    }

    public StoreBannerCouponDTO() {
    }

    public StoreBannerCouponDTO(int store_coupon_idx, int store_coupon_store_idx, String store_coupon_name, String store_coupon_number, String store_coupon_expiration_startDate, String store_coupon_expiration_endDate, int store_coupon_enabled, int store_coupon_isSpecial, String store_coupon_banner_img, String store_coupon_createtime, String store_coupon_updatetime, String store_name, String store_logo_icon) {
        this.store_coupon_idx = store_coupon_idx;
        this.store_coupon_store_idx = store_coupon_store_idx;
        this.store_coupon_name = store_coupon_name;
        this.store_coupon_number = store_coupon_number;
        this.store_coupon_expiration_startDate = store_coupon_expiration_startDate;
        this.store_coupon_expiration_endDate = store_coupon_expiration_endDate;
        this.store_coupon_enabled = store_coupon_enabled;
        this.store_coupon_isSpecial = store_coupon_isSpecial;
        this.store_coupon_banner_img = store_coupon_banner_img;
        this.store_coupon_createtime = store_coupon_createtime;
        this.store_coupon_updatetime = store_coupon_updatetime;
        this.store_name = store_name;
        this.store_logo_icon = store_logo_icon;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(store_coupon_idx);
        parcel.writeInt(store_coupon_store_idx);
        parcel.writeString(store_coupon_name);
        parcel.writeString(store_coupon_number);
        parcel.writeString(store_coupon_expiration_startDate);
        parcel.writeString(store_coupon_expiration_endDate);
        parcel.writeInt(store_coupon_enabled);
        parcel.writeInt(store_coupon_isSpecial);
        parcel.writeString(store_coupon_banner_img);
        parcel.writeString(store_coupon_createtime);
        parcel.writeString(store_coupon_updatetime);
        parcel.writeString(store_name);
        parcel.writeString(store_logo_icon);
    }
}
