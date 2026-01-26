package com.pira.ccloud.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.pira.ccloud.R
import com.pira.ccloud.components.ErrorScreen
import com.pira.ccloud.components.FilterTypeSelector
import com.pira.ccloud.components.GenreSelector
import com.pira.ccloud.components.ModernCircularProgressIndicator
import com.pira.ccloud.components.MovieGridItem
import com.pira.ccloud.components.MovieGridItemShimmer
import com.pira.ccloud.components.MovieListItem
import com.pira.ccloud.components.ViewToggleCard
import com.pira.ccloud.components.ViewType
import com.pira.ccloud.data.model.FilterType
import com.pira.ccloud.data.model.Series
import com.pira.ccloud.navigation.AppScreens
import com.pira.ccloud.ui.series.SeriesViewModel
import com.pira.ccloud.utils.DeviceUtils
import com.pira.ccloud.utils.StorageUtils

@Composable
fun SeriesScreen(
    viewModel: SeriesViewModel = viewModel(),
    navController: NavController? = null,
    filterType: FilterType
) {
    val series = viewModel.series
    val isLoading = viewModel.isLoading
    val isLoadingMore = viewModel.isLoadingMore
    val errorMessage = viewModel.errorMessage
    val genres = viewModel.genres
    val selectedGenreId = viewModel.selectedGenreId
    val selectedFilterType = viewModel.selectedFilterType
    var viewType by remember { mutableStateOf(ViewType.GRID) }

    LaunchedEffect(Unit) {
        viewModel.selectFilterType(filterType)
        if (series.isEmpty()) {
            viewModel.loadSeries()
        }
    }
    
    Column(modifier = Modifier.fillMaxSize()) {
        Toolbar(
            navController = navController ,
            title = stringResource(R.string.series)
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
            isLoading && series.isEmpty() -> {
                MovieGridItemShimmer()
            }
            errorMessage != null && series.isEmpty() -> {
                ErrorScreen(
                    errorMessage = errorMessage,
                    onRetry = { viewModel.retry() }
                )
            }
            else -> {
                SeriesGridContent(
                    series = series,
                    isLoadingMore = isLoadingMore,
                    onLoadMore = { viewModel.loadMoreSeries() },
                    navController = navController ,
                    isGridView = viewType == ViewType.GRID
                )
            }
        }
    }
}



@Composable
fun SeriesGridContent(
    series: List<Series>,
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
        itemsIndexed(series) { index, serie ->
            AnimatedVisibility(
                visible = true,
                enter = fadeIn() + expandVertically()
            ) {
                if (isGridView) {
                    MovieGridItem(series = serie, onClick = {
                        StorageUtils.saveSeriesToFile(context, serie)
                        navController?.navigate(AppScreens.SingleSeries.routeWithData(serie.id))
                    })
                } else {
                    MovieListItem(series = serie, onClick = {
                        StorageUtils.saveSeriesToFile(context, serie)
                        navController?.navigate(AppScreens.SingleSeries.routeWithData(serie.id))
                    })
                }
            }

            if (index >= series.size - 3) {
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

