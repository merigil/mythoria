package com.example.mitego.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
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
    val points by repository.points.collectAsState(initial = emptyList())
    val gameState by repository.gameState.collectAsState()
    
    // Setup Map Logic (Kept from original)
    LaunchedEffect(Unit) {
        mapView.setTileSource(TileSourceFactory.MAPNIK)
        mapView.setMultiTouchControls(true)
        mapView.controller.setZoom(18.0)
        mapView.controller.setCenter(GeoPoint(41.9323395951151, 2.252533598712291)) // Start at Test Point
        
        val locationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(context), mapView)
        locationOverlay.enableMyLocation()
        locationOverlay.enableFollowLocation()
        locationOverlay.isDrawAccuracyEnabled = true
        mapView.overlays.add(locationOverlay)
    }

    LaunchedEffect(points) {
        addMarkersToMap(mapView, points) { selectedPoint ->
            onPointClick(selectedPoint)
        }
    }

    // New UI Shell wrapping the Map
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        AndroidView(
            factory = { mapView },
            modifier = Modifier.fillMaxSize()
        )

        // TOP APP BAR - SCORE & TIMER
        TopApBarWithStats(
            score = gameState.currentScore,
            timeRemaining = gameState.timeRemainingMs,
            isTimerActive = gameState.isTimerActive,
            modifier = Modifier.align(Alignment.TopCenter)
        )

        // BOTTOM BAR - INVENTORY
        InventoryBottomBar(
            collectedItems = gameState.keyItemsCollected,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
        
        // GAME OVER / WIN OVERLAY
        gameState.gameResult?.let { result ->
            GameResultOverlay(result = result)
        }
    }
}

@Composable
fun TopApBarWithStats(
    score: Int,
    timeRemaining: Long,
    isTimerActive: Boolean,
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
                text = "Vida: $score",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = if (score > 100) Color(0xFF4CAF50) else Color.Black
                )
            )
            Text(
                text = "Objectiu: >100",
                style = MaterialTheme.typography.bodySmall.copy(color = Color.Gray)
            )
        }
        
        if (isTimerActive) {
            val minutes = (timeRemaining / 1000) / 60
            val seconds = (timeRemaining / 1000) % 60
            Text(
                text = String.format("%02d:%02d", minutes, seconds),
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.Red
                )
            )
        } else {
             Text(
                text = "CaçaMites",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xffec6209)
                )
            )
        }
    }
}

@Composable
fun InventoryBottomBar(
    collectedItems: List<String>,
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
        InventorySlot(icon = Icons.Default.Person, label = "Jove", isCollected = collectedItems.contains("p_jove"))
        InventorySlot(icon = Icons.Default.Face, label = "Baronessa", isCollected = collectedItems.contains("p_baronessa"))
        InventorySlot(icon = Icons.Default.Star, label = "Espasa", isCollected = collectedItems.contains("p_espasa"))
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
fun GameResultOverlay(result: com.example.mitego.model.GameResult) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.8f)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            val (title, color) = when(result) {
                com.example.mitego.model.GameResult.WIN -> "VICTÒRIA!" to Color.Green
                else -> "GAME OVER" to Color.Red
            }
            
            Text(
                text = title,
                style = MaterialTheme.typography.displayMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = color
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = if (result == com.example.mitego.model.GameResult.WIN) 
                    "Has vençut al Baró i alliberat la vall!" 
                else "El temps s'ha acabat o no tens prou força.",
                style = MaterialTheme.typography.bodyLarge.copy(color = Color.White),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

@Composable
fun Property1Default(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .height(58.dp)
            .background(color = Color.White.copy(alpha = 0.9f)) // Increased alpha for visibility
    ) {
        // Simulating the footer icons row
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.AccountCircle, // Placeholder
                contentDescription = "User",
                tint = Color(0xff1c1b1f),
                modifier = Modifier.size(24.dp)
            )
            // Other placeholders for footer icons
            Box(modifier = Modifier.size(24.dp).background(Color.LightGray, RoundedCornerShape(4.dp)))
            Box(modifier = Modifier.size(24.dp).background(Color.LightGray, RoundedCornerShape(4.dp)))
            Box(modifier = Modifier.size(24.dp).background(Color.LightGray, RoundedCornerShape(4.dp)))
        }
    }
}
