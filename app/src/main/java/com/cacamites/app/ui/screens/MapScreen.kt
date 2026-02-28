package com.cacamites.app.ui.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.cacamites.app.R
import com.cacamites.app.model.Point
import com.cacamites.app.model.PointState
import com.cacamites.app.repository.GameRepository
import com.cacamites.app.ui.components.BottomMenuBar
import com.cacamites.app.ui.components.addMarkersToMap
import com.cacamites.app.ui.theme.OpenSans
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

    val locationOverlay = remember { MyLocationNewOverlay(GpsMyLocationProvider(context), mapView) }

    var locationPermissionGranted by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED
        )
    }

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        locationPermissionGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
                || permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
    }

    LaunchedEffect(Unit) {
        if (!locationPermissionGranted) {
            locationPermissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
        }
        mapView.setTileSource(TileSourceFactory.MAPNIK)
        mapView.setMultiTouchControls(true)
        mapView.controller.setZoom(18.0)

        val isSerpent = points.any { it.id.startsWith("s_") && it.state != PointState.LOCKED }
        val centerLocation = if (isSerpent) GeoPoint(41.930675, 2.254059) else GeoPoint(41.931992, 2.252433)
        mapView.controller.setCenter(centerLocation)
    }

    LaunchedEffect(locationPermissionGranted) {
        if (locationPermissionGranted) {
            locationOverlay.enableMyLocation()
            locationOverlay.enableFollowLocation()
            if (!mapView.overlays.contains(locationOverlay)) {
                mapView.overlays.add(locationOverlay)
            }
        }
    }

    LaunchedEffect(points) {
        while (true) {
            val userLocation = locationOverlay.myLocation
            if (userLocation != null) {
                points.filter { it.state == PointState.VISIBLE }.forEach { point ->
                    if (!repository.isVibrated(point.id)) {
                        val distance = userLocation.distanceToAsDouble(point.coordinate)
                        if (distance <= 20.0) {
                            val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                                vibratorManager.defaultVibrator
                            } else {
                                @Suppress("DEPRECATION")
                                context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                            }

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                vibrator.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE))
                            } else {
                                @Suppress("DEPRECATION")
                                vibrator.vibrate(300)
                            }
                            repository.markAsVibrated(point.id)
                        }
                    }
                }
            }
            kotlinx.coroutines.delay(2000)
        }
    }

    LaunchedEffect(points) {
        addMarkersToMap(mapView, points) { selectedPoint ->
            val userLocation = locationOverlay.myLocation
            if (userLocation != null) {
                val distance = userLocation.distanceToAsDouble(selectedPoint.coordinate)
                if (distance <= 20.0) {
                    // Si per algun motiu no ha vibrat automàticament (ex: delay del loop), vibrem en clicar
                    if (!repository.isVibrated(selectedPoint.id)) {
                        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                            vibratorManager.defaultVibrator
                        } else {
                            @Suppress("DEPRECATION")
                            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                        }

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
                        } else {
                            @Suppress("DEPRECATION")
                            vibrator.vibrate(200)
                        }
                        repository.markAsVibrated(selectedPoint.id)
                    }
                    onPointClick(selectedPoint)
                } else {
                    Toast.makeText(context, "Estàs massa lluny per interactuar!", Toast.LENGTH_SHORT).show()
                }
            } else {
                onPointClick(selectedPoint)
            }
        }
    }

    Scaffold(
        bottomBar = {
            BottomMenuBar(
                activeItem = "backpack",
                onFirstItemClick = { },
                onTrobadorClick = onNavigateToTrobador,
                onBookClick = onOpenBook,
                firstItemIcon = Icons.Default.Person,
                firstItemLabel = "Motxilla"
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding).background(color = Color.White)) {
            AndroidView(factory = { mapView }, modifier = Modifier.fillMaxSize())

            HeaderMap(
                modifier = Modifier.align(Alignment.TopCenter).fillMaxWidth().padding(top = 10.dp, start = 16.dp, end = 16.dp),
                onScoreboardClick = onShowScoreboard
            )

            Column(
                modifier = Modifier.align(Alignment.TopCenter).padding(top = 85.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (gameState.status != com.cacamites.app.model.GameStatus.WAITING_TO_START) {
                    Box(
                        modifier = Modifier
                            .background(Color.White, RoundedCornerShape(20.dp))
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .shadow(2.dp, RoundedCornerShape(20.dp))
                    ) {
                        val isSerpent = points.any { it.id.startsWith("s_") && it.state != PointState.LOCKED }
                        val statusText = when (gameState.status) {
                            com.cacamites.app.model.GameStatus.ACTIVE_PLAY -> if (isSerpent) "Busca les pistes de la Serpent" else "Explora el territori i completa la llegenda. Per aconseguir-ho, hauràs de:\nReunir 101 punts de força\nTrobar l’espasa\nParlar amb la Baronessa i el criat\nCercar el Baró i enfrontar-t’hi"
                            com.cacamites.app.model.GameStatus.WON -> "HAS GUANYAT!"
                            com.cacamites.app.model.GameStatus.LOST -> "Has perdut..."
                            else -> ""
                        }
                        if (statusText.isNotEmpty()) {
                            Text(
                                text = statusText,
                                style = TextStyle(
                                    fontFamily = OpenSans,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Gray,
                                    lineHeight = 18.sp
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HeaderMap(modifier: Modifier = Modifier, onScoreboardClick: () -> Unit) {
    Row(modifier = modifier.fillMaxWidth().height(60.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
        Box(modifier = Modifier.size(50.dp).shadow(4.dp, CircleShape).background(Color.White, CircleShape).padding(4.dp), contentAlignment = Alignment.Center) {
            Image(painter = painterResource(id = R.drawable.logo_cacamites_petit), contentDescription = "Logo", modifier = Modifier.fillMaxSize().clip(CircleShape))
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(40.dp).shadow(2.dp, CircleShape).clip(CircleShape).background(Color.White).clickable { onScoreboardClick() }, contentAlignment = Alignment.Center) {
                Icon(imageVector = Icons.Default.EmojiEvents, contentDescription = "Rànquing", tint = Color(0xFF0B94FE), modifier = Modifier.size(24.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Box(modifier = Modifier.size(40.dp).shadow(2.dp, CircleShape).clip(CircleShape).background(Color(0xFF0B94FE)), contentAlignment = Alignment.Center) {
                Icon(imageVector = Icons.Default.Person, contentDescription = "Perfil", tint = Color.White, modifier = Modifier.size(22.dp))
            }
        }
    }
}
