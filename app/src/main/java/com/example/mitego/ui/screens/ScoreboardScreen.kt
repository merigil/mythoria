package com.example.mitego.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mitego.model.GameStatus
import com.example.mitego.repository.GameRepository
import com.example.mitego.ui.theme.ForestGreen
import com.example.mitego.ui.theme.GoldAccent
import com.example.mitego.ui.theme.Merienda

@Composable
fun ScoreboardScreen(
    repository: GameRepository,
    onClose: () -> Unit
) {
    val gameState by repository.gameState.collectAsState()
    val points by repository.points.collectAsState()

    // Filter only visited points to show in list
    val visitedPointIds = gameState.visitedPoints
    val discoveredPoints = points.filter { visitedPointIds.contains(it.id) }
        .sortedByDescending { kotlin.math.abs(it.score) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)) // Light Gray background
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Header
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(ForestGreen)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Puntuació",
                    style = MaterialTheme.typography.headlineMedium.copy(fontFamily = Merienda),
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Center)
                )
                IconButton(
                    onClick = onClose,
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    Icon(Icons.Default.Close, contentDescription = "Tancar", tint = Color.White)
                }
            }

            // Score Summary
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Total Punts",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.Gray
                    )
                    Text(
                        text = "${gameState.totalScore}",
                        style = MaterialTheme.typography.displayLarge.copy(fontFamily = Merienda, fontWeight = FontWeight.Bold),
                        color = ForestGreen
                    )
                    
                    if (gameState.inventory.contains("espassa")) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "⚔️ Espassa Aconseguida! ⚔️",
                            color = GoldAccent,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    if (gameState.status == GameStatus.BARO_CHALLENGE && gameState.timerSecondsRemaining != null) {
                        Spacer(modifier = Modifier.height(16.dp))
                        val min = gameState.timerSecondsRemaining!! / 60
                        val sec = gameState.timerSecondsRemaining!! % 60
                        Text(
                            text = "Temps Restant: %02d:%02d".format(min, sec),
                            color = Color.Red,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Visited Items List
            Text(
                text = "Descobertes (${discoveredPoints.size})",
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                items(discoveredPoints) { point ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White, RoundedCornerShape(8.dp))
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = point.title, fontWeight = FontWeight.Bold)
                            // Could add description or type
                        }
                        
                        val scoreText = if (point.score > 0) "+${point.score}" else "${point.score}"
                        val scoreColor = if (point.score >= 0) ForestGreen else Color.Red
                        
                        Text(
                            text = scoreText,
                            color = scoreColor,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                }
            }
            
            // Restart Button (Only if Game Over/Won)
            if (gameState.status == GameStatus.WON || gameState.status == GameStatus.LOST) {
                 androidx.compose.material3.Button(
                    onClick = { repository.restartGame() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = GoldAccent)
                ) {
                    Text("Reiniciar Joc")
                }
            }
        }
    }
}
