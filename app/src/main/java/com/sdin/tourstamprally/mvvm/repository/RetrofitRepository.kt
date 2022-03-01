package com.sdin.tourstamprally.mvvm.repository

import com.sdin.tourstamprally.model.StoreSubDTO

interface RetrofitRepository {
    suspend fun getStoreSub() : StoreSubDTO

}