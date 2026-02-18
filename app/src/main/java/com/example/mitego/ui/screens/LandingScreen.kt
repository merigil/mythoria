package com.example.mitego.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Terrain
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mitego.ui.theme.ForestGreen
import com.example.mitego.ui.theme.GoldAccent

@Composable
fun LandingScreen(
    onNavigateToMap: () -> Unit
) {
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(3000)
        onNavigateToMap()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White), // White background as requested
        contentAlignment = Alignment.Center
    ) {
        // Logo
        Icon(
            imageVector = Icons.Default.Terrain, // Placeholder until asset is set
            contentDescription = "CaçaMites Logo",
            tint = ForestGreen,
            modifier = Modifier.size(150.dp)
        )
    }
}
