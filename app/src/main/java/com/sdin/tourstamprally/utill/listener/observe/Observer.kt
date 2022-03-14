package com.sdin.tourstamprally.utill.listener.observe

interface Observer<T> {
    fun update1(message : T)
    fun update2(message1 : T, message2 : T)
}