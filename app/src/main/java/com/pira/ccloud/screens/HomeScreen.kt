package com.pira.ccloud.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.pira.ccloud.R
import com.pira.ccloud.components.HomeMovieSection
import com.pira.ccloud.components.HomeSeriesSection
import com.pira.ccloud.data.model.Movie
import com.pira.ccloud.data.model.Series
import com.pira.ccloud.navigation.AppScreens
import com.pira.ccloud.ui.home.HomeViewModel
import com.pira.ccloud.ui.home.MovieData
import com.pira.ccloud.ui.home.SeriesData
import com.pira.ccloud.utils.StorageUtils

@SuppressLint("RememberReturnType")
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(),
    navController: NavController? = null
) {
    val context = LocalContext.current

    val newMovies = viewModel.newMovie
    val newSeries = viewModel.newSeries
    val topMovies = viewModel.topMovies
    val topSeries = viewModel.topSeries
    val popularMovies = viewModel.popularMovies
    val popularSeries = viewModel.popularSeries

    val onClickMovie : (Movie) -> Unit = { movie ->
        StorageUtils.saveMovieToFile(context, movie)
        navController?.navigate(AppScreens.SingleMovie.routeWithData(movie.id))
    }

    val onClickSeries : (Series) -> Unit = { series ->
        StorageUtils.saveSeriesToFile(context, series)
        navController?.navigate(AppScreens.SingleSeries.routeWithData(series.id))
    }

    val onClickShowMoreSeries : (SeriesData) -> Unit = {
        navController?.navigate(AppScreens.Series.routeWithData(it.filterType))
    }

    val onClickShowMoreMovie : (MovieData) -> Unit = {
        navController?.navigate(AppScreens.Movies.routeWithData(it.filterType))
    }

    LaunchedEffect(Unit) {
        viewModel.loadMovies()
    }

    val paddingSize = 20.dp
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            HomeMovieSection(
                title = stringResource(R.string.home_screen_newest_movies),
                data = newMovies,
                onClick = onClickMovie ,
                showMore = { onClickShowMoreMovie(newMovies) }
            )
        }

        item { Spacer(Modifier.height(paddingSize)) }

        item {
            HomeSeriesSection(
                title = stringResource(R.string.home_screen_newest_series),
                data = newSeries,
                onClick = onClickSeries ,
                showMore = { onClickShowMoreSeries(newSeries) }
            )
        }

        item { Spacer(Modifier.height(paddingSize)) }

        item {
            HomeMovieSection(
                title = stringResource(R.string.home_screen_top_movies),
                data = topMovies,
                onClick = onClickMovie ,
                showMore = { onClickShowMoreMovie(topMovies) }
            )
        }

        item { Spacer(Modifier.height(paddingSize)) }

        item {
            HomeSeriesSection(
                title = stringResource(R.string.home_screen_top_series),
                data = topSeries,
                onClick = onClickSeries ,
                showMore = { onClickShowMoreSeries(topSeries) }
            )
        }

        item { Spacer(Modifier.height(paddingSize)) }

        item {
            HomeMovieSection(
                title = stringResource(R.string.home_screen_popular_movies),
                data = popularMovies,
                onClick = onClickMovie ,
                showMore = { onClickShowMoreMovie(popularMovies) }
            )
        }

        item { Spacer(Modifier.height(paddingSize)) }

        item {
            HomeSeriesSection(
                title = stringResource(R.string.home_screen_popular_series),
                data = popularSeries,
                onClick = onClickSeries ,
                showMore = { onClickShowMoreSeries(popularSeries) }
            )
        }
    }
}
