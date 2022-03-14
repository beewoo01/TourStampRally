package com.sdin.tourstamprally.utill.listener

import com.sdin.tourstamprally.model.HistorySpotModel

interface VisitItemClickListener {
    fun deapClick(position : Int, model : HistorySpotModel)
    fun deleteReview(reviewIdx : Int, position : Int)
    fun clearClick(model : HistorySpotModel, position: Int)
}