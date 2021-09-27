package com.sdin.tourstamprally.utill;

import com.sdin.tourstamprally.model.AllReviewDTO;
import com.sdin.tourstamprally.model.Location_four;
import com.sdin.tourstamprally.model.Tour_Spot;
import com.sdin.tourstamprally.model.TouristSpotPoint;

import java.util.ArrayList;

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

    /*@Override
    public void SetFragment(String tag){

    }*/

    @Override
    public void SetFragment(ArrayList<Location_four> location_four) {

    }

    @Override
    public void onItemClick(Location_four location_four) {

    }

    /*@Override
    public void onItemClick(Tour_Spot tour_spot) {

    }*/

    @Override
    public void ItemGuidForDetail(TouristSpotPoint model) {
        
    }


    @Override
    public void onWriteRewviewClick(int spotIdx, String spotName) {

    }

    @Override
    public void onWriteReviewSuccess() {

    }

    @Override
    public void reviewMoreClick() { }

    @Override
    public void reviewItemClick(AllReviewDTO allReviewDTO) { }
}
