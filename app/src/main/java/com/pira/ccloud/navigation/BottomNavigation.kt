package com.pira.ccloud.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.pira.ccloud.ui.theme.*

@Composable
fun CustomBottomNavigationBar(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(Surface)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        AppScreens.screens.filter { it.showBottomBar }.forEach { screen ->
            val isSelected = currentRoute == screen.route

            Row(
                modifier = Modifier
                    .height(60.dp)
                    .padding(vertical = 10.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(color = if (isSelected) Primary.copy(alpha = 0.15f) else Color.Transparent,)
                    .clickable {
                        if (!isSelected) {
                            navController.navigate(screen.route) {
                                launchSingleTop = true
                                restoreState = true
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                            }
                        }
                    }
                    .padding(horizontal = 12.dp ),
                verticalAlignment = Alignment.CenterVertically
            ) {
                androidx.compose.material3.Icon(
                    imageVector = screen.icon ?: Icons.Default.Movie,
                    contentDescription = stringResource(screen.resourceId),
                    tint = if (isSelected) Primary else TextMuted,
                    modifier = Modifier.size(22.dp)
                )

                AnimatedVisibility(
                    visible = isSelected,
                    enter = expandHorizontally(
                        animationSpec = tween(200),
                        expandFrom = Alignment.Start
                    ) + fadeIn(animationSpec = tween(150)),
                    exit = shrinkHorizontally(
                        animationSpec = tween(200),
                        shrinkTowards = Alignment.Start
                    ) + fadeOut(animationSpec = tween(150))
                ) {
                    Text(
                        modifier = Modifier.padding(start = 8.dp),
                        text = stringResource(screen.resourceId),
                        color = Primary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1
                    )
                }
            }
        }
    }
}
