package com.altunoymak.esarj.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import androidx.core.content.ContextCompat
import com.altunoymak.esarj.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer

class CustomClusterRenderer(
    private val context: Context,
    map: GoogleMap,
    clusterManager: ClusterManager<ClusterItem>
) : DefaultClusterRenderer<ClusterItem>(context, map, clusterManager) {

    override fun shouldRenderAsCluster(cluster: Cluster<ClusterItem>): Boolean {
        return cluster.size > 10
    }

    private fun createCustomMarker(
        resourceId: Int,
        width: Int,
        height: Int,
        color: Int
    ): BitmapDescriptor {
        val markerBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(markerBitmap)
        val paint = Paint()
        paint.isAntiAlias = true

        paint.color = color
        canvas.drawCircle(width / 2f, height / 2f, width / 2f, paint)

        paint.color = ContextCompat.getColor(context, android.R.color.white)
        canvas.drawCircle(width / 2f, height / 2f, (width / 2f) - 10, paint)

        val drawable = ContextCompat.getDrawable(context, resourceId)
        val iconSize = width / 2
        val left = (width - iconSize) / 2
        val top = (height - iconSize) / 2
        drawable?.setBounds(left, top, left + iconSize, top + iconSize)
        drawable?.draw(canvas)

        return BitmapDescriptorFactory.fromBitmap(markerBitmap)
    }

    override fun onBeforeClusterItemRendered(item: ClusterItem, markerOptions: MarkerOptions) {
        val icon: BitmapDescriptor
        when (item.title) {
            "BEEFULL" -> {
                icon = createCustomMarker(
                    R.drawable.beeful_ic,
                    125,
                    125,
                    ContextCompat.getColor(context, R.color.orange)
                )
            }

            "ESARJ" -> {
                icon = createCustomMarker(
                    R.drawable.esarj_ic,
                    125,
                    125,
                    ContextCompat.getColor(context, R.color.blue)
                )
            }

            "AKSAENERGY" -> {
                icon = createCustomMarker(
                    R.drawable.aksa_ic,
                    125,
                    125,
                    ContextCompat.getColor(context, R.color.green)
                )
            }

            "ZES" -> {
                icon = createCustomMarker(
                    R.drawable.zes_ic,
                    125,
                    125,
                    ContextCompat.getColor(context, R.color.red)
                )
            }

            else -> {
                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)
            }
        }
        markerOptions.icon(icon)
    }

    override fun onClusterItemRendered(clusterItem: ClusterItem, marker: Marker) {
        super.onClusterItemRendered(clusterItem, marker)
        marker.tag = clusterItem.id
    }
}
