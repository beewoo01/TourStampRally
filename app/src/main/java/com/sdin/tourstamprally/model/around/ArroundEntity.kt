package com.sdin.tourstamprally.model.around

import com.google.gson.annotations.SerializedName

data class ArroundEntity(
    val location : MutableList<LocationEntity>,
    val store : MutableList<StoreEntity>
)

data class LocationEntity(
    @SerializedName("location_idx")
    val location_idx : Int,
    @SerializedName("location_name")
    val location_name : String
)

data class StoreEntity(
    val storeType : Int,
    val storeName : String
)

data class AllAroundEntity(
    @SerializedName("location_idx")
    val location_idx : Int,
    @SerializedName("location_name")
    val location_name : String?
    /*,
    val storeType : Int?,
    val storeName : String?,
    val viewType : Int = 0*/
    //0 -> Location 1 -> Stroe
)