package com.pira.ccloud.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

enum class ViewType {
    GRID, LIST
}

@Composable
fun ViewToggleCard(
    initial: ViewType = ViewType.GRID,
    onViewChange: (ViewType) -> Unit
) {
    var selected by remember { mutableStateOf(initial) }

    Card(
        shape = RoundedCornerShape(50.dp),
        modifier = Modifier
            .wrapContentSize()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Grid Icon
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(color = if (selected == ViewType.GRID) MaterialTheme.colorScheme.primary else Color.Transparent)
                    .clickable {
                        selected = ViewType.GRID
                        onViewChange(ViewType.GRID)
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.GridView,
                    contentDescription = "Grid",
                    tint = if (selected == ViewType.GRID) Color.White else MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.padding(5.dp)
                )
            }

            // List Icon
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .background(color = if (selected == ViewType.LIST) MaterialTheme.colorScheme.primary else Color.Transparent)
                    .clickable {
                        selected = ViewType.LIST
                        onViewChange(ViewType.LIST)
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.List,
                    contentDescription = "List",
                    tint = if (selected == ViewType.LIST) Color.White else MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.padding(5.dp)
                )
            }
        }
    }
}
