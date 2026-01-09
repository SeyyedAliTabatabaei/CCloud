package com.pira.ccloud.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.pira.ccloud.R


@Composable
fun MainToolbar(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp) ,
        verticalAlignment = Alignment.CenterVertically ,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.app_name) ,
            style = MaterialTheme.typography.titleLarge
                .copy(color = MaterialTheme.colorScheme.onPrimary),
        )
    }
}


