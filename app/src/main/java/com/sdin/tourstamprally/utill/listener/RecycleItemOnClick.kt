package com.sdin.tourstamprally.utill.listener

interface RecycleItemOnClick<T> {
    fun onItemClickListener(model : T, position : Int)
}