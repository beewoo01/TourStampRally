package com.sdin.tourstamprally.mvvm.viewmodel

import androidx.lifecycle.*
import com.sdin.tourstamprally.model.StoreModel
import kotlinx.coroutines.launch

class StoreDetailViewModel : ViewModel() {
    private val _storeDetailItem: MutableLiveData<StoreModel> = MutableLiveData()

    val storeDetailItem: LiveData<StoreModel>
        get() = _storeDetailItem


    init {

    }

    fun getStoreSubData() {
        viewModelScope.launch {

        }
    }

}