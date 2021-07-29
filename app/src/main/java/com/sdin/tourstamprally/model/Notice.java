package com.sdin.tourstamprally.model;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
}
