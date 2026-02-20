package com.cacamites.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.CenterAlignedTopAppBar // Using as button container per design?
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface // Alternative to Scaffold for partial usage
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
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import com.cacamites.app.ui.theme.ForestGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginClick: () -> Unit
) {
    // Colors from design
    val AppColors_color_Black_60 = Color(0x99000000)
    val helpText16019 = false // Defaulting to false as logic isn't provided

    Box(
        modifier = Modifier
            .requiredWidth(width = 411.dp)
            .requiredHeight(height = 980.dp)
            .background(color = Color.White)
    ) {
        // Logo / Circle Background
        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 132.dp, y = 62.dp)
                .requiredWidth(width = 151.dp)
                .requiredHeight(height = 152.dp)
                .clip(shape = CircleShape)
                .background(color = Color(0xff0b94fe))
        )
        
        // Component 2 (Logo Icon Placeholder)
        // Image(painter = painterResource(id = R.drawable.component2), ... )
        // Replacing with simple box/text for placeholder
        Text(
            text = "Logo",
            color = Color.White,
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 163.dp, y = 120.dp) // Adjusted y to center in circle
        )

        // Title: CaçaMites
        Text(
            textAlign = TextAlign.Center,
            lineHeight = 40.sp, // Adjusted from 5.sp which seemed too small
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(
                    color = Color(0xfff17002),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Black)) { append("Caça") }
                withStyle(style = SpanStyle(
                    color = Color(0xff202020),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Black)) { append("Mites") }
            },
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 0.dp, y = 230.dp)
                .requiredWidth(width = 415.dp)
                .requiredHeight(height = 48.dp)
        )

        // Input: Nom
        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 24.dp, y = 309.dp)
                .requiredWidth(width = 363.dp)
                .requiredHeight(height = 76.dp)
                .clip(shape = RoundedCornerShape(5.dp))
                .background(color = Color.White)
                .shadow(2.dp) // Adding shadow since Scaffold was removed
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                 Text(
                    text = "Nom",
                    color = AppColors_color_Black_60,
                    style = TextStyle(fontSize = 16.sp)
                )
                // Placeholder for actual input functionality
            }
        }

        // Input: Password
        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 24.dp, y = 437.dp)
                .requiredWidth(width = 363.dp)
                .requiredHeight(height = 76.dp)
                .clip(shape = RoundedCornerShape(5.dp))
                .background(color = Color.White)
                 .shadow(2.dp)
        ) {
             Column(modifier = Modifier.padding(16.dp)) {
                 Text(
                    text = "Password",
                    color = AppColors_color_Black_60,
                    style = TextStyle(fontSize = 16.sp)
                )
            }
             Image(
                imageVector = Icons.Default.Visibility,
                contentDescription = "visibility",
                colorFilter = ColorFilter.tint(Color(0xff0b94fe)),
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 16.dp)
                    .requiredSize(size = 24.dp)
            )
        }

        // Button: ENTRAR
        // The design used CenterAlignedTopAppBar, which is strange for a button,
        // but likely due to text centering. Converting to a Surface/Box for better semantics
        // but keeping the visual properties.
        Surface(
            color = Color(0xff0b94fe), // Primary Blue
            shadowElevation = 4.dp,
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 24.dp, y = 574.dp)
                .requiredWidth(363.dp) // Assuming width from other inputs as it wasn't specified but needed
                .requiredHeight(50.dp) // Approximated height
                .clickable { onLoginClick() } // Added Click Action
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = "ENTRAR",
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        }

        // Button: REGISTRA'T
        Surface(
            color = Color.White,
            shadowElevation = 4.dp,
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 24.dp, y = 697.dp)
                .requiredWidth(363.dp)
                .requiredHeight(50.dp)
        ) {
             Box(contentAlignment = Alignment.Center) {
                Text(
                    text = "REGISTRA’T",
                    color = Color(0xff0b94fe),
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        }

        // Footer Text
        Text(
            text = "Condicions d’ús i política de privacitat",
            color = Color.Black,
            textAlign = TextAlign.Center,
            style = TextStyle(fontSize = 10.sp),
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = (-2).dp, y = 886.dp)
                .requiredWidth(width = 411.dp)
                .requiredHeight(height = 57.dp)
                .wrapContentHeight(align = Alignment.CenterVertically)
        )

        Text(
            text = "Recuperar contrasenya",
            color = Color.Black,
            textAlign = TextAlign.End,
            style = TextStyle(fontSize = 10.sp),
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 24.dp, y = 628.dp)
                .requiredWidth(width = 363.dp)
                .requiredHeight(height = 57.dp)
                .wrapContentHeight(align = Alignment.CenterVertically)
        )

        // Social Media Placeholder
        Box(
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 67.dp, y = 799.dp)
                .requiredWidth(width = 274.dp)
                .requiredHeight(height = 60.dp)
                .background(color = Color(0xffdcdcdc))
        )
        Text(
            text = "Xarxes socials",
            color = Color.Black,
            textAlign = TextAlign.Center,
            style = TextStyle(fontSize = 16.sp),
            modifier = Modifier
                .align(alignment = Alignment.TopStart)
                .offset(x = 68.dp, y = 800.dp)
                .requiredWidth(width = 272.dp)
                .requiredHeight(height = 57.dp)
                .wrapContentHeight(align = Alignment.CenterVertically)
        )
    }
}

