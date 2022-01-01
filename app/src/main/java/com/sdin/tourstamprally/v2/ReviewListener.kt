package com.sdin.tourstamprally.v2

import com.sdin.tourstamprally.model.ReviewWriter

interface ReviewListener {
    fun onWriteReviewClick(model : ReviewWriter)
}