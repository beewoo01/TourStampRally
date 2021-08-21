package com.sdin.tourstamprally.utill;

import com.sdin.tourstamprally.model.Tour_Spot;
import com.sdin.tourstamprally.model.TouristSpotPoint;

public interface ItemOnClick {
    void onClick(int position);
    void ItemGuid(int position);
    void ItemGuidForPoint(Tour_Spot model);
    void ItemGuidForDetail(TouristSpotPoint model);
    void SetFragment(String tag);
    void onItemClick(Tour_Spot tour_spot);
}
