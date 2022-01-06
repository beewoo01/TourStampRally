package com.sdin.tourstamprally.v2

import com.sdin.tourstamprally.model.RallyMapDTO

interface ScanListener {
    fun moveSpotPoint(model : RallyMapDTO)
}