package com.example.mitego.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mitego.model.Card
import com.example.mitego.ui.theme.ForestGreen
import com.example.mitego.ui.theme.GoldAccent

@Composable
fun LegendDetailScreen(
    card: Card,
    onClose: () -> Unit,
    repository: GameRepository // Afegim el repository per accedir a la proximitat
) {
    val scrollState = rememberScrollState()
    val userLocation by repository.userLocation.collectAsState()
    val points by repository.points.collectAsState()
    
    // Trobem el punt corresponent a aquesta carta per saber-ne les coordenades
    val point = remember(card.id, points) {
        val pointId = if (card.id.startsWith("c_s_")) card.id.removePrefix("c_") else if (card.id.startsWith("c_")) "p_" + card.id.removePrefix("c_") else card.id
        points.find { it.id == pointId }
    }

    // Lògica de proximitat
    val proximityState = remember(userLocation, point) {
        if (userLocation != null && point != null) {
            repository.checkProximity(
                userLocation!!.latitude, userLocation!!.longitude,
                point.coordinate.latitude, point.coordinate.longitude
            )
        } else {
            Pair(false, -1f)
        }
    }
    
    val (isNear, distance) = proximityState

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(scrollState)
    ) {
        // Hero Image Area (Placeholder logic for now)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .background(ForestGreen)
        ) {
            IconButton(
                onClick = onClose,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
                    .background(Color.Black.copy(alpha = 0.3f), shape = MaterialTheme.shapes.small)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Tancar",
                    tint = Color.White
                )
            }
        }

        Column(modifier = Modifier.padding(24.dp)) {
            Text(
                text = card.title,
                style = MaterialTheme.typography.displayMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = ForestGreen
                )
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = card.type.uppercase(),
                style = MaterialTheme.typography.labelMedium.copy(
                    color = GoldAccent,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = card.description,
                style = MaterialTheme.typography.bodyLarge.copy(
                    lineHeight = 28.sp
                ),
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // BOTÓ DINÀMIC DE JOC (Basat en proximitat)
            if (point != null) {
                Button(
                    onClick = { /* Aquí aniria la lògica per obrir el minijoc o acció */ },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    enabled = isNear,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isNear) Color(0xFFec6209) else Color.Gray,
                        disabledContainerColor = Color.LightGray
                    )
                ) {
                    if (isNear) {
                        Text("COMPENÇA LA LLEGENDA!", fontWeight = FontWeight.Bold)
                    } else {
                        val distText = if (distance >= 0) " (${distance.toInt()}m)" else ""
                        Text("ACOSTA'T MÉS PER JUGAR$distText", fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }
            
            Button(
                onClick = onClose,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = ForestGreen)
            ) {
                Text("Tornar al Mapa")
            }
        }
    }
}
