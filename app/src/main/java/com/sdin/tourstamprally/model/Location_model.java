package com.sdin.tourstamprally.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Location_model {

    private String name;
    private String explan;
    private String imgUrl;
    private String createTime;
    private String upDataTime;

}
