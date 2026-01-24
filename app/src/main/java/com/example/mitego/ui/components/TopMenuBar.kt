package com.example.mitego.ui.components

import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
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
            IconButton(onClick = onMenuClick) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Més opcions",
                    tint = Color(0xff202020)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White
        ),
        modifier = modifier.shadow(elevation = 2.dp)
    )
}
