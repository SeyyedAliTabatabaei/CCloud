package com.pira.ccloud.screens

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.pira.ccloud.components.ErrorScreen
import com.pira.ccloud.components.FilterTypeSelector
import com.pira.ccloud.components.GenreFilterSection
import com.pira.ccloud.components.GenreSelector
import com.pira.ccloud.components.MovieGridItem
import com.pira.ccloud.components.MovieListItem
import com.pira.ccloud.components.ViewToggleCard
import com.pira.ccloud.components.ViewType
import com.pira.ccloud.data.model.Genre
import com.pira.ccloud.data.model.Movie
import com.pira.ccloud.ui.movies.MoviesViewModel
import com.pira.ccloud.ui.theme.Surface
import com.pira.ccloud.utils.DeviceUtils
import com.pira.ccloud.utils.StorageUtils
import kotlin.enums.enumEntries


@SuppressLint("RememberReturnType")
@Composable
fun MoviesScreen(
    viewModel: MoviesViewModel = viewModel(),
    navController: NavController? = null
) {
    val movies = viewModel.movies
    val isLoading = viewModel.isLoading
    val isLoadingMore = viewModel.isLoadingMore
    val errorMessage = viewModel.errorMessage
    val genres = viewModel.genres
    val selectedGenreId = viewModel.selectedGenreId
    val selectedFilterType = viewModel.selectedFilterType
    var viewType by remember { mutableStateOf(ViewType.GRID) }

    LaunchedEffect(Unit) {
        if (movies.isEmpty()) {
            viewModel.loadMovies()
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {

        Row(
            modifier = Modifier.fillMaxWidth() ,
            verticalAlignment = Alignment.CenterVertically ,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ViewToggleCard(
                initial = viewType ,
                onViewChange = {
                    viewType = it
                }
            )


            Row(
                verticalAlignment = Alignment.CenterVertically ,
                horizontalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                GenreSelector(
                    genres = genres,
                    selectedGenreId = selectedGenreId,
                    onGenreSelected = { genreId ->
                        viewModel.selectGenre(genreId)
                    }
                )

                FilterTypeSelector(
                    selectedFilterType = selectedFilterType,
                    onFilterTypeSelected = { filterType ->
                        viewModel.selectFilterType(filterType)
                    }
                )
            }
        }

        when {
            isLoading && movies.isEmpty() -> {
                // Show modern loading animation when initial movies are loading
                LoadingScreen()
            }
            errorMessage != null && movies.isEmpty() -> {
                ErrorScreen(
                    errorMessage = errorMessage,
                    onRetry = { viewModel.retry() }
                )
            }
            else -> {
                MovieGridContent(
                    movies = movies,
                    isLoadingMore = isLoadingMore,
                    errorMessage = errorMessage,
                    onRetry = { viewModel.retry() },
                    onLoadMore = { viewModel.loadMoreMovies() },
                    navController = navController ,
                    isGridView = viewType == ViewType.GRID
                )
            }
        }
    }
}

@Composable
fun LoadingScreen() {
    val shimmerColor = MaterialTheme.colorScheme.surfaceVariant
    val shimmerColorShade = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Add a title while loading
        Text(
            text = "درحال بارگزاری...",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(16.dp),
            fontWeight = FontWeight.Bold
        )

        AnimatedVisibility(
            visible = true,
            enter = fadeIn(initialAlpha = 0.3f),
            exit = fadeOut()
        ) {
            val columns = DeviceUtils.getGridColumns(LocalContext.current.resources)
            LazyVerticalGrid(
                columns = GridCells.Fixed(columns),
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(6) { // Show 6 loading placeholders
                    ShimmerMovieItem(shimmerColor, shimmerColorShade)
                }
            }
        }
    }
}

@Composable
fun ShimmerMovieItem(
    shimmerColor: Color,
    shimmerColorShade: Color
) {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = -1000f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200)
        ), label = "shimmer_translate"
    )

    val brush = Brush.linearGradient(
        colors = listOf(
            shimmerColor,
            shimmerColorShade,
            shimmerColor,
            shimmerColorShade,
            shimmerColor
        ),
        start = Offset.Zero,
        end = Offset(x = translateAnim, y = translateAnim)
    )

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // Movie poster shimmer
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(brush)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Title shimmer
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(20.dp)
                    .background(brush)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Year shimmer
            Box(
                modifier = Modifier
                    .width(60.dp)
                    .height(16.dp)
                    .background(brush)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Genres shimmer
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(16.dp)
                    .background(brush)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Rating shimmer
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(16.dp)
                        .background(brush)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .height(16.dp)
                        .background(brush)
                )
            }
        }
    }
}

@Composable
fun ModernCircularProgressIndicator() {
    val transition = rememberInfiniteTransition(label = "progress")
    val progress by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 1000,
                easing = androidx.compose.animation.core.FastOutSlowInEasing
            )
        ), label = "progress_anim"
    )

    // Add rotation animation for a more dynamic effect
    val rotation by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 2000,
                easing = androidx.compose.animation.core.LinearEasing
            )
        ), label = "rotation_anim"
    )

    CircularProgressIndicator(
        progress = progress,
        modifier = Modifier
            .size(48.dp)
            .rotate(rotation), // Add rotation
        strokeWidth = 4.dp,
        trackColor = MaterialTheme.colorScheme.surfaceVariant,
        color = MaterialTheme.colorScheme.primary
    )
}



@Composable
fun ErrorItem(
    errorMessage: String,
    onRetry: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer,
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "خطا در دریافت لیست",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onErrorContainer,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = errorMessage,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onErrorContainer,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Button(
                onClick = onRetry,
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onErrorContainer,
                    contentColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Retry",
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("تلاش مجدد")
            }
        }
    }
}

@Composable
fun MovieGridContent(
    movies: List<Movie>,
    isGridView: Boolean,
    isLoadingMore: Boolean,
    errorMessage: String?,
    onRetry: () -> Unit,
    onLoadMore: () -> Unit,
    navController: NavController?
) {
    val context = LocalContext.current
    val columns = if (isGridView) DeviceUtils.getGridColumns(LocalContext.current.resources) else 1

    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        itemsIndexed(movies) { index, movie ->
            // انتخاب انیمیشن بر اساس تغییر حالت نمایش
            AnimatedVisibility(
                visible = true,
                enter = fadeIn() + expandVertically()
            ) {
                if (isGridView) {
                    MovieGridItem(movie = movie, onClick = {
                        StorageUtils.saveMovieToFile(context, movie)
                        navController?.navigate("single_movie/${movie.id}")
                    })
                } else {
                    MovieListItem(movie = movie, onClick = {
                        StorageUtils.saveMovieToFile(context, movie)
                        navController?.navigate("single_movie/${movie.id}")
                    })
                }
            }

            if (index >= movies.size - 3) {
                LaunchedEffect(Unit) { onLoadMore() }
            }
        }

        if (isLoadingMore) {
            item {
                Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                    ModernCircularProgressIndicator()
                }
            }
        }
    }
}
