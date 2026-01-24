package com.example.mitego.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.mitego.model.Card
import com.example.mitego.repository.GameRepository
import com.example.mitego.ui.theme.ForestGreen
import com.example.mitego.ui.theme.GoldAccent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LegendBookScreen(
    repository: GameRepository,
    onBack: () -> Unit,
    onCardClick: (String) -> Unit
) {
    val cards by repository.cards.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Llibre de Llegendes") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "Tornar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = ForestGreen,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Summary Header
            val unlockedCount = cards.count { it.isUnlocked }
            val totalCount = cards.size.coerceAtLeast(1) // Avoid div by zero in future
            
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(ForestGreen.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                    .padding(16.dp)
            ) {
                Text(
                    text = "Llegendes Descobertes: $unlockedCount / $totalCount",
                    style = MaterialTheme.typography.titleMedium,
                    color = ForestGreen
                )
            }

            // Grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(cards) { card ->
                    LegendCardItem(card) {
                        if (card.isUnlocked) {
                            onCardClick(card.id)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LegendCardItem(
    card: Card,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.75f) // Book Aspect Ratio
            .clickable(enabled = card.isUnlocked) { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (card.isUnlocked) Color.White else Color.LightGray
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (card.isUnlocked) {
                Column(
                    modifier = Modifier.padding(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .background(ForestGreen.copy(alpha = 0.2f), RoundedCornerShape(4.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = card.title.take(1).uppercase(),
                            style = MaterialTheme.typography.displayMedium,
                            color = ForestGreen
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = card.title,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        maxLines = 2
                    )
                }
            } else {
                // Locked State
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Bloquejat",
                        tint = Color.Gray,
                        modifier = Modifier.fillMaxSize(0.4f)
                    )
                }
            }
        }
    }
}
