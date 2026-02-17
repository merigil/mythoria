package com.example.mitego.ui.screens

import com.example.mitego.R
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
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
    onSkip: () -> Unit,
    onNavigateToTrobador: () -> Unit
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
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        // SCROLLABLE CONTENT
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(bottom = 110.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // TOP ILLUSTRATION AREA
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

            // CONTENT COLUMN
            Column(
                modifier = Modifier.padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // TITLE (BLACK 900)
                Text(
                    text = when(pageIndex) {
                        0 -> "Sigues benvinguda o benvingut,\ncaçadora o caçador!"
                        1 -> "Juga al món real, amb seguretat"
                        else -> "Categories i punts"
                    },
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontSize = 20.sp,
                        fontWeight = FontWeight(900),
                        lineHeight = 26.sp
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // MAIN CONTENT
                when (pageIndex) {
                    0 -> {
                        // Introducció ressaltada amb fons
                        Surface(
                            color = Color(0xFF0B94FE).copy(alpha = 0.05f),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Camina pel territori, desperta llegendes i ajuda els seus protagonistes: cada pas obre nous punts d’interès, reptes i històries vives del lloc que explores, amb experiències uniques que fas teves. Coneix la llegenda i incorpora-la al teu llibre, guanya punts per pujar al rànquing de Caça Mites.",
                                color = Color.Black,
                                modifier = Modifier.padding(16.dp),
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight(900),
                                    lineHeight = 20.sp
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = buildAnnotatedString {
                                withStyle(style = SpanStyle(fontWeight = FontWeight(900), fontSize = 16.sp)) {
                                    append("Passos del joc\n\n")
                                }
                                
                                withStyle(style = SpanStyle(fontWeight = FontWeight(900), fontSize = 14.sp, color = Color(0xFF0B94FE))) {
                                    append("• Desperta la llegenda: ")
                                }
                                withStyle(style = SpanStyle(fontWeight = FontWeight(700), fontSize = 13.sp)) {
                                    append("Arriba al punt sagrat i inicia l’aventura: el camí s’obre, els llocs revelen secrets i cada pas et guia entre pistes, objectes i proves d’una història única.\n\n")
                                }
                                
                                withStyle(style = SpanStyle(fontWeight = FontWeight(900), fontSize = 14.sp, color = Color(0xFF0B94FE))) {
                                    append("• Escolta i fes-la teva: ")
                                }
                                withStyle(style = SpanStyle(fontWeight = FontWeight(700), fontSize = 13.sp)) {
                                    append("Deixa’t portar per la veu de La Trobadora, llegeix o hears el relat i incorpora la llegenda al teu llibre, perquè ja formi part de tu.")
                                }
                            },
                            color = Color.Black,
                            textAlign = TextAlign.Start,
                            lineHeight = 18.sp,
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp)
                        )
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        // Trobador Help Button
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp)
                                .shadow(4.dp, RoundedCornerShape(8.dp))
                                .background(Color.White, RoundedCornerShape(8.dp))
                                .clickable { onNavigateToTrobador() }
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(36.dp)
                                        .background(Color(0xff0b94fe), RoundedCornerShape(4.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.Face, null, tint = Color.White)
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "Recorre al Trobador si necesites ajuda",
                                    color = Color(0xff0b94fe),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight(900)
                                )
                                Spacer(modifier = Modifier.weight(1f))
                                Icon(Icons.Default.ArrowForward, null, tint = Color(0xff0b94fe), modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                    1 -> {
                        Column(horizontalAlignment = Alignment.Start) {
                            // Introducció ressaltada (estil pàgina 1)
                            Surface(
                                color = Color(0xFF0B94FE).copy(alpha = 0.05f),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = buildAnnotatedString {
                                        append("L’app utilitza la teva ubicació real per desbloquejar continguts només quan arribes als llocs indicats.\n\n")
                                        append("No tot és visible des del principi: explorar forma part del misteri i del joc.\n\n")
                                        withStyle(style = SpanStyle(color = Color(0xFF0B94FE))) {
                                            append("La geolocalització és clau per a l’experiència, activa-la!")
                                        }
                                    },
                                    color = Color.Black,
                                    modifier = Modifier.padding(16.dp),
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight(900),
                                        lineHeight = 20.sp
                                    )
                                )
                            }
                            
                            Spacer(modifier = Modifier.height(20.dp))

                            // Secció d'advertència corregida
                            Row(verticalAlignment = Alignment.Top, modifier = Modifier.padding(horizontal = 4.dp)) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_launcher), // Fixed: changed from ic_launcher_foreground to ic_launcher
                                    contentDescription = null,
                                    tint = Color.Black, 
                                    modifier = Modifier.size(23.dp).padding(top = 2.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = "Totes les rutes es basen en localitzacions reals. Cal preparar-se com quan vas d’excursió: roba adequada, aigua, bateria suficient i atenció a l’entorn.",
                                    color = Color.Black,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight(700)
                                    ),
                                    lineHeight = 16.sp
                                )
                            }

                            Spacer(modifier = Modifier.height(20.dp))
                            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color.LightGray))
                            Spacer(modifier = Modifier.height(16.dp))

                            // Simplified InfoItems for Page 2
                            Text("• No compartim la teva ubicació amb altres usuaris.", fontWeight = FontWeight(700), fontSize = 13.sp)
                            Text("• Rebràs avisos de seguretat i recomanacions durant la ruta.", fontWeight = FontWeight(700), fontSize = 13.sp)
                            Text("• Respecta la natura, la pagesia i l’entorn per on passes.", fontWeight = FontWeight(700), fontSize = 13.sp)
                        }
                    }
                    2 -> {
                        Column(horizontalAlignment = Alignment.Start) {
                            Surface(
                                color = Color(0xFF0B94FE).copy(alpha = 0.05f),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "A Caça Mites volem crear un sistema de rànquings que abraci tots els Països Catalans.\n\n" +
                                            "Les puntuacions es comptabilitzaran segons la manera de jugar —sol, en grup, amb mainada (fins a 12 anys) o en rutes accessibles—, així com per comarca, fins a arribar als millors Caça Mites de tot el territori.",
                                    color = Color.Black,
                                    modifier = Modifier.padding(16.dp),
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight(700),
                                        lineHeight = 20.sp
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(24.dp))

                            Text(
                                text = buildAnnotatedString {
                                    withStyle(style = SpanStyle(fontWeight = FontWeight(900), fontSize = 16.sp)) {
                                        append("Punts del mapa\n\n")
                                    }
                                    withStyle(style = SpanStyle(fontWeight = FontWeight(900), fontSize = 14.sp, color = Color(0xFF0B94FE))) {
                                        append("• +1 punt: ")
                                    }
                                    withStyle(style = SpanStyle(fontWeight = FontWeight(700), fontSize = 13.sp)) {
                                        append("zona urbana o accés fàcil\n")
                                    }
                                    withStyle(style = SpanStyle(fontWeight = FontWeight(900), fontSize = 14.sp, color = Color(0xFF0B94FE))) {
                                        append("• +2 punts: ")
                                    }
                                    withStyle(style = SpanStyle(fontWeight = FontWeight(700), fontSize = 13.sp)) {
                                        append("muntanya, ruta llarga o poc senyalitzada\n\n")
                                    }

                                    withStyle(style = SpanStyle(fontWeight = FontWeight(900), fontSize = 16.sp)) {
                                        append("Maneres de jugar\n\n")
                                    }
                                    withStyle(style = SpanStyle(fontWeight = FontWeight(700), fontSize = 13.sp)) {
                                        append("• Sol: al teu ritme\n")
                                        append("• En grup: compartint punts\n")
                                        append("• Amb infants (fins a 12 anys)\n\n")
                                    }

                                    withStyle(style = SpanStyle(fontWeight = FontWeight(900), fontSize = 16.sp)) {
                                        append("Rutes accessibles\n\n")
                                    }
                                    withStyle(style = SpanStyle(fontWeight = FontWeight(700), fontSize = 13.sp)) {
                                        append("• Mateixos punts que la resta\n")
                                        append("• Pensades perquè tothom hi pugui arribar\n")
                                        append("• Formen part d’un rànquing inclusiu propi")
                                    }
                                },
                                color = Color.Black,
                                textAlign = TextAlign.Start,
                                lineHeight = 18.sp,
                                modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp)
                            )

                            Spacer(modifier = Modifier.height(24.dp))

                            Surface(
                                color = Color(0xFFFF9800).copy(alpha = 0.08f),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "Però anem a poc a poc. Actualment estàs jugant a la beta 1.0 (2026), encara en fase de proves. En aquesta versió només es tenen en compte les puntuacions segons la manera de jugar i la comarca.",
                                    color = Color.Black,
                                    modifier = Modifier.padding(16.dp),
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight(700),
                                        lineHeight = 20.sp
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }

        // BUTTONS AREA
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
                    border = BorderStroke(2.dp, Color(0xff0b94fe))
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "SURT",
                            color = Color(0xff0b94fe),
                            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight(900))
                        )
                    }
                }
                
                Surface(
                    modifier = Modifier
                        .width(150.dp)
                        .height(50.dp)
                        .clickable { onNext() },
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xff0b94fe)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = if (isLastPage) "COMENÇA" else "SEGUENT",
                            color = Color.White,
                            style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight(900))
                        )
                    }
                }
            }
        }
    }
}
