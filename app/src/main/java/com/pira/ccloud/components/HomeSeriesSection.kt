package com.pira.ccloud.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pira.ccloud.data.model.Movie
import com.pira.ccloud.data.model.Series
import com.pira.ccloud.ui.home.MovieData
import com.pira.ccloud.ui.home.SeriesData
import com.pira.ccloud.ui.theme.CCloudTheme
import com.pira.ccloud.utils.StorageUtils
import com.pira.ccloud.utils.fakeMovie


@Composable
fun HomeSeriesSection(
    modifier: Modifier = Modifier,
    title : String,
    data: SeriesData,
    onClick : (Series) -> Unit,
    showMore : () -> Unit
) {
    val context = LocalContext.current

    Column(
        modifier = modifier.fillMaxWidth() ,
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp) ,
            horizontalArrangement = Arrangement.SpaceBetween ,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title ,
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = "نمایش همه" ,
                style = MaterialTheme.typography.bodySmall ,
                color = MaterialTheme.colorScheme.primary ,
                modifier = Modifier.padding(vertical = 5.dp).clickable{
                    showMore()
                } ,
                textAlign = TextAlign.Center
            )
        }

        when {
            data.isLoading && data.series.isEmpty() -> {
                MovieGridItemShimmer()
            }
            data.errorMessage != null && data.series.isEmpty() -> {
                ErrorScreen(
                    errorMessage = data.errorMessage,
                    onRetry = { data.retry() }
                )
            }
            else -> {
                LazyRow(
                    modifier = Modifier.fillMaxWidth() ,
                    contentPadding = PaddingValues(15.dp) ,
                ) {
                    items(
                        items = data.series,
                        key = { it.id } ,
                    ) { series ->
                        MovieGridItem(series = series, onClick = {
                            StorageUtils.saveSeriesToFile(context, series)
                            onClick(series)
                        })
                        Spacer(Modifier.width(10.dp))
                    }

                }
            }
        }

    }
}


@Preview(showBackground = true)
@Composable
private fun HomeSeriesSectionPreview() {
    CCloudTheme() {
//        HomeSeriesSection(
//            title = "دسته بندی فیلم" ,
//            data = SeriesData(
//                series = listOf(fakeMovie() , fakeMovie().copy(id = 2))
//            ),
//            onClick = {
//
//            } ,
//            showMore = {
//
//            }
//        )
    }

}