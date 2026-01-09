package com.pira.ccloud.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.pira.ccloud.R


@Composable
fun Toolbar(
    modifier: Modifier = Modifier ,
    navController : NavController? ,
    title : String
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp) ,
        verticalAlignment = Alignment.CenterVertically ,
        horizontalArrangement = Arrangement.Center
    ) {

        IconButton(onClick = { navController?.popBackStack() }) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back"
            )
        }

        Text(
            text = title ,
            style = MaterialTheme.typography.titleLarge
                .copy(color = MaterialTheme.colorScheme.onPrimary),
            modifier = Modifier.weight(1f)
        )
    }
}


