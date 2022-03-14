package com.sdin.tourstamprally.utill.listener.observe

interface Subject<T> {
    fun attach(observer: Observer<T>)
    fun detach(observer: Observer<T>)
    fun notifyUpdate(massage : T, position: Int)
    fun notifyTwiceUpdate(massage1 : T, massage2 : T, position: Int)
}