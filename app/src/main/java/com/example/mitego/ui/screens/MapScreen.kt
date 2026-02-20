package com.example.mitego.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.mitego.model.GameStatus
import com.example.mitego.model.Point
import com.example.mitego.repository.GameRepository
import com.example.mitego.ui.components.addMarkersToMap
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay

@Composable
fun MapScreen(
    repository: GameRepository,
    onPointClick: (Point) -> Unit,
    onOpenBook: () -> Unit
) {
    val context = LocalContext.current
    val mapView = remember { MapView(context) }
    val points by repository.points.collectAsState()
    val gameState by repository.gameState.collectAsState()
    
    LaunchedEffect(Unit) {
        mapView.setTileSource(TileSourceFactory.MAPNIK)
        mapView.setMultiTouchControls(true)
        mapView.controller.setZoom(18.0)
        mapView.controller.setCenter(GeoPoint(41.930675, 2.254059)) // Inici Manlleu
        
        val locationProvider = GpsMyLocationProvider(context)
        val locationOverlay = object : MyLocationNewOverlay(locationProvider, mapView) {
            override fun onLocationChanged(location: android.location.Location?, source: org.osmdroid.views.overlay.mylocation.IMyLocationProvider?) {
                super.onLocationChanged(location, source)
                location?.let {
                    val isMock = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        it.isMock
                    } else {
                        @Suppress("DEPRECATION")
                        it.isFromMockProvider
                    }
                    repository.updateUserLocation(it.latitude, it.longitude, isMock)
                }
            }
        }
        locationOverlay.enableMyLocation()
        locationOverlay.enableFollowLocation()
        mapView.overlays.add(locationOverlay)
    }

    LaunchedEffect(points) {
        addMarkersToMap(mapView, points) { selectedPoint ->
            onPointClick(selectedPoint)
        }
    }

    Box(modifier = Modifier.fillMaxSize().background(color = Color.White)) {
        AndroidView(factory = { mapView }, modifier = Modifier.fillMaxSize())

        // BARRA SUPERIOR - PUNTUACIÓ I TEMPS
        TopApBarWithStats(
            score = gameState.totalScore,
            timerSeconds = gameState.timerSecondsRemaining,
            modifier = Modifier.align(Alignment.TopCenter)
        )

        // BARRA INFERIOR - INVENTARI
        InventoryBottomBar(
            inventory = gameState.inventory,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
        
        // PANTALLES DE FINAL DE JOC
        if (gameState.status == GameStatus.WON) {
            GameResultOverlay(title = "VICTÒRIA!", subtitle = "Has superat el repte!", color = Color.Green)
        } else if (gameState.status == GameStatus.LOST) {
            GameResultOverlay(title = "GAME OVER", subtitle = "El temps s'ha acabat.", color = Color.Red)
        }
    }
}

@Composable
fun TopApBarWithStats(
    score: Int,
    timerSeconds: Long?,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(color = Color.White.copy(alpha = 0.9f))
            .padding(horizontal = 16.dp)
            .shadow(elevation = 2.dp)
    ) {
        Column {
            Text(
                text = "Punts: $score",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
        }
        
        timerSeconds?.let { seconds ->
            val min = seconds / 60
            val sec = seconds % 60
            Text(
                text = String.format("%02d:%02d", min, sec),
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.Red
                )
            )
        } ?: Text(
            text = "CaçaMites",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = Color(0xffec6209)
            )
        )
    }
}

@Composable
fun InventoryBottomBar(
    inventory: Set<String>,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(70.dp)
            .background(color = Color.White.copy(alpha = 0.95f))
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        InventorySlot(icon = Icons.Default.Person, label = "Personatge", isCollected = inventory.isNotEmpty())
        InventorySlot(icon = Icons.Default.Star, label = "Objectes", isCollected = inventory.contains("espassa") || inventory.contains("rubi"))
    }
}

@Composable
fun InventorySlot(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, isCollected: Boolean) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (isCollected) Color(0xFFec6209) else Color.LightGray,
            modifier = Modifier.size(32.dp)
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = if (isCollected) Color.Black else Color.Gray
        )
    }
}

@Composable
fun GameResultOverlay(title: String, subtitle: String, color: Color) {
    Box(
        modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.8f)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = title, style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.Bold, color = color))
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = subtitle, style = MaterialTheme.typography.bodyLarge.copy(color = Color.White))
        }
    }
}
