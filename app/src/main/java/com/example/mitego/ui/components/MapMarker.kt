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
    // Clear existing markers to avoid duplicates (naive approach for MVP)
    // In production, we'd diff the list.
    mapView.overlays.removeAll { it is Marker && it.id != "user_location" }

    points.forEach { point ->
        val marker = Marker(mapView)
        marker.position = point.coordinate
        marker.title = point.title
        marker.id = point.id
        
        // Custom Icon logic based on state
        val iconDrawable = when (point.state) {
            PointState.LOCKED -> android.R.drawable.ic_lock_lock // Placeholder system icons
            PointState.VISIBLE -> android.R.drawable.star_on
            PointState.COMPLETED -> android.R.drawable.checkbox_on_background
        }
        
        // We can't easily set a Compose Drawable here without converting.
        // For MVP, we'll rely on default markers or set simplified colors if possible.
        // marker.icon = ...
        
        marker.setOnMarkerClickListener { _, _ ->
            onPointClick(point)
            true
        }
        
        mapView.overlays.add(marker)
    }
    
    mapView.invalidate()
}
