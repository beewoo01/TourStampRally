package com.sdin.tourstamprally.utill.listener

interface Observable {
    fun registerObserver(observer: Observer) // attach, subscribe
    fun unregisterObserver(observer: Observer) // detach, unsubscribe
    fun notifyObservers(search: String)
}