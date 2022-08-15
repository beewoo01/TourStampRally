package com.sdin.tourstamprally.utill

import kotlin.math.acos
import kotlin.math.cos
import kotlin.math.round
import kotlin.math.sin

object LocationUtil {

    fun calcDistance(lat1 : Double, lon1: Double, lat2 : Double, lon2 : Double): String {

        if (lat1 == 0.0 || lon1 == 0.0 || lat2 == 0.0 || lon2 == 0.0) {
            return "0 m"
        }

        val EARTH_R = 6371000.0
        val Rad : Double = Math.PI/180
        val radLat1 : Double = Rad * lat1
        val radLat2 : Double = Rad * lat2
        val radDist = Rad * (lon1 - lon2)

        val distance = (sin(radLat1) * sin(radLat2)) + (cos(radLat1) * cos(radLat2) * cos(radDist))
        val ret = EARTH_R * acos(distance)
        val rslt : Double = round(round(ret) / 1000)
        var result = "${rslt}km"
        if (rslt == 0.0) result = "${round(ret)} m"

        return result
    }

}