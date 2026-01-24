package com.example.mitego.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mitego.R

@Composable
fun TrobadorScreen(
    mode: String = "full", // "intro" o "full"
    onClose: () -> Unit
) {
    val scrollState = rememberScrollState()

    val titleText = if (mode == "intro") "Salutacions, viatger!" else "La llegenda del Baró de Savassona"

    val annotatedContent = if (mode == "intro") {
        buildAnnotatedString {
            withStyle(style = SpanStyle(color = Color(0xFF1A1A1A), fontWeight = FontWeight.Bold)) {
                append("Sóc l'Isolda, Trobadora de llegendes, i guardo històries que només els més intrèpids poden descobrir. Vine amb mi i et revelaré la llegenda que estàs explorant, així com el secret que hauràs de seguir per fer-la teva.")
            }
        }
    } else {
        buildAnnotatedString {
            withStyle(style = SpanStyle(color = Color(0xFF1A1A1A), fontWeight = FontWeight.Bold)) {
                append("Conta la llegenda que el Baró de Savassona, abans de marxar cap a Terra Santa per participar en les Croades, va encomanar al seu criat més fidel que cuidés i vetllés per la seva bella esposa durant la seva absència.\n\n")
                append("El baró va partir, i com el pas del temps, la solitud i la convivència van unir la baronessa i el criat, que acabaren enamorant-se. Des de la font, els amants vigilaven cada dia la pujada al castell pel Roc del Llum, temorosos que el baró pogués tornar.\n\n")
                append("Però el baró, desconfiat com era, no va prendre el camí habitual. Va tornar d’amagat pel camí de Can Janot i va aparèixer sobtadament per sobre la font. En descobrir la traïció, encegat per la ràbia, va matar els dos amants en aquell mateix indret.\n\n")
                append("Diu la tradició que, les nits de lluna plena, només els homes que han estat traïts per les seves dones poden veure, reflectida a la lluna, la imatge de la baronessa acompanyada del seu amant.\n\n")
            }

            withStyle(style = SpanStyle(color = Color.Black, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)) {
                append("El repte\n\n")
            }

            withStyle(style = SpanStyle(color = Color(0xFF1A1A1A), fontWeight = FontWeight.Bold)) {
                append("Els esperits dels amants encara vaguen per aquests paratges… i necessiten la teva ajuda.\n\n")
                append("Ajuda els amants i interfereix el destí del Baró.\n")
                append("Per aconseguir-ho hauràs de:\n\n")
                append("• Recollir tots els punts de vida que puguis.\n")
                append("• Trobar l’Espassa.\n")
                append("• Parlar amb els amants.\n")
                append("• I, finalment, trobar el Baró per derrotar-lo.\n\n")
            }

            withStyle(style = SpanStyle(color = Color.Black, fontWeight = FontWeight.ExtraBold)) {
                append("⚠️ Atenció: ")
            }
            withStyle(style = SpanStyle(color = Color(0xFF1A1A1A), fontWeight = FontWeight.Bold)) {
                append("mira de trobar el Baró al final del teu camí. Un cop el descobreixis, només tindràs 10 minuts per acumular els punts necessaris i vèncer-lo.\n\n")
                append("La llegenda encara no està escrita del tot.\n")
                append("Ara ets tu qui pot canviar-ne el final.")
            }
        }
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
                painter = painterResource(id = R.drawable.onboarding_01),
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
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Contingut
            Text(
                text = annotatedContent,
                textAlign = TextAlign.Start,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 15.sp
                ),
                lineHeight = 22.sp,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Botó "ENTÉS" fix
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 24.dp, vertical = 20.dp)
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clickable { onClose() },
                shape = RoundedCornerShape(8.dp),
                color = Color(0xff0b94fe)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "ENTÉS",
                        color = Color.White,
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
