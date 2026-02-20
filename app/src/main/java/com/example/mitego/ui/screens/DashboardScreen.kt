package com.example.mitego.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mitego.ui.theme.ForestGreen
import com.example.mitego.ui.theme.GoldAccent

data class AdventureItem(
    val id: String,
    val title: String,
    val isActive: Boolean
)

@Composable
fun DashboardScreen(
    onAdventureSelected: (String) -> Unit
) {
    val adventures = listOf(
        AdventureItem("BARO", "El Baró de Savassona", true),
        AdventureItem("SERPENT", "La Serpent de Manlleu", true),
        AdventureItem("ALTRES", "Més llegendes...", false)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
            .padding(24.dp)
    ) {
        Text(
<<<<<<< HEAD
            text = "Explora les LlegendeS",
            style = MaterialTheme.typography.headlineMedium,
            color = Color(0xFF202020),
            modifier = Modifier.padding(bottom = 32.dp, top = 16.dp)
=======
            text = "El Llibre de Llegendes",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold,
                color = Color.Black
            ),
            modifier = Modifier.padding(vertical = 24.dp)
>>>>>>> 0f8a79eccb4579cba4ceeea3a5fbad3eed57fda4
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(20.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            items(adventures) { adventure ->
                AdventureCard(
                    title = adventure.title,
                    isActive = adventure.isActive,
                    onClick = { onAdventureSelected(adventure.id) }
                )
            }
        }
    }
}

@Composable
fun AdventureCard(
    title: String,
    isActive: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
<<<<<<< HEAD
            .height(130.dp)
=======
            .height(100.dp)
>>>>>>> 0f8a79eccb4579cba4ceeea3a5fbad3eed57fda4
            .clickable(enabled = isActive) { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isActive) Color.White else Color(0xFFE0E0E0)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isActive) 6.dp else 0.dp)
    ) {
<<<<<<< HEAD
        Box(modifier = Modifier.fillMaxSize().padding(20.dp)) {
            Column(modifier = Modifier.align(Alignment.CenterStart)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    color = if (isActive) ForestGreen else Color.Gray,
                    fontWeight = FontWeight.Bold
                )
                if (!isActive) {
                    Text(
                        text = "Aviat disponible",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.Gray
                    )
                }
            }
=======
        Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = if (isActive) ForestGreen else Color.DarkGray
                ),
                modifier = Modifier.align(Alignment.CenterStart)
            )
>>>>>>> 0f8a79eccb4579cba4ceeea3a5fbad3eed57fda4

            if (isActive) {
                Icon(
                    imageVector = Icons.Default.Map,
<<<<<<< HEAD
                    contentDescription = "Active",
                    tint = GoldAccent,
                    modifier = Modifier.align(Alignment.BottomEnd).size(36.dp)
=======
                    contentDescription = "Activa",
                    tint = ForestGreen,
                    modifier = Modifier.align(Alignment.CenterEnd).size(32.dp)
>>>>>>> 0f8a79eccb4579cba4ceeea3a5fbad3eed57fda4
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Bloquejada",
                    tint = Color.Gray,
<<<<<<< HEAD
                    modifier = Modifier.align(Alignment.BottomEnd).size(28.dp)
=======
                    modifier = Modifier.align(Alignment.CenterEnd).size(32.dp)
>>>>>>> 0f8a79eccb4579cba4ceeea3a5fbad3eed57fda4
                )
            }
        }
    }
}
