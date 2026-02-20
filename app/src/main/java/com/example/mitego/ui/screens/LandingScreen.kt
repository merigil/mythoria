package com.example.mitego.ui.screens

<<<<<<< HEAD
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
=======
import androidx.compose.foundation.background
>>>>>>> 0f8a79eccb4579cba4ceeea3a5fbad3eed57fda4
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
<<<<<<< HEAD
import androidx.compose.material3.Text
=======
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Terrain
import androidx.compose.material3.Icon
>>>>>>> 0f8a79eccb4579cba4ceeea3a5fbad3eed57fda4
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
<<<<<<< HEAD
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mitego.R
import com.example.mitego.ui.theme.Merienda
=======
import androidx.compose.ui.unit.dp
import com.example.mitego.ui.theme.ForestGreen
>>>>>>> 0f8a79eccb4579cba4ceeea3a5fbad3eed57fda4
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
<<<<<<< HEAD
            .background(Color.White)
            .clickable { onNavigateToMap() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(120.dp))

            // Logo
            Image(
                painter = painterResource(id = R.drawable.logo_cacamites_petit),
                contentDescription = "CaçaMites Logo",
                modifier = Modifier
                    .size(width = 196.dp, height = 219.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))

            // CaçaMites Title estilitzat
            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = Color(0xFFEC6209),
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = Merienda
                        )
                    ) { append("Caça") }
                    withStyle(
                        style = SpanStyle(
                            color = Color(0xFF202020),
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = Merienda
                        )
                    ) { append("Mites") }
                },
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Explora, descobreix i viu les històries del territori",
                color = Color.Black,
                textAlign = TextAlign.Center,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
            
            Spacer(modifier = Modifier.weight(1f))

            val uriHandler = androidx.compose.ui.platform.LocalUriHandler.current
            Text(
                text = "Crèdits / Edició Beta",
                color = Color.Gray,
                textAlign = TextAlign.Center,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .clickable { uriHandler.openUri("https://krei24.com/mitego/") }
                    .padding(16.dp)
            )
            
            Spacer(modifier = Modifier.height(30.dp))
        }
=======
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
>>>>>>> 0f8a79eccb4579cba4ceeea3a5fbad3eed57fda4
    }
}
