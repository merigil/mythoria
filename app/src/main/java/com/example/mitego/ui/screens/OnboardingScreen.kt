package com.example.mitego.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

// Simple placeholder for R.drawable resources that are missing
// In a real app, these would be proper resources.
// Using Icons or Boxes instead.

@Composable
fun OnboardingScreen(
    onFinishOnboarding: () -> Unit,
    onSkip: () -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { 3 })
    val scope = rememberCoroutineScope()

    HorizontalPager(
        state = pagerState,
        modifier = Modifier.fillMaxSize().background(Color.White)
    ) { page ->
        OnboardingPageContent(
            pageIndex = page,
            onNext = {
                if (page < 2) {
                    scope.launch { pagerState.animateScrollToPage(page + 1) }
                } else {
                    onFinishOnboarding()
                }
            },
            onSkip = onSkip,
            isLastPage = page == 2
        )
    }
}

@Composable
fun OnboardingPageContent(
    pageIndex: Int,
    onNext: () -> Unit,
    onSkip: () -> Unit,
    isLastPage: Boolean
) {
    // Adapted from user provided "Onboarding1" code
    // Since we don't have the specific assets (R.drawable.group, etc.),
    // we use placeholders but keep the structure and text.
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        // TOP ILLUSTRATION AREA (Placeholder for the complex Group images)
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 50.dp)
                .size(360.dp, 300.dp)
                .background(Color(0xFFE0F7FA), RoundedCornerShape(16.dp)) // Light Cyan placeholder
        ) {
            Text(
                text = "Il·lustració Pàgina ${pageIndex + 1}",
                modifier = Modifier.align(Alignment.Center),
                color = Color(0xFF006064)
            )
            // Simulating decorative elements
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = (-20).dp, y = 20.dp)
                    .size(60.dp)
                    .background(Color(0xFFFFCC80), CircleShape)
            )
        }

        // PAGER INDICATORS (Approximate position from design: y=335dp)
        Row(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = (-50).dp), // Adjusting visually
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            repeat(3) { index ->
                Box(
                    modifier = Modifier
                        .size(14.dp)
                        .clip(CircleShape)
                        .background(
                            if (index == pageIndex) Color(0xff0b94fe) else Color(0xffdcdcdc)
                        )
                )
            }
        }

        // TITLE
        Text(
            text = when(pageIndex) {
                0 -> "Funcionament bàsic del joc"
                1 -> "Explora el mapa"
                else -> "Aconsegueix recompenses"
            },
            color = Color.Black,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge.copy(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = 0.dp)
                .padding(horizontal = 24.dp)
        )

        // DESCRIPTION
        Text(
            text = when(pageIndex) {
                0 -> "Camina el territori, desperta llegendes i ajuda els protagonistes: cada pas obre nous punts d’interès, objectes ocults i històries vives del lloc que explores."
                1 -> "Navega pel mapa per trobar les teves properes aventures. La ubicació és clau per desbloquejar els secrets amagats."
                else -> "Completa els reptes per guanyar punts i col·leccionables únics. Converteix-te en un expert caçallegendes!"
            },
            color = Color.Black,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = 60.dp)
                .padding(horizontal = 32.dp)
        )
        
        // DETAILED LIST (Only on page 0 based on design, but keeping structure)
        if (pageIndex == 0) {
             Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(
                        color = Color.Black,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold)) {append("Com funciona el joc (en pocs passos)\n\n")}
                    withStyle(style = SpanStyle(
                        color = Color.Black,
                        fontSize = 12.sp)) {append("Ves al punt d’inici per activar la llegenda.\nEs desbloquegen els punts d’interès del recorregut.\nExplora l’entorn i troba diamants per avançar en la història.\nRecull objectes i pistes amagades.\nAjuda els protagonistes, resol els reptes i guanya punts per pujar al rànquing de caçallegendes.")}
                },
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(y = 180.dp)
                    .padding(horizontal = 24.dp)
            )
        }

        // TOOLTIP / HELP (Trobador)
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(bottom = 120.dp, start = 25.dp)
                .width(320.dp)
                .height(60.dp)
                .shadow(4.dp, RoundedCornerShape(8.dp))
                .background(Color.White, RoundedCornerShape(8.dp))
                .clickable { /* Help action */ }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color(0xff0b94fe), RoundedCornerShape(4.dp)),
                    contentAlignment = Alignment.Center
                ) {
                   Icon(
                       imageVector = Icons.Default.Face,
                       contentDescription = "Face",
                       tint = Color.White
                   )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Recorre al Trobador si necesites ajuda",
                    color = Color(0xff0b94fe),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                 Spacer(modifier = Modifier.width(8.dp))
                 Icon(Icons.Default.ArrowForward, contentDescription = null, tint = Color(0xff0b94fe), modifier = Modifier.size(16.dp))
            }
        }

        // BUTTONS (Surt / Seguent)
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 40.dp, start = 24.dp, end = 24.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // SURT Button
            Surface(
                modifier = Modifier
                    .width(150.dp)
                    .height(50.dp)
                    .shadow(4.dp, RoundedCornerShape(8.dp))
                    .clickable { onSkip() },
                shape = RoundedCornerShape(8.dp),
                color = Color.White,
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xff0b94fe))
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "SURT",
                        color = Color(0xff0b94fe),
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp
                    )
                }
            }
            
            // SEGUENT Button
            Surface(
                modifier = Modifier
                    .width(150.dp)
                    .height(50.dp)
                    .shadow(4.dp, RoundedCornerShape(8.dp))
                    .clickable { onNext() },
                shape = RoundedCornerShape(8.dp),
                color = Color(0xff0b94fe)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = if (isLastPage) "COMENÇAR" else "SEGUENT",
                        color = Color.White,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}
