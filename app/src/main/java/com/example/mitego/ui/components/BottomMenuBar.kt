package com.example.mitego.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backpack
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BottomMenuBar(
    activeItem: String, // "map", "trobador", "book", "backpack"
    onFirstItemClick: () -> Unit,
    onTrobadorClick: () -> Unit,
    onBookClick: () -> Unit,
    firstItemIcon: ImageVector = Icons.Default.Map,
    firstItemLabel: String = "Explora"
) {
    val activeColor = Color(0xff0b94fe)
    val inactiveColor = Color.Gray

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shadowElevation = 16.dp,
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .padding(bottom = 4.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            MenuItem(
                icon = firstItemIcon,
                label = firstItemLabel,
                isSelected = activeItem == "map" || activeItem == "backpack",
                color = if (activeItem == "map" || activeItem == "backpack") activeColor else inactiveColor,
                onClick = onFirstItemClick
            )
            MenuItem(
                icon = Icons.Default.Face,
                label = "Trobador",
                isSelected = activeItem == "trobador",
                color = if (activeItem == "trobador") activeColor else inactiveColor,
                onClick = onTrobadorClick
            )
            MenuItem(
                icon = Icons.Default.MenuBook,
                label = "El Llibre",
                isSelected = activeItem == "book",
                color = if (activeItem == "book") activeColor else inactiveColor,
                onClick = onBookClick
            )
        }
    }
}

@Composable
private fun MenuItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    color: Color,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .clickable { onClick() }
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = color,
            modifier = Modifier.size(28.dp)
        )
        Text(
            text = label,
            color = color,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Black else FontWeight.Medium
        )
    }
}
