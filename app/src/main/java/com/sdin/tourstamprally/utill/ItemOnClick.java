package com.sdin.tourstamprally.utill;

import com.sdin.tourstamprally.model.AllReviewDTO;
import com.sdin.tourstamprally.model.Location_four;
import com.sdin.tourstamprally.model.Tour_Spot;
import com.sdin.tourstamprally.model.TouristSpotPoint;

import java.util.ArrayList;

public interface ItemOnClick {
    void onClick(int position);
    void ItemGuid(int position);
    void ItemGuidForPoint(Tour_Spot model);
    void ItemGuidForDetail(TouristSpotPoint model);
    void SetFragment(ArrayList<Location_four> location_fours);
    //void onItemClick(Tour_Spot tour_spot);
    void onItemClick(Location_four location_four);
    void onWriteRewviewClick(int spotIdx, String spotName);
    void onWriteReviewSuccess();

    void reviewMoreClick();
    void reviewItemClick(AllReviewDTO allReviewDTO);
}
