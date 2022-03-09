package com.sdin.tourstamprally.utill.listener;

import com.sdin.tourstamprally.model.Location_four;
import com.sdin.tourstamprally.model.RallyMapDTO;
import com.sdin.tourstamprally.model.ReviewWriter;
import com.sdin.tourstamprally.model.TouristSpotPoint;

import java.util.ArrayList;

public interface ItemOnClick {
    void onClick(int position);
    void ItemGuid(int position);
    void ItemGuidForPoint(RallyMapDTO model);
    void ItemGuidForDetail(TouristSpotPoint model);
    void SetFragment(ArrayList<Location_four> location_fours);
    //void onItemClick(Tour_Spot tour_spot);
    void onItemClick(Location_four location_four);
    /*void onWriteRewviewClick(int spotIdx, String spotName, float review_score, String review_contents);*/
    void onWriteRewviewClick(ReviewWriter reviewWriter);
    void onWriteReviewSuccess();

    void reviewMoreClick();
    void reviewItemClick(int review_idx, String spot_name);

    //void signOutListener();
}
