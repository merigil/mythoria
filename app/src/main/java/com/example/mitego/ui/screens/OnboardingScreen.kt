package com.example.mitego.ui.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Face
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
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
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        // TOP ILLUSTRATION AREA
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 50.dp)
                .size(360.dp, 300.dp)
                .background(Color(0xFFE0F7FA), RoundedCornerShape(16.dp))
        ) {
            Text(
                text = "Il·lustració Pàgina ${pageIndex + 1}",
                modifier = Modifier.align(Alignment.Center),
                color = Color(0xFF006064)
            )
        }

        // PAGER INDICATORS
        Row(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = (-50).dp),
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
                0 -> "Camina el territori, desperta llegendes i ajuda els protagonistes."
                1 -> "Navega pel mapa per trobar les teves properes aventures."
                else -> "Completa els reptes per guanyar punts i col·leccionables."
            },
            color = Color.Black,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = 60.dp)
                .padding(horizontal = 32.dp)
        )

        // BUTTONS
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 40.dp, start = 24.dp, end = 24.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Surface(
                modifier = Modifier
                    .width(150.dp)
                    .height(50.dp)
                    .shadow(4.dp, RoundedCornerShape(8.dp))
                    .clickable { onSkip() },
                shape = RoundedCornerShape(8.dp),
                color = Color.White
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(text = "SURT", color = Color(0xff0b94fe))
                }
            }
            
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
                    Text(text = if (isLastPage) "COMENÇAR" else "SEGUENT", color = Color.White)
                }
            }
        }
    }
}
