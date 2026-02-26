package com.cacamites.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cacamites.app.model.Card
import com.cacamites.app.repository.GameRepository
import com.cacamites.app.ui.theme.Merienda
import com.cacamites.app.ui.theme.Montserrat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LegendBookScreen(
    repository: GameRepository,
    onBack: () -> Unit,
    onCardClick: (String) -> Unit
) {
    val cards by repository.cards.collectAsState()
    val primaryBlue = Color(0xff0b94fe)
    val textBlack = Color(0xFF202020) // Negre carbó intens

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Text(
                        "El meu llibre de llegendes", 
                        style = TextStyle(
                            fontFamily = Merienda, 
                            fontSize = 22.sp, 
                            fontWeight = FontWeight.Black,
                            color = textBlack // Forcem el color negre intens
                        )
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "Tornar", tint = primaryBlue)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = textBlack // Assegurem que la barra també ho sàpiga
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF8F9FA))
        ) {
            // Summary Header
            val unlockedCount = cards.count { it.isUnlocked }
            val totalCount = cards.size.coerceAtLeast(1)
            
            Surface(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                color = primaryBlue.copy(alpha = 0.05f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Llegendes Descobertes: $unlockedCount / $totalCount",
                    style = TextStyle(
                        fontFamily = Montserrat, 
                        fontWeight = FontWeight.Black, 
                        fontSize = 14.sp
                    ),
                    color = primaryBlue,
                    modifier = Modifier.padding(16.dp)
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
    val primaryBlue = Color(0xff0b94fe)
    val textBlack = Color(0xFF202020)
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.8f)
            .clickable(enabled = card.isUnlocked) { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (card.isUnlocked) Color.White else Color(0xFFE0E0E0)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (card.isUnlocked) 6.dp else 0.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            if (card.isUnlocked) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .background(primaryBlue.copy(alpha = 0.1f), RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = card.title.take(1).uppercase(),
                            style = TextStyle(
                                fontFamily = Merienda, 
                                fontSize = 40.sp, 
                                color = primaryBlue,
                                fontWeight = FontWeight.Black
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = card.title,
                        style = TextStyle(
                            fontFamily = Montserrat, 
                            fontWeight = FontWeight.Black, 
                            fontSize = 13.sp,
                            color = textBlack
                        ),
                        maxLines = 2
                    )
                }
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Bloquejat",
                        tint = Color.Gray,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }
    }
}
