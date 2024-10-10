package com.daymax.crochetpixels

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun GridScreen(
    rows: MutableList<GridRow>,
    fetchingData: Boolean,
) {
    if (fetchingData) {
        Column {
            Text(text = "Loading...")
        }
    }
    // Create a LazyColumn of LazyRows
    LazyColumn {
        items(rows) { row ->
            LazyRow {
                items(row.cells) { cell ->
                    Box(
                        modifier = Modifier
                            .padding(2.dp)
                            .background(cell.colour)
                    ) {
                        Text(cell.length.toString())
                    }
                }
            }
        }
    }
}
