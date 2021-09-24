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
    private String location_updatetime;

    public Location(int location_idx, String location_name){
        this.location_idx = location_idx;
        this.location_name = location_name;
    }
}
