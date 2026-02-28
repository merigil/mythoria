package com.cacamites.app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.cacamites.app.R
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.text.googlefonts.Font as GFont

// Colors personalitzats
val DarkCharcoal = Color(0xFF333333)
val GrayText = Color(0xFF666666)
val DefaultDarkText = Color(0xFF202020)

// Google Fonts Provider
val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

val OpenSansFont = GoogleFont("Open Sans")

val OpenSans = FontFamily(
    GFont(googleFont = OpenSansFont, fontProvider = provider),
    GFont(googleFont = OpenSansFont, fontProvider = provider, weight = FontWeight.Bold),
    GFont(googleFont = OpenSansFont, fontProvider = provider, weight = FontWeight.ExtraBold)
)

// Alias Montserrat per a compatibilitat total
val Montserrat = OpenSans

val Merienda = FontFamily(
    Font(R.font.merienda)
)

val Typography = Typography(
    headlineLarge = TextStyle(
        fontFamily = Merienda,
        fontWeight = FontWeight.Black,
        fontSize = 32.sp,
        color = DefaultDarkText
    ),
    headlineMedium = TextStyle(
        fontFamily = Merienda,
        fontWeight = FontWeight.Black,
        fontSize = 28.sp,
        color = DefaultDarkText
    ),
    titleLarge = TextStyle(
        fontFamily = OpenSans,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp,
        color = DefaultDarkText
    ),
    bodyLarge = TextStyle(
        fontFamily = OpenSans,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        color = DefaultDarkText
    ),
    bodyMedium = TextStyle(
        fontFamily = OpenSans,
        fontWeight = FontWeight.Bold,
        fontSize = 14.sp,
        color = DefaultDarkText
    )
)
