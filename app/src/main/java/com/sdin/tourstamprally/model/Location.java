package com.sdin.tourstamprally.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Location {
    private int location_idx;
    private String location_name;
    private String location_img;
    private String location_createtime;
    private String locatio_updatetime;

}
