package com.example.mitego.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp

@Composable
fun SearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.Start),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(shape = MaterialTheme.shapes.medium)
            .background(color = Color.White)
            .border(
                border = BorderStroke(1.dp, Color(0xff0b94fe)),
                shape = MaterialTheme.shapes.medium
            )
            .padding(horizontal = 16.dp, vertical = 4.dp)
    ) {
        BasicTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier.weight(1f),
            textStyle = TextStyle(
                color = Color.Black,
                fontSize = 16.sp,
                lineHeight = 1.5.em
            ),
            cursorBrush = SolidColor(Color(0xff0b94fe)),
            decorationBox = { innerTextField ->
                if (query.isEmpty()) {
                    Text(
                        text = "Buscar",
                        color = Color.Black.copy(alpha = 0.6f),
                        fontSize = 16.sp,
                        lineHeight = 1.5.em
                    )
                }
                innerTextField()
            }
        )
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "search",
            tint = Color(0xff0b94fe),
            modifier = Modifier.size(24.dp)
        )
    }
}
