package com.cacamites.app.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(
    onFinishOnboarding: () -> Unit,
    onSkip: () -> Unit,
    onNavigateToTrobador: () -> Unit = {}
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
            onNavigateToTrobador = onNavigateToTrobador,
            isLastPage = page == 2
        )
    }
}

@Composable
fun OnboardingPageContent(
    pageIndex: Int,
    onNext: () -> Unit,
    onSkip: () -> Unit,
    onNavigateToTrobador: () -> Unit,
    isLastPage: Boolean
) {
    val scrollState = rememberScrollState()
    val primaryBlue = Color(0xff0b94fe)
    val primaryOrange = Color(0xFFF17002)
    val darkText = Color(0xFF202020) // El color dels títols

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(bottom = 110.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(
                    id = when (pageIndex) {
                        0 -> R.drawable.onboarding_01
                        1 -> R.drawable.onboarding_02
                        else -> R.drawable.onboarding_03
                    }
                ),
                contentDescription = null,
                modifier = Modifier
                    .padding(top = 20.dp)
                    .size(324.dp, 270.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Column(
                modifier = Modifier.padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // TÍTOL (Merienda + darkText)
                Text(
                    text = stringResource(
                        when (pageIndex) {
                            0 -> R.string.onb_title_0
                            1 -> R.string.onb_title_1
                            else -> R.string.onb_title_2
                        }
                    ),
                    color = darkText,
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        fontFamily = Merienda,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Black,
                        lineHeight = 26.sp
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                when (pageIndex) {
                    0 -> {
                        Surface(
                            color = primaryBlue.copy(alpha = 0.05f),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = stringResource(R.string.onb_p0_intro),
                                color = darkText,
                                modifier = Modifier.padding(16.dp),
                                style = TextStyle(
                                    fontFamily = OpenSans,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    lineHeight = 20.sp
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(fontFamily = OpenSans, fontWeight = FontWeight.Black, fontSize = 16.sp, color = darkText)) {
                                    append(stringResource(R.string.onb_p0_steps_title) + "\n\n")
                                }
                                withStyle(style = SpanStyle(fontFamily = OpenSans, fontWeight = FontWeight.Black, fontSize = 14.sp, color = primaryBlue)) {
                                    append(stringResource(R.string.onb_p0_step1_label))
                                }
                                withStyle(style = SpanStyle(fontFamily = OpenSans, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = darkText)) {
                                    append(stringResource(R.string.onb_p0_step1_body) + "\n\n")
                                }
                                withStyle(style = SpanStyle(fontFamily = OpenSans, fontWeight = FontWeight.Black, fontSize = 14.sp, color = primaryBlue)) {
                                    append(stringResource(R.string.onb_p0_step2_label))
                                }
                                withStyle(style = SpanStyle(fontFamily = OpenSans, fontWeight = FontWeight.Bold, fontSize = 13.sp, color = darkText)) {
                                    append(stringResource(R.string.onb_p0_step2_body))
                                }
                            },
                            textAlign = TextAlign.Start,
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp)
                        )
                    }
                    1 -> {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = stringResource(R.string.onb_p1_intro),
                                style = TextStyle(fontFamily = OpenSans, fontSize = 14.sp, fontWeight = FontWeight.Bold, lineHeight = 22.sp),
                                color = darkText
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            val bulletStyle = TextStyle(fontFamily = OpenSans, fontSize = 13.sp, fontWeight = FontWeight.Bold, lineHeight = 20.sp, color = darkText)
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                                Row(verticalAlignment = Alignment.Top) {
                                    Text("• ", color = primaryBlue, fontWeight = FontWeight.Black, fontSize = 16.sp)
                                    Text(stringResource(R.string.onb_p1_bullet1), style = bulletStyle)
                                }
                                Row(verticalAlignment = Alignment.Top) {
                                    Text("• ", color = primaryBlue, fontWeight = FontWeight.Black, fontSize = 16.sp)
                                    Text(stringResource(R.string.onb_p1_bullet2), style = bulletStyle)
                                }
                                Row(verticalAlignment = Alignment.Top) {
                                    Text("• ", color = primaryBlue, fontWeight = FontWeight.Black, fontSize = 16.sp)
                                    Text(stringResource(R.string.onb_p1_bullet3), style = bulletStyle)
                                }
                            }
                        }
                    }
                    2 -> {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = stringResource(R.string.onb_p2_intro),
                                style = TextStyle(fontFamily = OpenSans, fontSize = 14.sp, fontWeight = FontWeight.Bold, lineHeight = 22.sp),
                                color = darkText
                            )
                            Spacer(modifier = Modifier.height(20.dp))
                            Text(
                                text = stringResource(R.string.onb_p2_points_title),
                                style = TextStyle(fontFamily = Merienda, fontSize = 16.sp, fontWeight = FontWeight.Black),
                                color = primaryBlue
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            val pointBodyStyle = TextStyle(fontFamily = OpenSans, fontSize = 13.sp, fontWeight = FontWeight.Bold, lineHeight = 20.sp, color = darkText)
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.fillMaxWidth()) {
                                Row(verticalAlignment = Alignment.Top) {
                                    Text(stringResource(R.string.onb_p2_point1_label), color = primaryOrange, fontWeight = FontWeight.Black, fontSize = 15.sp)
                                    Text(stringResource(R.string.onb_p2_point1_body), style = pointBodyStyle)
                                }
                                Row(verticalAlignment = Alignment.Top) {
                                    Text(stringResource(R.string.onb_p2_point2_label), color = primaryOrange, fontWeight = FontWeight.Black, fontSize = 15.sp)
                                    Text(stringResource(R.string.onb_p2_point2_body), style = pointBodyStyle)
                                }
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                            // Maneres de jugar
                            Text(
                                text = stringResource(R.string.onb_p2_ways_title),
                                style = TextStyle(fontFamily = Merienda, fontSize = 16.sp, fontWeight = FontWeight.Black),
                                color = primaryBlue
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            val wayStyle = TextStyle(fontFamily = OpenSans, fontSize = 13.sp, fontWeight = FontWeight.Bold, lineHeight = 20.sp, color = darkText)
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp), modifier = Modifier.fillMaxWidth()) {
                                Row(verticalAlignment = Alignment.Top) {
                                    Text("• ", color = primaryBlue, fontWeight = FontWeight.Black, fontSize = 15.sp)
                                    Text(stringResource(R.string.onb_p2_way1), style = wayStyle)
                                }
                                Row(verticalAlignment = Alignment.Top) {
                                    Text("• ", color = primaryBlue, fontWeight = FontWeight.Black, fontSize = 15.sp)
                                    Text(stringResource(R.string.onb_p2_way2), style = wayStyle)
                                }
                                Row(verticalAlignment = Alignment.Top) {
                                    Text("• ", color = primaryBlue, fontWeight = FontWeight.Black, fontSize = 15.sp)
                                    Text(stringResource(R.string.onb_p2_way3), style = wayStyle)
                                }
                            }
                            Spacer(modifier = Modifier.height(20.dp))
                            // Rutes accessibles
                            Surface(
                                color = Color(0xFFE8F5E9),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(12.dp)) {
                                    Text(
                                        text = stringResource(R.string.onb_p2_access_title),
                                        style = TextStyle(fontFamily = Merienda, fontSize = 15.sp, fontWeight = FontWeight.Black),
                                        color = Color(0xFF2E7D32)
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    val accessStyle = TextStyle(fontFamily = OpenSans, fontSize = 13.sp, fontWeight = FontWeight.Bold, lineHeight = 20.sp, color = darkText)
                                    Text(stringResource(R.string.onb_p2_access1), style = accessStyle)
                                    Text(stringResource(R.string.onb_p2_access2), style = accessStyle)
                                }
                            }
                        }
                    }
                }
            }
        }

        // BOTONS
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Color.White)
                .padding(bottom = 30.dp, start = 24.dp, end = 24.dp, top = 10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Surface(
                    modifier = Modifier
                        .width(150.dp)
                        .height(50.dp)
                        .clickable { onSkip() },
                    shape = RoundedCornerShape(8.dp),
                    color = Color.White,
                    border = BorderStroke(2.dp, primaryBlue)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = stringResource(R.string.onb_btn_skip),
                            color = primaryBlue,
                            style = TextStyle(fontFamily = OpenSans, fontSize = 16.sp, fontWeight = FontWeight.Black)
                        )
                    }
                }

                Surface(
                    modifier = Modifier
                        .width(150.dp)
                        .height(50.dp)
                        .clickable { onNext() },
                    shape = RoundedCornerShape(8.dp),
                    color = primaryBlue
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = stringResource(if (isLastPage) R.string.onb_btn_start else R.string.onb_btn_next),
                            color = Color.White,
                            style = TextStyle(fontFamily = OpenSans, fontSize = 16.sp, fontWeight = FontWeight.Black)
                        )
                    }
                }
            }
        }
    }
}
