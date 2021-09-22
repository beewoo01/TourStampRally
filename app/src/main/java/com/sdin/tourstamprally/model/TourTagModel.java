package com.sdin.tourstamprally.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class TourTagModel implements Serializable {
    private String hashTag;
    private int location_idx;
}
