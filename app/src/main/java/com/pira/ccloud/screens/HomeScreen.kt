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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.pira.ccloud.components.HomeMovieSection
import com.pira.ccloud.components.HomeSeriesSection
import com.pira.ccloud.data.model.Movie
import com.pira.ccloud.data.model.Series
import com.pira.ccloud.navigation.AppScreens
import com.pira.ccloud.ui.home.HomeViewModel
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
        navController?.navigate("single_movie/${movie.id}")
    }

    val onClickSeries : (Series) -> Unit = { series ->
        StorageUtils.saveSeriesToFile(context, series)
        navController?.navigate("single_series/${series.id}")
    }

    val onClickShowMore : () -> Unit = {
        navController?.navigate(AppScreens.Series.route)
    }

    LaunchedEffect(Unit) {
        viewModel.loadMovies()
    }

    val paddingSize = 20.dp
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            HomeMovieSection(
                title = "جدیدترین فیلم ها",
                data = newMovies,
                onClick = onClickMovie ,
                showMore = onClickShowMore
            )
        }

        item { Spacer(Modifier.height(paddingSize)) }

        item {
            HomeSeriesSection(
                title = "جدیدترین سریال ها",
                data = newSeries,
                onClick = onClickSeries ,
                showMore = onClickShowMore
            )
        }

        item { Spacer(Modifier.height(paddingSize)) }

        item {
            HomeMovieSection(
                title = "برترین فیلم ها",
                data = topMovies,
                onClick = onClickMovie ,
                showMore = onClickShowMore
            )
        }

        item { Spacer(Modifier.height(paddingSize)) }

        item {
            HomeSeriesSection(
                title = "برترین سریال ها",
                data = topSeries,
                onClick = onClickSeries ,
                showMore = onClickShowMore
            )
        }

        item { Spacer(Modifier.height(paddingSize)) }

        item {
            HomeMovieSection(
                title = "فیلم های پرطرفدار",
                data = popularMovies,
                onClick = onClickMovie ,
                showMore = onClickShowMore
            )
        }

        item { Spacer(Modifier.height(paddingSize)) }

        item {
            HomeSeriesSection(
                title = "سریال های پرطرفدار",
                data = popularSeries,
                onClick = onClickSeries ,
                showMore = onClickShowMore
            )
        }
    }
}
