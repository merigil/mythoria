package com.example.mitego.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessibleForward
import androidx.compose.material.icons.filled.Diversity3
import androidx.compose.material.icons.filled.EmojiPeople
import androidx.compose.material.icons.filled.EscalatorWarning
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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

data class LegendAdventure(
    val id: String, // Identificador únic
    val title: String,
    val imageUrl: String?,
    val isActive: Boolean,
    val location: String = "A Osona (Temps aprox. 2 h)",
    val isAccessible: Boolean = false,
    val isForwardAccessible: Boolean = false
)

@Composable
fun DashboardScreen(
    onNavigateToMap: (String) -> Unit, // Ara accepta l'ID de la llegenda
    onNavigateToTrobador: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    
    val allLegends = remember {
        listOf(
            LegendAdventure("BARO", "La font de la Baronessa", "foto_castell_del_baro", true, isAccessible = true),
            LegendAdventure("SERPENT", "La Serpent de Manlleu", "placa_de_fra_bernadi_manlleu", true, "A Osona (Temps aprox. 1 h)", isAccessible = true, isForwardAccessible = true),
            LegendAdventure("MINYONA", "El Salt de la Minyona", "salt_de_la_minyona", true, "A Osona (Temps aprox. 2 h)", isAccessible = true),
            LegendAdventure("CASTELL", "El Castell Perdut", null, false),
            LegendAdventure("COVES", "Les Coves del Drac", null, false)
        )
    }

    val filteredLegends = remember(searchQuery) {
        if (searchQuery.isBlank()) {
            allLegends
        } else {
            allLegends.filter { it.title.contains(searchQuery, ignoreCase = true) }
        }
    }

    Scaffold(
        topBar = {
            MainTopAppBar(
                onMenuClick = { /* Accions del menú */ }
            )
        },
        bottomBar = {
            BottomMenuBar(
                activeItem = "book",
                onFirstItemClick = { /* Pròximament: Mapa general */ },
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

            SearchField(
                query = searchQuery,
                onQueryChange = { searchQuery = it },
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(filteredLegends) { legend ->
                    LegendAdventureCard(
                        title = legend.title,
                        imageUrl = legend.imageUrl,
                        isActive = legend.isActive,
                        location = legend.location,
                        isAccessible = legend.isAccessible,
                        isForwardAccessible = legend.isForwardAccessible,
                        onClick = { onNavigateToMap(legend.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun LegendAdventureCard(
    title: String,
    imageUrl: String?,
    isActive: Boolean,
    location: String,
    isAccessible: Boolean = false,
    isForwardAccessible: Boolean = false,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(70.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.LightGray)
                    ) {
                        if (imageUrl != null) {
                            val resId = context.resources.getIdentifier(imageUrl, "drawable", context.packageName)
                            if (resId != 0) {
                                Image(
                                    painter = painterResource(id = resId),
                                    contentDescription = title,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize(),
                                    alpha = if (isActive) 1f else 0.5f
                                )
                            }
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
                            text = location,
                            style = TextStyle(
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (isActive) Color.Gray else Color.LightGray
                            )
                        )
                    }
                }

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

            if (isActive) {
                Row(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 10.dp, end = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val iconColor = Color(0xff0b94fe)
                    Icon(
                        imageVector = Icons.Default.Diversity3, 
                        contentDescription = null, 
                        modifier = Modifier.size(14.dp), 
                        tint = iconColor
                    )
                    Icon(
                        imageVector = Icons.Default.EscalatorWarning, 
                        contentDescription = null, 
                        modifier = Modifier.size(14.dp), 
                        tint = iconColor
                    )
                    Icon(
                        imageVector = Icons.Default.EmojiPeople,
                        contentDescription = "Accessible", 
                        modifier = Modifier.size(14.dp), 
                        tint = iconColor
                    )
                    if (isForwardAccessible) {
                        Icon(
                            imageVector = Icons.Default.AccessibleForward,
                            contentDescription = "Forward Accessible",
                            modifier = Modifier.size(14.dp),
                            tint = iconColor
                        )
                    }
                }
            }
        }
    }
}
