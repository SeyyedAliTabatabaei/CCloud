package com.pira.ccloud.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.MovieFilter
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pira.ccloud.data.model.FilterType
import com.pira.ccloud.data.model.Genre

@Composable
fun GenreFilterSection(
    genres: List<Genre>,
    selectedGenreId: Int,
    selectedFilterType: FilterType,
    onGenreSelected: (Int) -> Unit,
    onFilterTypeSelected: (FilterType) -> Unit
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        // Filter type selector on the left
        FilterTypeSelector(
            selectedFilterType = selectedFilterType,
            onFilterTypeSelected = onFilterTypeSelected
        )

        // Genre selector on the right
        GenreSelector(
            genres = genres,
            selectedGenreId = selectedGenreId,
            onGenreSelected = onGenreSelected
        )
    }
}

@Composable
fun FilterTypeSelector(
    selectedFilterType: FilterType,
    onFilterTypeSelected: (FilterType) -> Unit
) {
    var showSheet by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(50.dp),
        modifier = Modifier
            .padding(8.dp)
            .clip(RoundedCornerShape(50.dp))
            .clickable { showSheet = true },
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    )  {
        Row(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Sort,
                contentDescription = "Filter",
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(20.dp)
            )

            Text(
                text = when (selectedFilterType) {
                    FilterType.DEFAULT -> "Default"
                    FilterType.BY_YEAR -> "By Year"
                    FilterType.BY_IMDB -> "By IMDB"
                    FilterType.BY_VIEWS -> "By Views"
                },
                style = MaterialTheme.typography.labelMedium
            )
        }
    }

    if (showSheet) {
        SortBottomSheet(
            selectedFilter = selectedFilterType,
            onDismiss = { showSheet = false } ,
            onFilterSelected = {
                onFilterTypeSelected(it)
                showSheet = false
            }
        )
    }

}

@Composable
fun GenreSelector(
    genres: List<Genre>,
    selectedGenreId: Int,
    onGenreSelected: (Int) -> Unit
) {
    var showSheet by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(50.dp),
        modifier = Modifier
            .padding(8.dp)
            .clip(RoundedCornerShape(50.dp))
            .clickable { showSheet = true },
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    )  {
        Row(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Icon(
                imageVector = Icons.Default.MovieFilter,
                contentDescription = "List",
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.size(20.dp)
            )

            Text(
                text = "${if (selectedGenreId == 0) "All Genres" else genres.find { it.id == selectedGenreId }?.title ?: "All Genres"}",
                style = MaterialTheme.typography.labelMedium
            )
        }
    }

    if (showSheet) {
        GenreBottomSheet(
            genres = genres,
            selectedGenreId = selectedGenreId,
            onGenreSelected = {
                onGenreSelected(it)
                showSheet = false
            },
            onDismiss = { showSheet = false }
        )
    }
}
