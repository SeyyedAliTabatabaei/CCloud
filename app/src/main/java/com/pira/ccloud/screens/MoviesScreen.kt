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
import com.pira.ccloud.components.ModernCircularProgressIndicator
import com.pira.ccloud.components.MovieGridItem
import com.pira.ccloud.components.MovieGridItemShimmer
import com.pira.ccloud.components.MovieListItem
import com.pira.ccloud.components.ViewToggleCard
import com.pira.ccloud.components.ViewType
import com.pira.ccloud.data.model.FilterType
import com.pira.ccloud.data.model.Genre
import com.pira.ccloud.data.model.Movie
import com.pira.ccloud.navigation.AppScreens
import com.pira.ccloud.ui.movies.MoviesViewModel
import com.pira.ccloud.ui.theme.Surface
import com.pira.ccloud.utils.DeviceUtils
import com.pira.ccloud.utils.StorageUtils
import kotlin.enums.enumEntries


@SuppressLint("RememberReturnType")
@Composable
fun MoviesScreen(
    viewModel: MoviesViewModel = viewModel(),
    navController: NavController? = null,
    filterType: FilterType
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
        viewModel.selectFilterType(filterType)
        if (movies.isEmpty()) {
            viewModel.loadMovies()
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {

        Toolbar(
            navController = navController ,
            title = "فیلم ها"
        )

        Row(
            modifier = Modifier.fillMaxWidth() ,
            verticalAlignment = Alignment.CenterVertically ,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Row(
                modifier = Modifier.weight(1f),
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

            ViewToggleCard(
                initial = viewType ,
                onViewChange = {
                    viewType = it
                }
            )
        }

        when {
            isLoading && movies.isEmpty() -> {
                MovieGridItemShimmer()
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
                    onLoadMore = { viewModel.loadMoreMovies() },
                    navController = navController ,
                    isGridView = viewType == ViewType.GRID
                )
            }
        }
    }
}

@Composable
fun MovieGridContent(
    movies: List<Movie>,
    isGridView: Boolean,
    isLoadingMore: Boolean,
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
            AnimatedVisibility(
                visible = true,
                enter = fadeIn() + expandVertically()
            ) {
                if (isGridView) {
                    MovieGridItem(movie = movie, onClick = {
                        StorageUtils.saveMovieToFile(context, movie)
                        navController?.navigate(AppScreens.SingleMovie.routeWithData(movie.id))
                    })
                } else {
                    MovieListItem(movie = movie, onClick = {
                        StorageUtils.saveMovieToFile(context, movie)
                        navController?.navigate(AppScreens.SingleMovie.routeWithData(movie.id))
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
