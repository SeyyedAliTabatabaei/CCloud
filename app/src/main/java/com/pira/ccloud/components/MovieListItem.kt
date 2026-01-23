package com.pira.ccloud.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.unit.sp
import com.pira.ccloud.data.model.Movie
import com.pira.ccloud.data.model.Series
import com.pira.ccloud.utils.fakeMovie

@Composable
fun MovieListItem(
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
        else -> 0.0
    }


    val title = when {
        movie != null -> movie.title
        series != null -> series.title
        else -> ""
    }

    val duration = when {
        movie != null -> movie.duration
        series != null -> series.duration
        else -> ""
    }

    val description = when {
        movie != null -> movie.description
        series != null -> series.description
        else -> ""
    }


    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp)
    ) {
        MoviePoster(
            imageUrl = image,
            modifier = Modifier
                .width(110.dp)
                .height(160.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.weight(1f))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "⭐ ${imdb}",
                    fontSize = 13.sp
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = duration ?: "",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun MovieListItemPreview() {
    MovieListItem(
        movie = fakeMovie()
    )
}
