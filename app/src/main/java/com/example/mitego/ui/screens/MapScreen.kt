package com.example.mitego.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backpack
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.mitego.R
import com.example.mitego.model.Point
import com.example.mitego.model.PointState
import com.example.mitego.repository.GameRepository
import com.example.mitego.ui.components.BottomMenuBar
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
    onOpenBook: () -> Unit,
    onShowScoreboard: () -> Unit,
    onNavigateToTrobador: () -> Unit
) {
    val context = LocalContext.current
    val mapView = remember { MapView(context) }
    val points by repository.points.collectAsState(initial = emptyList())
    val gameState by repository.gameState.collectAsState()
    
    LaunchedEffect(Unit) {
        mapView.setTileSource(TileSourceFactory.MAPNIK)
        mapView.setMultiTouchControls(true)
        mapView.setBuiltInZoomControls(false) // Desactiva els botons de + i -
        mapView.controller.setZoom(18.0)
        mapView.controller.setCenter(GeoPoint(41.95531, 2.33645))
        
        val locationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(context), mapView)
        locationOverlay.enableMyLocation()
        locationOverlay.enableFollowLocation()
        locationOverlay.isDrawAccuracyEnabled = true
        mapView.overlays.add(locationOverlay)
    }

    LaunchedEffect(points) {
        addMarkersToMap(mapView, points) { selectedPoint ->
            // Tornem a permetre el clic sempre que el punt estigui VISIBLE
            // El repositori ja s'encarrega que la puntuació només es sumi el primer cop
            onPointClick(selectedPoint)
        }
    }

    Scaffold(
        bottomBar = {
            BottomMenuBar(
                activeItem = "backpack",
                onFirstItemClick = { /* Pròximament: Mochilla */ },
                onTrobadorClick = onNavigateToTrobador,
                onBookClick = onOpenBook,
                firstItemIcon = Icons.Default.Backpack,
                firstItemLabel = "Motxilla"
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(color = Color.White)
        ) {
            AndroidView(
                factory = { mapView },
                modifier = Modifier.fillMaxSize()
            )

            // HEADER
            Header(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .fillMaxWidth()
                    .padding(top = 10.dp, start = 16.dp, end = 16.dp),
                onScoreboardClick = onShowScoreboard
            )

            // Status / Timer
            Column(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 85.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (gameState.status != com.example.mitego.model.GameStatus.WAITING_TO_START) {
                    Box(
                        modifier = Modifier
                            .background(Color.White, RoundedCornerShape(20.dp))
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        val statusText = when (gameState.status) {
                             com.example.mitego.model.GameStatus.ACTIVE_PLAY -> "Suma 100 punts de vida i desafia el Baró"
                             com.example.mitego.model.GameStatus.BARO_CHALLENGE -> "FUGIU DEL BARÓ!"
                             com.example.mitego.model.GameStatus.WON -> "HAS GUANYAT!"
                             com.example.mitego.model.GameStatus.LOST -> "Has perdut..."
                             else -> ""
                        }
                        
                        if (statusText.isNotEmpty()) {
                            Text(
                                text = statusText,
                                color = Color.Black,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
                
                if (gameState.status == com.example.mitego.model.GameStatus.BARO_CHALLENGE && gameState.timerSecondsRemaining != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .background(Color.Red, RoundedCornerShape(12.dp))
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        val min = gameState.timerSecondsRemaining!! / 60
                        val sec = gameState.timerSecondsRemaining!! % 60
                        Text(
                            text = "%02d:%02d".format(min, sec),
                            color = Color.White,
                            fontWeight = FontWeight.Black,
                            fontSize = 22.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Header(
    modifier: Modifier = Modifier,
    onScoreboardClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .shadow(4.dp, CircleShape)
                .background(Color.White, CircleShape)
                .padding(4.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo_cacamites_petit),
                contentDescription = "Logo Mythoria",
                modifier = Modifier.fillMaxSize().clip(CircleShape)
            )
        }
        
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .shadow(2.dp, CircleShape)
                    .clip(CircleShape)
                    .background(Color.White)
                    .clickable { onScoreboardClick() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.EmojiEvents,
                    contentDescription = "Rànquing",
                    tint = Color(0xFF0B94FE),
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .shadow(2.dp, CircleShape)
                    .clip(CircleShape)
                    .background(Color(0xFF0B94FE)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile",
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}
