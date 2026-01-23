package com.pira.ccloud.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pira.ccloud.data.model.FilterType
import com.pira.ccloud.data.model.Movie
import com.pira.ccloud.data.model.Series
import com.pira.ccloud.data.repository.MovieRepository
import com.pira.ccloud.data.repository.SeriesRepository
import com.pira.ccloud.utils.LanguageUtils
import kotlinx.coroutines.launch


data class MovieData(
    val movies : List<Movie> = emptyList(),
    val isLoading : Boolean = true,
    val errorMessage : String ?= null,
    val filterType: FilterType = FilterType.DEFAULT,
    var retry : () -> Unit = {}
)

data class SeriesData(
    val series : List<Series> = emptyList(),
    val isLoading : Boolean = true,
    val filterType: FilterType = FilterType.DEFAULT,
    val errorMessage : String ?= null,
    var retry : () -> Unit = {}
)

class HomeViewModel : ViewModel() {
    private val repository = MovieRepository()
    private val seriesRepository = SeriesRepository()

    var newMovie by mutableStateOf(MovieData(filterType = FilterType.DEFAULT))
        private set

    var newSeries by mutableStateOf(SeriesData(filterType = FilterType.DEFAULT))
        private set

    var topMovies by mutableStateOf(MovieData(filterType = FilterType.BY_IMDB))
        private set

    var topSeries by mutableStateOf(SeriesData(filterType = FilterType.BY_IMDB))
        private set

    var popularMovies by mutableStateOf(MovieData(filterType = FilterType.BY_VIEWS))
        private set

    var popularSeries by mutableStateOf(SeriesData(filterType = FilterType.BY_VIEWS))
        private set

    init {
        newMovie.retry = { loadNewMovies() }
        newSeries.retry = { loadNewSeries() }
        topMovies.retry = { loadTopMovies() }
        topSeries.retry = { loadTopSeries() }
        popularMovies.retry = { loadPopularMovies() }
        popularSeries.retry = { loadPopularSeries() }
    }

    fun loadMovies() {
        loadNewMovies()
        loadNewSeries()
        loadTopMovies()
        loadTopSeries()
        loadPopularMovies()
        loadPopularSeries()
    }

    private fun loadNewMovies(page: Int = 0) {
        viewModelScope.launch {
            var mData = newMovie
            try {
                if (page == 0) {
                    mData = mData.copy(isLoading = true)
                } else {
                    mData = mData.copy(isLoading = false)
                }
                mData = mData.copy(errorMessage = null)

                val newMovies = repository.getNewMovies(filterType = mData.filterType).filter { movie ->
                    LanguageUtils.shouldDisplayTitle(movie.title)
                }

                mData = mData.copy(movies = newMovies)
            } catch (e: Exception) {
                mData = mData.copy(errorMessage = e.message)
            } finally {
                mData = mData.copy(isLoading = false)
            }
            newMovie = mData
        }
    }

    private fun loadNewSeries(page: Int = 0) {
        viewModelScope.launch {
            var mData = newSeries
            try {
                if (page == 0) {
                    mData = mData.copy(isLoading = true)
                } else {
                    mData = mData.copy(isLoading = false)
                }
                mData = mData.copy(errorMessage = null)

                val newMovies = seriesRepository.getNewSeries(filterType = mData.filterType).filter { movie ->
                    LanguageUtils.shouldDisplayTitle(movie.title)
                }

                mData = mData.copy(series = newMovies)
            } catch (e: Exception) {
                mData = mData.copy(errorMessage = e.message)
            } finally {
                mData = mData.copy(isLoading = false)
            }
            newSeries = mData
        }
    }

    private fun loadTopMovies(page: Int = 0) {
        viewModelScope.launch {
            var mData = topMovies
            try {
                if (page == 0) {
                    mData = mData.copy(isLoading = true)
                } else {
                    mData = mData.copy(isLoading = false)
                }
                mData = mData.copy(errorMessage = null)

                val newMovies = repository.getTopMovie(filterType = mData.filterType).filter { movie ->
                    LanguageUtils.shouldDisplayTitle(movie.title)
                }

                mData = mData.copy(movies = newMovies)
            } catch (e: Exception) {
                mData = mData.copy(errorMessage = e.message)
            } finally {
                mData = mData.copy(isLoading = false)
            }
            topMovies = mData
        }
    }

    private fun loadTopSeries(page: Int = 0) {
        viewModelScope.launch {
            var mData = topSeries
            try {
                if (page == 0) {
                    mData = mData.copy(isLoading = true)
                } else {
                    mData = mData.copy(isLoading = false)
                }
                mData = mData.copy(errorMessage = null)

                val newMovies = seriesRepository.getTopSeries(filterType = mData.filterType).filter { movie ->
                    LanguageUtils.shouldDisplayTitle(movie.title)
                }

                mData = mData.copy(series = newMovies)
            } catch (e: Exception) {
                mData = mData.copy(errorMessage = e.message)
            } finally {
                mData = mData.copy(isLoading = false)
            }
            topSeries = mData
        }
    }

    private fun loadPopularMovies(page: Int = 0) {
        viewModelScope.launch {
            var mData = popularMovies
            try {
                if (page == 0) {
                    mData = mData.copy(isLoading = true)
                } else {
                    mData = mData.copy(isLoading = false)
                }
                mData = mData.copy(errorMessage = null)

                val newMovies = repository.getPopularMovie(filterType = mData.filterType).filter { movie ->
                    LanguageUtils.shouldDisplayTitle(movie.title)
                }

                mData = mData.copy(movies = newMovies)
            } catch (e: Exception) {
                mData = mData.copy(errorMessage = e.message)
            } finally {
                mData = mData.copy(isLoading = false)
            }
            popularMovies = mData
        }
    }

    private fun loadPopularSeries(page: Int = 0) {
        viewModelScope.launch {
            var mData = popularSeries
            try {
                if (page == 0) {
                    mData = mData.copy(isLoading = true)
                } else {
                    mData = mData.copy(isLoading = false)
                }
                mData = mData.copy(errorMessage = null)

                val newMovies = seriesRepository.getPopularSeries(filterType = mData.filterType).filter { movie ->
                    LanguageUtils.shouldDisplayTitle(movie.title)
                }

                mData = mData.copy(series = newMovies)
            } catch (e: Exception) {
                mData = mData.copy(errorMessage = e.message)
            } finally {
                mData = mData.copy(isLoading = false)
            }
            popularSeries = mData
        }
    }



}