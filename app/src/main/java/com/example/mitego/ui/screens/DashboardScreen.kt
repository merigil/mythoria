package com.example.mitego.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessibilityNew
import androidx.compose.material.icons.filled.Diversity3
import androidx.compose.material.icons.filled.EscalatorWarning
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mitego.R
import com.example.mitego.ui.components.BottomMenuBar
import com.example.mitego.ui.components.MainTopAppBar
import com.example.mitego.ui.components.SearchField

@Composable
fun DashboardScreen(
    onNavigateToMap: () -> Unit,
    onNavigateToTrobador: () -> Unit
) {
    Scaffold(
        topBar = {
            MainTopAppBar(
                onMenuClick = { /* Accions del menú */ }
            )
        },
        bottomBar = {
            BottomMenuBar(
                activeItem = "book",
                onFirstItemClick = onNavigateToMap,
                onTrobadorClick = onNavigateToTrobador,
                onBookClick = { /* Ja estem aquí */ }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(12.dp))
            
            Text(
                text = "El meu llibre de llegendes",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Black,
                    color = Color.Black,
                    fontSize = 20.sp
                ),
                modifier = Modifier.padding(vertical = 8.dp)
            )

            SearchField(modifier = Modifier.padding(vertical = 8.dp))

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                item {
                    LegendAdventureCard(
                        title = "La font de la Baronessa",
                        imageRes = R.drawable.foto_castell_del_baro,
                        isActive = true,
                        onClick = onNavigateToMap
                    )
                }
                item {
                    LegendAdventureCard(
                        title = "El Castell Perdut",
                        imageRes = null,
                        isActive = false,
                        onClick = {}
                    )
                }
                item {
                    LegendAdventureCard(
                        title = "Les Coves del Drac",
                        imageRes = null,
                        isActive = false,
                        onClick = {}
                    )
                }
            }
        }
    }
}

@Composable
fun LegendAdventureCard(
    title: String,
    imageRes: Int?,
    isActive: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 131.dp)
            .shadow(elevation = 2.dp, shape = RoundedCornerShape(12.dp))
            .clickable(enabled = isActive) { onClick() },
        color = if (isActive) Color.White else Color(0xFFF0F0F0),
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp)
            ) {
                // Fila superior amb Imatge i Textos
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Imatge
                    Box(
                        modifier = Modifier
                            .size(70.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.LightGray)
                    ) {
                        if (imageRes != null) {
                            Image(
                                painter = painterResource(id = imageRes),
                                contentDescription = title,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize(),
                                alpha = if (isActive) 1f else 0.5f
                            )
                        }
                        if (!isActive) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Locked",
                                tint = Color.Gray,
                                modifier = Modifier.align(Alignment.Center).size(24.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // Textos
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = title,
                            style = TextStyle(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isActive) Color.Black else Color.Gray
                            )
                        )
                        
                        Text(
                            text = "A Osona (Temps aprox. 2 h)",
                            style = TextStyle(
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (isActive) Color.Gray else Color.LightGray
                            )
                        )
                    }
                }

                // Botó a la part inferior ocupant tot l'ample
                if (isActive) {
                    Spacer(modifier = Modifier.height(14.dp))
                    
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                            .shadow(elevation = 2.dp, shape = RoundedCornerShape(6.dp)),
                        color = Color(0xff0b94fe),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "ENTRA",
                                color = Color.White,
                                style = TextStyle(
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Black
                                ),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            // Icones de dificultat (Dalt a la dreta, sense solapar gràcies al padding de la columna)
            if (isActive) {
                Row(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 10.dp, end = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(imageVector = Icons.Default.AccessibilityNew, contentDescription = null, modifier = Modifier.size(14.dp), tint = Color.LightGray)
                    Icon(imageVector = Icons.Default.Diversity3, contentDescription = null, modifier = Modifier.size(14.dp), tint = Color.LightGray)
                    Icon(imageVector = Icons.Default.EscalatorWarning, contentDescription = null, modifier = Modifier.size(14.dp), tint = Color.LightGray)
                }
            }
        }
    }
}
