package com.example.mitego.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mitego.R
import com.example.mitego.model.Card
import com.example.mitego.ui.theme.ForestGreen
import com.example.mitego.ui.theme.GoldAccent

@Composable
fun LegendDetailScreen(
    card: Card,
    points: Int = 0,
    onClose: () -> Unit
) {
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            // Hero Image with Gradient Overlay
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.foto_castell_del_baro),
                    contentDescription = card.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                
                // Gradient to make title pop if needed (optional)
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.4f)),
                                startY = 600f
                            )
                        )
                )
            }

            // Content Card
            Column(
                modifier = Modifier
                    .offset(y = (-20).dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                    .background(Color.White)
                    .padding(24.dp)
            ) {
                // Category Tag
                Surface(
                    color = GoldAccent.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = card.type.uppercase(),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = GoldAccent,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.5.sp
                        )
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Title
                Text(
                    text = card.title,
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Black,
                        fontSize = 28.sp,
                        color = Color.Black
                    )
                )

                // Points Badge
                if (points != 0) {
                    Spacer(modifier = Modifier.height(16.dp))
                    val sign = if (points > 0) "+" else ""
                    val badgeColor = if (points > 0) Color(0xFF4CAF50) else Color(0xFFE53935)
                    
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            color = badgeColor,
                            shape = CircleShape,
                            modifier = Modifier.size(8.dp)
                        ) {}
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "$sign$points PUNTS DE VIDA",
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = badgeColor,
                                fontWeight = FontWeight.ExtraBold
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                
                // Divider
                Divider(color = Color.LightGray.copy(alpha = 0.5f))
                Spacer(modifier = Modifier.height(24.dp))

                // Description
                Text(
                    text = card.description,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        lineHeight = 26.sp,
                        fontSize = 16.sp,
                        color = Color(0xFF333333)
                    )
                )
                
                Spacer(modifier = Modifier.height(40.dp))
                
                // Bottom Button
                Button(
                    onClick = onClose,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xff0b94fe))
                ) {
                    Text(
                        "TORNAR AL MAPA",
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    )
                }
                
                Spacer(modifier = Modifier.height(20.dp))
            }
        }

        // Floating Close Button
        IconButton(
            onClick = onClose,
            modifier = Modifier
                .padding(16.dp)
                .size(40.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.8f))
                .align(Alignment.TopEnd)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Tancar",
                tint = Color.Black
            )
        }
    }
}
