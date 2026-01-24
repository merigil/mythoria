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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Accessible
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.EscalatorWarning
import androidx.compose.material.icons.filled.ExploreOff
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Face3
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Hiking
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Landscape
import androidx.compose.material.icons.filled.SentimentVeryDissatisfied
import androidx.compose.material.icons.filled.SettingsRemote
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Terrain
import androidx.compose.material.icons.filled.Warning
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
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
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
            contentDescription = "Il·lustració Pàgina ${pageIndex + 1}",
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 20.dp)
                .size(324.dp, 270.dp)
        )

        // CONTENT COLUMN
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .offset(y = 310.dp)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // TITLE
            Text(
                text = when(pageIndex) {
                    0 -> "Sigues benvinguda o benvingut,\ncaçadora o caçador!"
                    1 -> "Juga al món real, amb seguretat"
                    else -> "Categories, punts i premis"
                },
                color = Color.Black,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Black
                )
            )

            Spacer(modifier = Modifier.height(10.dp))

            // MAIN CONTENT
            when (pageIndex) {
                0 -> {
                    Text(
                        text = "Camina pel territori, desperta llegendes i ajuda les protagonistes i els protagonistes: cada pas obre nous punts d’interès, objectes ocults i històries vives del lloc que explores, amb experiències uniques locals.",
                        color = Color.Black,
                        textAlign = TextAlign.Start,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        ),
                        lineHeight = 19.sp,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Black, fontSize = 12.sp)) {
                                append("Com funciona el joc (en pocs passos):\n")
                            }
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontSize = 12.sp)) {
                                append("1. Ves al punt d’inici per activar la llegenda.\n")
                                append("2. Es desbloquegen els punts d’interès del recorregut.\n")
                                append("3. Explora l’entorn i troba diamants per avançar en la història.\n")
                                append("4. Recull objectes i pistes amagades.\n")
                                append("5. Parla amb les protagonistes i els protagonistes, resol els reptes i guanya punts per pujar al rànquing de caçallegendes.")
                            }
                        },
                        color = Color.Black,
                        textAlign = TextAlign.Start,
                        lineHeight = 16.sp,
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp)
                    )
                    
                    Spacer(modifier = Modifier.height(20.dp))
                    
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
                                Icon(
                                    imageVector = Icons.Default.Face,
                                    contentDescription = "Face",
                                    tint = Color.White
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Recorre al Trobador si necesites ajuda",
                                color = Color(0xff0b94fe),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Black
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            Icon(Icons.Default.ArrowForward, contentDescription = null, tint = Color(0xff0b94fe), modifier = Modifier.size(16.dp))
                        }
                    }
                }
                1 -> {
                    Column(horizontalAlignment = Alignment.Start) {
                        Text(
                            text = buildAnnotatedString {
                                append("L’app utilitza la teva ubicació real per desbloquejar continguts només quan arribes als llocs indicats.\n")
                                append("No tot és visible des del principi: explorar forma part del misteri i del joc.\n\n")
                                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                                    append("La geolocalització és clau per a l’experiència, activa-la!")
                                }
                            },
                            color = Color.Black,
                            textAlign = TextAlign.Start,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            ),
                            lineHeight = 17.sp
                        )
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color.LightGray))
                        Spacer(modifier = Modifier.height(12.dp))

                        Row(verticalAlignment = Alignment.Top) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = null,
                                tint = Color.Red,
                                modifier = Modifier.size(16.dp).padding(top = 2.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = buildAnnotatedString {
                                    withStyle(style = SpanStyle(fontWeight = FontWeight.Black)) {
                                        append("Alerta important: ")
                                    }
                                    append("Totes les rutes es basen en localitzacions reals. Cal preparar-se com quan vas d’excursió: roba adequada, aigua, bateria suficient i atenció a l’entorn.")
                                },
                                color = Color.Black,
                                textAlign = TextAlign.Start,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                ),
                                lineHeight = 15.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        InfoItem(Icons.Default.SettingsRemote, "No compartim la teva ubicació amb altres usuaris.", Color(0xff0b94fe))
                        Spacer(modifier = Modifier.height(8.dp))
                        InfoItem(Icons.Default.Shield, "Rebràs avisos de seguretat i recomanacions durant la ruta.", Color(0xff0b94fe))
                        Spacer(modifier = Modifier.height(8.dp))
                        InfoItem(Icons.Default.Landscape, "Respecta la natura, la pagesia i l’entorn per on passes.", Color(0xFF4CAF50))
                    }
                }
                2 -> {
                    Column(horizontalAlignment = Alignment.Start) {
                        Text(
                            text = "Què trobaràs pel camí? Aquests son els simbols i què volen dir:",
                            color = Color.Black,
                            textAlign = TextAlign.Start,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            ),
                            lineHeight = 18.sp,
                            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                        )
                        
                        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color.LightGray))
                        Spacer(modifier = Modifier.height(10.dp))

                        val blue = Color(0xff0b94fe)
                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            InfoItem(Icons.Default.Flag, "Punts amb inici de ruta.", blue)
                            InfoItem(Icons.Default.Hiking, "On et trobes: Mostra la teva posició actual.", blue)
                            InfoItem(Icons.Default.Inventory2, "Cofres i caixes: contingut sorpresa.", blue)
                            InfoItem(Icons.Default.Diamond, "Diamants: imprescindibles per acabar la ruta", blue)
                            InfoItem(Icons.Default.Shield, "Objectes: recull-los i utilitza’ls", blue)
                            InfoItem(Icons.Default.Face3, "Personatges: expliquen històries i llegendes", blue)
                            InfoItem(Icons.Default.SentimentVeryDissatisfied, "Monstres i enemics: reptes a superar", Color.Red)
                            InfoItem(Icons.Default.Star, "Punts i rànquings: Classificacions individuals", Color(0xFFFFA000))
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Color.LightGray))
                        Spacer(modifier = Modifier.height(12.dp))

                        // Classification system
                        Text(
                            text = "Cada llegenda disposa d’un sistema de classificació perquè puguis saber si és adequada per a tu:",
                            color = Color.Black,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Black
                            )
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))

                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Row(modifier = Modifier.fillMaxWidth()) {
                                Box(modifier = Modifier.weight(1f)) { InfoItem(Icons.Default.EscalatorWarning, "Apte nens", blue) }
                                Box(modifier = Modifier.weight(1f)) { InfoItem(Icons.Default.Accessible, "Accessible", blue) }
                            }
                            Row(modifier = Modifier.fillMaxWidth()) {
                                Box(modifier = Modifier.weight(1f)) { InfoItem(Icons.Default.ExploreOff, "Sense indicacions", blue) }
                                Box(modifier = Modifier.weight(1f)) { InfoItem(Icons.Default.Terrain, "Difícil", blue) }
                            }
                        }
                    }
                }
            }
        }

        // BUTTONS (Surt / Seguent / Comença)
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 30.dp, start = 24.dp, end = 24.dp)
                .fillMaxWidth(),
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
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black
                        )
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
                        textAlign = TextAlign.Center,
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun InfoItem(icon: ImageVector, text: String, color: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            color = Color.Black,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            ),
            lineHeight = 14.sp
        )
    }
}
