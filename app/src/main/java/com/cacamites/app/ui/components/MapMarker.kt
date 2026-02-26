package com.cacamites.app.ui.components

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.cacamites.app.R
import com.cacamites.app.model.Point
import com.cacamites.app.model.PointState
import com.cacamites.app.model.PointType
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polygon

fun addMarkersToMap(
    mapView: MapView,
    points: List<Point>,
    onPointClick: (Point) -> Unit
) {
    val context = mapView.context
    // Clear existing overlays but keep User Location
    mapView.overlays.removeAll { it !is org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay }

    points.forEach { point ->
        // Si el punt és invisible per disseny, no el dibuixem mai al mapa
        if (point.isAlwaysInvisible) return@forEach
        
        if (point.state != PointState.LOCKED) {
            val isStart = point.id.endsWith("_start")
            
            // 1. Draw Interaction Circle
            val circle = Polygon().apply {
                pointsAsCircle(point.coordinate, point.interactionRadius)
                
                val baseColor = if (isStart) {
                    android.graphics.Color.parseColor("#F17002") // Taronja per a l'inici
                } else {
                    android.graphics.Color.GREEN
                }
                
                fillColor = android.graphics.Color.argb(40, android.graphics.Color.red(baseColor), android.graphics.Color.green(baseColor), android.graphics.Color.blue(baseColor))
                strokeColor = baseColor
                strokeWidth = 2.0f
            }
            mapView.overlays.add(circle)

            // 2. Draw Marker
            val marker = Marker(mapView)
            marker.position = point.coordinate
            marker.title = point.title
            marker.id = point.id
            
            // Icona segons estat
            if (point.state == PointState.COMPLETED) {
                marker.icon = ContextCompat.getDrawable(context, android.R.drawable.checkbox_on_background)
            } else {
                if (isStart) {
                    marker.icon = createFlagMarker(context, "#F17002")
                } else {
                    val drawable = ContextCompat.getDrawable(context, org.osmdroid.library.R.drawable.marker_default)
                    drawable?.setTint(android.graphics.Color.RED)
                    marker.icon = drawable
                }
            }
            
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            
            marker.setOnMarkerClickListener { _, _ ->
                onPointClick(point)
                true
            }
            
            mapView.overlays.add(marker)
        }
    }
    
    mapView.invalidate()
}

private fun Polygon.pointsAsCircle(center: org.osmdroid.util.GeoPoint, radiusInMeters: Double) {
    val points = mutableListOf<org.osmdroid.util.GeoPoint>()
    for (i in 0 until 360 step 10) {
        points.add(center.destinationPoint(radiusInMeters, i.toDouble()))
    }
    this.points = points
}

private fun createFlagMarker(context: Context, hexColor: String): Drawable {
    val width = 120
    val height = 160 
    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    paint.color = android.graphics.Color.parseColor(hexColor)
    
    val centerX = width / 2f
    val radius = width / 2f
    canvas.drawCircle(centerX, radius, radius, paint)
    
    val path = android.graphics.Path()
    path.moveTo(centerX - radius * 0.85f, radius * 0.85f) 
    path.lineTo(centerX, height.toFloat()) 
    path.lineTo(centerX + radius * 0.85f, radius * 0.85f)
    path.close()
    canvas.drawPath(path, paint)

    paint.color = android.graphics.Color.WHITE
    val iconSize = width * 0.5f
    val left = centerX - iconSize / 2f
    val top = radius - iconSize / 2f
    
    canvas.drawRect(left + 5f, top, left + 12f, top + iconSize, paint)
    val flagPath = android.graphics.Path()
    flagPath.moveTo(left + 12f, top)
    flagPath.lineTo(left + iconSize, top + iconSize / 4f)
    flagPath.lineTo(left + 12f, top + iconSize / 2f)
    flagPath.close()
    canvas.drawPath(flagPath, paint)

    return BitmapDrawable(context.resources, bitmap)
}
