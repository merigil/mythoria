package com.cacamites.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import com.cacamites.app.model.Adventure
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
import com.cacamites.app.ui.theme.ForestGreen
import com.cacamites.app.ui.theme.GoldAccent

@Composable
fun DashboardScreen(
    adventures: List<Adventure> = emptyList(), // Provide default or manage via state
    onAdventureSelected: (String) -> Unit
) {
    // Note: adventures is usually passed from MainScreen or a ViewModel
    // Let's ensure it has at least the default active one if empty for safety
    val displayAdventures = adventures.ifEmpty { 
        listOf(
            Adventure("baronessa", "La Font de la Baronessa", true),
            Adventure("serpent", "La Serpent de Manlleu", true),
            Adventure("tres_creus", "Les Tres Creus", false)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
            .padding(24.dp)
    ) {
        Text(
            text = "Explora les LlegendeS",
            style = MaterialTheme.typography.headlineMedium,
            color = Color(0xFF202020),
            modifier = Modifier.padding(bottom = 32.dp, top = 16.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(20.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            items(displayAdventures) { adventure ->
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
            .height(130.dp)
            .clickable(enabled = isActive) { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isActive) Color.White else Color(0xFFE0E0E0)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isActive) 6.dp else 0.dp)
    ) {
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

            if (isActive) {
                Icon(
                    imageVector = Icons.Default.Map,
                    contentDescription = "Active",
                    tint = GoldAccent,
                    modifier = Modifier.align(Alignment.BottomEnd).size(36.dp)
                )
            } else {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Locked",
                    tint = Color.Gray,
                    modifier = Modifier.align(Alignment.BottomEnd).size(28.dp)
                )
            }
        }
    }
}
