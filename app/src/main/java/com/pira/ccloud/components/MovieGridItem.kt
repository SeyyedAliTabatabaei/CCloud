package com.pira.ccloud.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.sp
import com.pira.ccloud.data.model.Movie
import com.pira.ccloud.data.model.Series
import com.pira.ccloud.utils.fakeMovie


@Composable
fun MovieGridItem(
    movie: Movie ?= null,
    series: Series ?= null,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {

    val image = when {
        movie != null -> movie.image
        series != null -> series.image
        else -> ""
    }

    val imdb = when {
        movie != null -> movie.imdb
        series != null -> series.imdb
        else -> ""
    }

    val title = when {
        movie != null -> movie.title
        series != null -> series.title
        else -> ""
    }

    val year = when {
        movie != null -> movie.year
        series != null -> series.year
        else -> ""
    }

    val genre = when {
        movie != null -> movie.genres
        series != null -> series.genres
        else -> emptyList()
    }


    Card(
        modifier = modifier
            .width(150.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column {
            Box {
                MoviePoster(
                    imageUrl = image,
                    modifier = Modifier
                        .height(200.dp)
                        .fillMaxWidth()
                )

                // IMDb badge
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.TopEnd)
                        .background(
                            color = Color.Black.copy(alpha = 0.7f),
                            shape = RoundedCornerShape(8.dp)
                        )
                ) {
                    Text(
                        text = "⭐️ $imdb",
                        color = Color.White,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Column(
                modifier = Modifier.padding(10.dp)
            ) {
                Text(
                    text = title,
                    maxLines = 2,
                    minLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleSmall
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "$year • ${genre.firstOrNull()?.title ?: ""}",
                    color = MaterialTheme.colorScheme.primary ,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
fun MovieGridItemShimmer(count : Int = 5) {

    val brush = shimmerBrush()

    LazyRow (horizontalArrangement = Arrangement.spacedBy(10.dp)){
        items(count){
            Card(
                modifier = Modifier.width(150.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {
                Column {
                    Box {
                        Box(
                            modifier = Modifier
                                .height(200.dp)
                                .fillMaxWidth()
                                .background(brush)
                        )

                        Box(
                            modifier = Modifier
                                .padding(8.dp)
                                .align(Alignment.TopEnd)
                                .size(width = 40.dp, height = 18.dp)
                                .background(
                                    brush = brush,
                                    shape = RoundedCornerShape(8.dp)
                                )
                        )
                    }

                    Column(modifier = Modifier.padding(10.dp)) {

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(16.dp)
                                .background(brush, RoundedCornerShape(4.dp))
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.7f)
                                .height(16.dp)
                                .background(brush, RoundedCornerShape(4.dp))
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.5f)
                                .height(12.dp)
                                .background(brush, RoundedCornerShape(4.dp))
                        )
                    }
                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun MovieGridItemPreview() {
    Column() {
        MovieGridItem(
            movie = fakeMovie()
        )
        MovieGridItemShimmer()
    }

}


