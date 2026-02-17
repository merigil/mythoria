package com.example.mitego.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mitego.ui.theme.Merienda

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopAppBar(
    onBackClick: (() -> Unit)? = null,
    onMenuClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val usabilityFormUrl = "https://docs.google.com/forms/d/e/1FAIpQLSdrkxLJUjj1AFmGqFOIFcr5m6yMN9ix-SiaMdmfVz05qNRGnw/viewform"

    TopAppBar(
        title = {
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(
                        color = Color(0xffec6209),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = Merienda)) { append("Caça") }
                    withStyle(style = SpanStyle(
                        color = Color(0xff202020),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = Merienda)) { append("Mites") }
                }
            )
        },
        navigationIcon = {
            if (onBackClick != null) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Torna",
                        tint = Color(0xff202020),
                        modifier = Modifier.requiredSize(size = 25.dp)
                    )
                }
            }
        },
        actions = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Text "Test" amb l'enllaç
                Text(
                    text = "Test",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xff202020)
                    ),
                    modifier = Modifier
                        .clickable {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(usabilityFormUrl))
                            context.startActivity(intent)
                        }
                        .padding(horizontal = 8.dp)
                )
                
                // Icona de tres punts (sense enllaç, crida onMenuClick si es passa)
                IconButton(onClick = onMenuClick) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "Més opcions",
                        tint = Color(0xff202020)
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White
        ),
        modifier = modifier.shadow(elevation = 2.dp)
    )
}
