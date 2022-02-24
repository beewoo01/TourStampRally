package com.sdin.tourstamprally.utill.listener

import com.sdin.tourstamprally.model.RallyMapDTO

interface ScanListener {
    fun moveSpotPoint(model : RallyMapDTO)
}