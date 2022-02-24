package com.sdin.tourstamprally.utill.listener

import com.sdin.tourstamprally.model.ReviewWriter

interface ReviewListener {
    fun onWriteReviewClick(model : ReviewWriter)
}