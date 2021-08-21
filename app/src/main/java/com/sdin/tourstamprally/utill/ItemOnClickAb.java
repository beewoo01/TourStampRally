package com.sdin.tourstamprally.utill;

import com.sdin.tourstamprally.model.Tour_Spot;
import com.sdin.tourstamprally.model.TouristSpotPoint;

public abstract class ItemOnClickAb implements ItemOnClick{

    @Override
    public void onClick(int potision) {

    }

    @Override
    public void ItemGuid(int position) {

    }

    @Override
    public void ItemGuidForPoint(Tour_Spot model){

    };

    @Override
    public void SetFragment(String tag){

    };

    @Override
    public void onItemClick(Tour_Spot tour_spot) {

    }

    @Override
    public void ItemGuidForDetail(TouristSpotPoint model) {
        
    }

}
