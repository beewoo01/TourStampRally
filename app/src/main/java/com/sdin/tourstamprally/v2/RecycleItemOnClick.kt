package com.sdin.tourstamprally.v2

interface RecycleItemOnClick<T> {
    fun onItemClickListener(model : T, position : Int)
}