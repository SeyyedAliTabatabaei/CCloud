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
import com.pira.ccloud.utils.fakeMovie

@Composable
fun MovieListItem(
    movie: Movie,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp)
    ) {
        MoviePoster(
            imageUrl = movie.image,
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
                text = movie.title,
                style = MaterialTheme.typography.titleSmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = movie.description,
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
                    text = "⭐ ${movie.rating}",
                    fontSize = 13.sp
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = movie.duration ?: "",
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
