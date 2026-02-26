package com.cacamites.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BottomMenuBar(
    activeItem: String,
    onFirstItemClick: () -> Unit,
    onTrobadorClick: () -> Unit,
    onBookClick: () -> Unit,
    firstItemIcon: ImageVector = Icons.Default.Map,
    firstItemLabel: String = "Mapa"
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .shadow(elevation = 8.dp),
        color = Color.White
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BottomMenuItem(
                icon = firstItemIcon,
                label = firstItemLabel,
                isActive = activeItem == "backpack" || activeItem == "map",
                onClick = onFirstItemClick,
                modifier = Modifier.weight(1f)
            )
            BottomMenuItem(
                icon = Icons.Default.Face,
                label = "Trobadora",
                isActive = activeItem == "trobador",
                onClick = onTrobadorClick,
                modifier = Modifier.weight(1f)
            )
            BottomMenuItem(
                icon = Icons.Default.Book,
                label = "Llibre",
                isActive = activeItem == "book",
                onClick = onBookClick,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun BottomMenuItem(
    icon: ImageVector,
    label: String,
    isActive: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val contentColor = if (isActive) Color(0xFF0B94FE) else Color.Gray
    
    Column(
        modifier = modifier
            .fillMaxHeight()
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = contentColor,
            modifier = Modifier.size(26.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            color = contentColor,
            fontSize = 12.sp,
            fontWeight = if (isActive) FontWeight.Bold else FontWeight.Medium
        )
    }
}
