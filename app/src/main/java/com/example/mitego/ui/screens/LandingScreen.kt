package com.example.mitego.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Terrain
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.mitego.ui.theme.ForestGreen
import kotlinx.coroutines.delay

@Composable
fun LandingScreen(
    onNavigateToMap: () -> Unit
) {
    LaunchedEffect(Unit) {
        delay(3000)
        onNavigateToMap()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        // Logo
        Icon(
            imageVector = Icons.Default.Terrain,
            contentDescription = "CaçaMites Logo",
            tint = ForestGreen,
            modifier = Modifier.size(150.dp)
        )
    }
}
