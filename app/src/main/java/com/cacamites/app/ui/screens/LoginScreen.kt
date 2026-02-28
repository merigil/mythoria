package com.cacamites.app.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cacamites.app.R
import com.cacamites.app.ui.theme.Merienda
import com.cacamites.app.ui.theme.OpenSans

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginClick: () -> Unit
) {
    val grayTextColor = Color(0xFF888888) // L'estil de "A Osona"
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

        // Title: CaçaMites (Merienda Font - Es manté com a títol)
        Text(
            textAlign = TextAlign.Center,
            lineHeight = 30.sp,
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(
                    color = Color(0xfff17002),
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = Merienda)) { append("Caça") }
                withStyle(style = SpanStyle(
                    color = Color(0xff202020),
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = Merienda)) { append("Mites") }
            },
            modifier = Modifier
                .align(alignment = Alignment.TopCenter)
                .offset(y = 210.dp)
                .requiredWidth(width = 411.dp)
                .requiredHeight(height = 48.dp)
        )

        // Input: Nom (Estil "A Osona")
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
                color = grayTextColor,
                style = TextStyle(
                    fontFamily = OpenSans,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                ),
                modifier = Modifier.padding(start = 20.dp)
            )
        }

        // Input: Password (Estil "A Osona")
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
                color = grayTextColor,
                style = TextStyle(
                    fontFamily = OpenSans,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                ),
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

        // Recuperar contrasenya (Estil "A Osona")
        Text(
            text = "Recuperar contrasenya",
            color = grayTextColor,
            textAlign = TextAlign.End,
            style = TextStyle(
                fontFamily = Montserrat,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            ),
            modifier = Modifier
                .align(Alignment.TopCenter)
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
                        fontFamily = OpenSans,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black
                    )
                )
            }
        }

        // Footer Text (Estil "A Osona")
        Text(
            text = "Condicions d’ús i política de privacitat",
            color = grayTextColor,
            textAlign = TextAlign.Center,
            style = TextStyle(
                fontFamily = Montserrat,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            ),
            modifier = Modifier
                .align(alignment = Alignment.BottomCenter)
                .padding(bottom = 40.dp)
                .fillMaxWidth()
        )
    }
}
