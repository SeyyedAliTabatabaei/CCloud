package com.pira.ccloud.utils

import com.pira.ccloud.data.model.Genre
import com.pira.ccloud.data.model.Movie

fun fakeMovie() = Movie(
    id = 1,
    type = "movie",
    title = "Interstellar",
    description = "A team of explorers travel through a wormhole in space.",
    year = 2014,
    imdb = 8.6,
    rating = 4.5,
    duration = "2h 49m",
    image = "https://via.placeholder.com/300x450",
    cover = "",
    genres = listOf(
        Genre(id = 1, title = "Sci-Fi")
    ),
    sources = emptyList(),
    country = emptyList()
)
