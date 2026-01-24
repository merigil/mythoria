package com.example.mitego.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.mitego.model.Point
import com.example.mitego.model.PointState
import com.example.mitego.model.PointType
import com.example.mitego.ui.theme.GoldAccent
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

// Note: Since OSMDroid is View-based, we ideally use its native Marker overlay.
// However, for this MVP we can manipulate the Marker object from a Composable side-effect 
// or simpler: just create a function to add markers to the MapView.

// To keep it "Compose-ish", we'll create a helper that syncs the list of points to the MapView.

fun addMarkersToMap(
    mapView: MapView,
    points: List<Point>,
    onPointClick: (Point) -> Unit
) {
    // Clear existing overlays (Markers & Polygons) but keep User Location
    mapView.overlays.removeAll { it !is org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay }

    points.forEach { point ->
        if (point.state != PointState.LOCKED) {
            // 1. Draw Interaction Circle
            val circle = org.osmdroid.views.overlay.Polygon().apply {
                // Fix: Use setPoints() explicitly
                setPoints(org.osmdroid.views.overlay.Polygon.pointsAsCircle(point.coordinate, point.interactionRadius))
                
                // Color logic: Blue for Start, Green for everything else
                val baseColor = if (point.id == "p_start") {
                    android.graphics.Color.BLUE
                } else {
                    android.graphics.Color.GREEN
                }
                
                fillColor = android.graphics.Color.argb(40, android.graphics.Color.red(baseColor), android.graphics.Color.green(baseColor), android.graphics.Color.blue(baseColor))
                strokeColor = baseColor
                strokeWidth = 2.0f
                title = point.title // Optional
            }
            mapView.overlays.add(circle)

            // 2. Draw Marker
            val marker = Marker(mapView)
            marker.position = point.coordinate
            marker.title = point.title
            marker.id = point.id
            
            // Custom Icon logic based on state
            val iconDrawable = when (point.state) {
                PointState.LOCKED -> android.R.drawable.ic_lock_lock
                PointState.VISIBLE -> android.R.drawable.star_on // Or generic marker
                PointState.COMPLETED -> android.R.drawable.checkbox_on_background
            }
            // For MVP, relying on default marker pin is safer if drawables aren't perfect.
            // But if we want to distinguish:
            // marker.icon = ContextCompat.getDrawable(...) 
            
            marker.setOnMarkerClickListener { _, _ ->
                onPointClick(point)
                true
            }
            
            mapView.overlays.add(marker)
        }
    }
    
    mapView.invalidate()
}
