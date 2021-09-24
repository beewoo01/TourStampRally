package com.sdin.tourstamprally.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
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
}
