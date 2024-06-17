package com.altunoymak.esarj.util

import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

object Utils {
    fun haversine(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val R = 6371e3 // Dünya'nın yarıçapı (metre cinsinden)
        val phi1 = lat1 * PI / 180 // lat1'i radian cinsine çevir
        val phi2 = lat2 * PI / 180 // lat2'yi radian cinsine çevir
        val deltaPhi = (lat2 - lat1) * PI / 180
        val deltaLambda = (lon2 - lon1) * PI / 180

        val a = sin(deltaPhi / 2) * sin(deltaPhi / 2) +
                cos(phi1) * cos(phi2) *
                sin(deltaLambda / 2) * sin(deltaLambda / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))

        val distance = R * c // metre cinsinden mesafe
        return distance
    }
}