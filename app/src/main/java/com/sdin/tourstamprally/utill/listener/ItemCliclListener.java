package com.sdin.tourstamprally.utill.listener;

import com.sdin.tourstamprally.model.history_spotModel2;

public interface ItemCliclListener {
    void deapsClick(int position, history_spotModel2 model);
    void delReview(int review_idx, int position);
    void clearClick(int touristspot_idx);
}
