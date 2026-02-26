package com.cacamites.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cacamites.app.R
import com.cacamites.app.model.Card
import com.cacamites.app.model.Point
import com.cacamites.app.model.PointType
import com.cacamites.app.repository.GameRepository

@Composable
fun LegendDetailScreen(
    card: Card,
    onClose: () -> Unit,
    repository: GameRepository
) {
    val scrollState = rememberScrollState()
    val isStartPoint = card.id == "c_start"
    val context = LocalContext.current
    val gameState by repository.gameState.collectAsState()
    val pointsList by repository.points.collectAsState()
    val cardsList by repository.cards.collectAsState()
    
    // Observe the card in the list if it might change (e.g., quiz result)
    val liveCard = cardsList.find { it.id == card.id } ?: card
    
    // Trobem el punt associat a la carta
    val point = pointsList.find { p -> 
        if (p.id.startsWith("s_")) "c_s_${p.id.removePrefix("s_")}" == liveCard.id
        else "c_${p.id.removePrefix("p_")}" == liveCard.id
    }
    
    val isQuiz = point?.type == PointType.QUIZ && point.quiz != null

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            // Hero Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp)
            ) {
                val imageRes = if (!liveCard.imageUrl.isNullOrEmpty()) {
                    val resId = context.resources.getIdentifier(liveCard.imageUrl, "drawable", context.packageName)
                    if (resId != 0) resId else R.drawable.foto_castell_del_baro
                } else {
                    R.drawable.foto_castell_del_baro
                }
                
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = liveCard.title,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.4f)),
                                startY = 600f
                            )
                        )
                )
            }

            // Content Card
            Column(
                modifier = Modifier
                    .offset(y = (-20).dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                    .background(Color.White)
                    .padding(24.dp)
            ) {
                Text(
                    text = liveCard.type.uppercase(),
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = Color(0xFFEC6209),
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.5.sp
                    )
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = liveCard.title,
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Black,
                        fontSize = 28.sp,
                        color = Color.Black
                    )
                )
                
                if (point != null && point.score != 0) {
                    Spacer(modifier = Modifier.height(16.dp))
                    val sign = if (point.score > 0) "+" else ""
                    val badgeColor = if (point.score > 0) Color(0xFF4CAF50) else Color(0xFFE53935)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(color = badgeColor, shape = CircleShape, modifier = Modifier.size(8.dp)) {}
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "$sign${point.score} PUNTS DE VIDA",
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = badgeColor,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }

                Divider(modifier = Modifier.padding(vertical = 24.dp), color = Color.LightGray.copy(alpha = 0.5f))

                Text(
                    text = liveCard.description,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        lineHeight = 26.sp,
                        fontSize = 16.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Medium
                    )
                )

                // --- QUIZ SECTION ---
                if (isQuiz) {
                    Spacer(modifier = Modifier.height(32.dp))
                    if (point!!.state == com.cacamites.app.model.PointState.COMPLETED) {
                        // Show result message if already completed (after answering)
                        Text(
                            text = "RESPOSTA DONADA",
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Black, color = Color(0xFF4CAF50))
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp)
                                .shadow(elevation = 2.dp, shape = RoundedCornerShape(12.dp))
                                .clickable { onClose() },
                            color = Color(0xFFF17002),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxSize(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "CONTINUAR",
                                    color = Color.White,
                                    style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
                                )
                            }
                        }
                    } else {
                        QuizSectionComp(quiz = point!!.quiz!!, onAnswer = { index -> 
                            repository.onQuizAnswered(point.id, index)
                            // No tanquem immediatament per deixar que vegi el canvi de text si n'hi ha
                        })
                    }
                }
                
                Spacer(modifier = Modifier.height(40.dp))
                
                // Botó TORNAR
                if (!isQuiz) {
                    val buttonColor = if (isStartPoint || point?.type == PointType.NARRATIVE) Color(0xFFF17002) else Color(0xff0b94fe)
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .shadow(elevation = 2.dp, shape = RoundedCornerShape(12.dp))
                            .clickable { onClose() },
                        color = buttonColor,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            if (isStartPoint) {
                                Icon(Icons.Default.Explore, null, tint = Color.White)
                                Spacer(Modifier.width(12.dp))
                            }
                            Text(
                                text = "TORNAR AL MAPA",
                                color = Color(0xFF202020),
                                style = TextStyle(fontSize = 16.sp, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(20.dp))
            }
        }

        IconButton(
            onClick = onClose,
            modifier = Modifier
                .padding(16.dp)
                .size(40.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.8f))
                .align(Alignment.TopEnd)
        ) {
            Icon(Icons.Default.Close, "Tancar", tint = Color.Black)
        }
    }
}

@Composable
fun QuizSectionComp(quiz: com.cacamites.app.model.Quiz, onAnswer: (Int) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableStateOf(-1) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "RESPON PER GUANYAR PUNTS:",
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Black, color = Color.Gray)
        )
        Spacer(modifier = Modifier.height(12.dp))
        
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(
                onClick = { expanded = true },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (selectedIndex == -1) "Selecciona una resposta..." else quiz.options[selectedIndex],
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.weight(1f))
                    Icon(Icons.Default.ArrowDropDown, null, tint = Color.Black)
                }
            }
            
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.fillMaxWidth(0.8f).background(Color.White)
            ) {
                quiz.options.forEachIndexed { index, option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            selectedIndex = index
                            expanded = false
                        }
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = { if (selectedIndex != -1) onAnswer(selectedIndex) },
            enabled = selectedIndex != -1,
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text("DONAR RESPOSTA", fontWeight = FontWeight.Black)
        }
    }
}
