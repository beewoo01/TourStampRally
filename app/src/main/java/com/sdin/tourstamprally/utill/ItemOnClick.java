package com.sdin.tourstamprally.utill;

import com.sdin.tourstamprally.model.Tour_Spot;

public interface ItemOnClick {
    void onClick(int position);
    void ItemGuid(int position);
    void ItemGuid(int position, Tour_Spot model);
    void SetFragment(String tag);
    void onItemClick(Tour_Spot tour_spot);
}
