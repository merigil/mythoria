package com.example.mitego.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mitego.ui.theme.ForestGreen
import com.example.mitego.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginClick: () -> Unit
) {
    val AppColors_color_Black_60 = Color(0x99000000)
    val primaryBlue = Color(0xff0b94fe)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        // Logo
        Image(
            painter = painterResource(id = R.drawable.logo_cacamites_petit),
            contentDescription = "CaçaMites Logo",
            modifier = Modifier
                .align(alignment = Alignment.TopCenter)
                .offset(y = 100.dp)
                .requiredSize(width = 180.dp, height = 100.dp)
        )

        // Title: CaçaMites
        Text(
            textAlign = TextAlign.Center,
            lineHeight = 30.sp,
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(
                    color = Color(0xfff17002),
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = com.example.mitego.ui.theme.Merienda)) { append("Caça") }
                withStyle(style = SpanStyle(
                    color = Color(0xff202020),
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = com.example.mitego.ui.theme.Merienda)) { append("Mites") }
            },
            modifier = Modifier
                .align(alignment = Alignment.TopCenter)
                .offset(y = 210.dp)
                .requiredWidth(width = 411.dp)
                .requiredHeight(height = 48.dp)
        )

        // Input: Nom
        Box(
            contentAlignment = Alignment.CenterStart,
            modifier = Modifier
                .align(alignment = Alignment.TopCenter)
                .offset(y = 300.dp)
                .requiredWidth(width = 300.dp)
                .requiredHeight(height = 64.dp)
                .clip(shape = RoundedCornerShape(8.dp))
                .background(color = Color(0xFFF5F5F5))
        ) {
             Text(
                text = "Nom",
                color = AppColors_color_Black_60,
                style = TextStyle(fontSize = 14.sp),
                modifier = Modifier.padding(start = 20.dp)
            )
        }

        // Input: Password
        Box(
            contentAlignment = Alignment.CenterStart,
            modifier = Modifier
                .align(alignment = Alignment.TopCenter)
                .offset(y = 380.dp)
                .requiredWidth(width = 300.dp)
                .requiredHeight(height = 64.dp)
                .clip(shape = RoundedCornerShape(8.dp))
                .background(color = Color(0xFFF5F5F5))
        ) {
             Text(
                text = "Password",
                color = AppColors_color_Black_60,
                style = TextStyle(fontSize = 14.sp),
                modifier = Modifier.padding(start = 20.dp)
            )
             Image(
                imageVector = Icons.Default.Visibility,
                contentDescription = "visibility",
                colorFilter = ColorFilter.tint(primaryBlue),
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 16.dp)
                    .requiredSize(size = 20.dp)
            )
        }

        // Recuperar contrasenya
        Text(
            text = "Recuperar contrasenya",
            color = Color.Gray,
            textAlign = TextAlign.End,
            style = TextStyle(fontSize = 12.sp),
            modifier = Modifier
                .align(alignment = Alignment.TopCenter)
                .offset(y = 450.dp)
                .requiredWidth(300.dp)
        )

        // Button: ENTRAR
        Surface(
            color = primaryBlue,
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .align(alignment = Alignment.TopCenter)
                .offset(y = 520.dp)
                .requiredWidth(300.dp)
                .requiredHeight(50.dp)
                .clickable { onLoginClick() }
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = "ENTRAR",
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        fontSize = 16.sp, // Augmentada a 16.sp
                        fontWeight = FontWeight.Black // Canviat a Black per a màxim gruix
                    )
                )
            }
        }

        // Button: REGISTRA'T
        Surface(
            color = Color.White,
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(2.dp, primaryBlue), // Augmentat a 2.dp per visibilitat
            modifier = Modifier
                .align(alignment = Alignment.TopCenter)
                .offset(y = 600.dp)
                .requiredWidth(300.dp)
                .requiredHeight(50.dp)
        ) {
             Box(contentAlignment = Alignment.Center) {
                Text(
                    text = "REGISTRA’T",
                    color = primaryBlue,
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        fontSize = 16.sp, // Augmentada a 16.sp
                        fontWeight = FontWeight.Black // Canviat a Black per a màxim gruix
                    )
                )
            }
        }

        // Social Media Placeholder
        Text(
            text = "O connecta't amb",
            color = Color.Gray,
            textAlign = TextAlign.Center,
            style = TextStyle(fontSize = 14.sp),
            modifier = Modifier
                .align(alignment = Alignment.TopCenter)
                .offset(y = 700.dp)
        )

        // Footer Text
        Text(
            text = "Condicions d’ús i política de privacitat",
            color = Color.Gray,
            textAlign = TextAlign.Center,
            style = TextStyle(fontSize = 14.sp),
            modifier = Modifier
                .align(alignment = Alignment.BottomCenter)
                .padding(bottom = 40.dp)
                .fillMaxWidth()
        )
    }
}
