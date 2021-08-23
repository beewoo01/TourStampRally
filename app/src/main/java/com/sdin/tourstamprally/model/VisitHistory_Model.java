package com.sdin.tourstamprally.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class VisitHistory_Model {
    private int spot_idx;
    private String spot_name;
    private String spot_explan;
    private String date;

}
