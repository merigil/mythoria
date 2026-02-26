package com.cacamites.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.CheckBoxOutlineBlank
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import com.cacamites.app.R
import com.cacamites.app.model.GameState
import com.cacamites.app.model.GameStatus
import com.cacamites.app.ui.theme.Merienda
import kotlinx.coroutines.delay

@Composable
fun TrobadorScreen(
    mode: String = "full", // "intro" o "full"
    gameState: GameState,
    onClose: () -> Unit
) {
    val scrollState = rememberScrollState()
    val isSerpent = gameState.legendId == "SERPENT"
    val context = androidx.compose.ui.platform.LocalContext.current

    val titleText = when {
        mode == "intro" -> "Salutacions, viatger!"
        isSerpent -> "La Serpent de Manlleu"
        else -> "La llegenda del Baró de Savassona"
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp)
                .padding(bottom = 110.dp)
        ) {
            // Botó Tancar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Tornar",
                    modifier = Modifier
                        .size(28.dp)
                        .clickable { onClose() },
                    tint = Color.Black
                )
            }

            // Ilustració
            Image(
                painter = painterResource(id = R.drawable.la_trobadora_01),
                contentDescription = "Il·lustració Trobadora",
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 20.dp)
                    .size(280.dp, 240.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Títol
            Text(
                text = titleText,
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.titleLarge.copy(
                    fontFamily = Merienda,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Contingut segons el mode
            if (mode == "intro") {
                Text(
                    text = "Sóc l'Isolda, Trobadora de llegendes, i guardo històries que només els més intrèpids poden descobrir. Vine amb mi i et revelaré la llegenda que estàs explorant, així com el secret que hauràs de seguir per fer-la teva.",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A1A1A)
                    ),
                    lineHeight = 22.sp,
                    modifier = Modifier.fillMaxWidth()
                )

                Divider(modifier = Modifier.padding(vertical = 24.dp), color = Color.LightGray.copy(alpha = 0.5f))

                // TRASLLAT DES DE ONBOARDING
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Aquests son els simbols i què volen dir:",
                        color = Color.Black,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 14.sp,
                            fontWeight = FontWeight(900)
                        ),
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        TrobadorInfoItem(Icons.Default.Flag, "Punts amb inici de ruta.")
                        TrobadorInfoItem(Icons.Default.Hiking, "On et trobes: Mostra la teva posició actual.")
                        TrobadorInfoItem(Icons.Default.Inventory2, "Cofres i caixes: contingut sorpresa.")
                        TrobadorInfoItem(Icons.Default.Face3, "Personatges: expliquen històries i llegendes")
                        TrobadorInfoItem(Icons.Default.SentimentVeryDissatisfied, "Monstres i enemics: reptes a superar")
                    }

                    Divider(modifier = Modifier.padding(vertical = 24.dp), color = Color.LightGray.copy(alpha = 0.5f))

                    Text(
                        text = "Cada llegenda disposa d’un sistema de classificació perquè puguis saber si és adequada per a tu:",
                        color = Color.Black,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 13.sp,
                            fontWeight = FontWeight(900)
                        ),
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                    
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Box(modifier = Modifier.weight(1f)) { TrobadorInfoItem(Icons.Default.Person, "Solitari") }
                            Box(modifier = Modifier.weight(1f)) { TrobadorInfoItem(Icons.Default.Diversity3, "Grups") }
                        }
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Box(modifier = Modifier.weight(1f)) { TrobadorInfoItem(Icons.Default.ChildCare, "0–5") }
                            Box(modifier = Modifier.weight(1f)) { TrobadorInfoItem(Icons.Default.EscalatorWarning, "6–10") }
                        }
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Box(modifier = Modifier.weight(1f)) { TrobadorInfoItem(Icons.Default.Accessible, "Inclusiu") }
                            Box(modifier = Modifier.weight(1f)) { TrobadorInfoItem(Icons.Default.Terrain, "Difícil") }
                        }
                    }
                }

            } else {
                // Legenda amb efecte typewriter
                var textToDisplay by remember { mutableStateOf("") }
                val isWon = gameState.status == GameStatus.WON
                
                val fullText = buildString {
                    if (isSerpent) {
                        if (isWon) {
                            append("Conta la llegenda que… La Serpent no havia estat sempre un monstre, sinó una dona que fou transformada en serp gegantina. Un jove del poble, decidit a alliberar la vila, ideà un pla enginyós utilitzant un morter. La bèstia es va colpejar contra ell fins a morir. La pedra fou recuperada, i el poble quedà lliure del terror.")
                        } else {
                            append("Segueix les pistes: alguns punts i personatges clau són visibles i altres invisibles, però si segueixes el que et van dient, segur que te'n surts i la llegenda serà teva.")
                        }
                    } else {
                        append("Conta la llegenda que….\nAls boscos de Savassona antic,\n regnava un baró d’acer i crit.\n Orgullós, temut per la seva llei,\n més fred que l’hivern, més dur que rei.\nBella dama, d’esperit clar,\n somiava lliure entre murs d’acer.\nPartí a Croada amb ferro i honor;\n al castell, germinà secret amor.")
                        
                        if (gameState.visitedPoints.contains("p_jove")) {
                            append("\n\nEls dos enamorats es trobaven a la balma on brollava la font. Desconfiat, quan el baró tornà de les creuades descobrí allò que la gelosia ja li havia fet témer.")
                        }
                        
                        if (isWon) {
                            append("\n\nCegat per la gelosia, el Baró els va atacar amb la seva espasa. A la balma, va matar la Baronessa i el seu criat, segellant així el seu terrible destí. Diu la tradició que, les nits de lluna plena, es pot veure la imatge de la Baronessa a la lluna.")
                        }
                    }
                }

                LaunchedEffect(fullText) {
                    for (i in 0..fullText.length) {
                        textToDisplay = fullText.substring(0, i)
                        delay(15)
                    }
                }

                Text(
                    text = textToDisplay,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontFamily = Merienda,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1A1A1A)
                    ),
                    lineHeight = 24.sp,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Linia discontínua (efecte paper retallat) més marcada
                Text(
                    text = "- - - - - - - - - - - - - - - - - - - - - - -",
                    color = Color.Black.copy(alpha = 0.4f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    letterSpacing = 2.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Surface(
                    color = Color(0xFFF17002).copy(alpha = 0.15f),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("El repte", style = TextStyle(color = Color.Black, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = if (isWon) "Has completat la llegenda amb èxit!" else "Explora el territori i completa la llegenda. Per aconseguir-ho, hauràs de:",
                            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 15.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1A1A1A)),
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                        
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            if (isSerpent) {
                                ChallengeItem(text = "Escoltar el Pregoner", checked = gameState.visitedPoints.contains("s_pregoner"))
                                ChallengeItem(text = "Trobar el Barquer", checked = gameState.visitedPoints.contains("s_barquer"))
                                ChallengeItem(text = "Parlar amb el Vaquer", checked = gameState.visitedPoints.contains("s_vaquer"))
                                ChallengeItem(text = "Trobar el Rubí", checked = gameState.visitedPoints.contains("s_rubi"))
                                ChallengeItem(text = "Visitar el Terrissaire", checked = gameState.visitedPoints.contains("s_terrissaire"))
                                ChallengeItem(text = "Trobar la Remeiera", checked = gameState.visitedPoints.contains("s_remeiera"))
                                ChallengeItem(text = "Recuperar el Morter", checked = gameState.visitedPoints.contains("s_morter"))
                                ChallengeItem(text = "Resistir a la Plaça", checked = gameState.status == GameStatus.WON)
                            } else {
                                ChallengeItem(text = "Reunir més de 100 punts de força", checked = gameState.totalScore >= 101)
                                ChallengeItem(text = "Trobar l’espasa", checked = gameState.inventory.contains("p_espasa"))
                                ChallengeItem(text = "Parlar amb la Baronessa i el Jove Criat", checked = gameState.visitedPoints.contains("p_baronessa"))
                                ChallengeItem(text = "Buscar el Baró i plantar-li cara", checked = gameState.visitedPoints.contains("p_baro"))
                            }
                        }
                    }
                }
            }
        }

        Box(
            modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth().background(Color.White).padding(horizontal = 24.dp, vertical = 20.dp)
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth().height(50.dp).clickable { onClose() },
                shape = RoundedCornerShape(8.dp),
                color = Color(0xff0b94fe)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(text = "ENTÈS", color = Color(0xFF202020), style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Black))
                }
            }
        }
    }
}

@Composable
fun ChallengeItem(text: String, checked: Boolean) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = if (checked) Icons.Filled.CheckBox else Icons.Outlined.CheckBoxOutlineBlank,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = if (checked) Color(0xFF0B94FE) else Color.Black
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1A1A1A)
            )
        )
    }
}

@Composable
fun TrobadorInfoItem(icon: ImageVector, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
    ) {
        Icon(icon, null, tint = Color.Black, modifier = Modifier.size(23.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text,
            color = Color.Black,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 13.sp,
                fontWeight = FontWeight(700)
            ),
            lineHeight = 15.sp
        )
    }
}
