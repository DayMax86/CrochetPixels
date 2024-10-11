package com.daymax.crochetpixels

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
            Box(
                modifier = Modifier
                    .padding(2.dp)
                    .background(Color.Black)
            ) {
                Text(
                    text = "${rows.indexOf(row) + 1}",
                    color = Color.White,
                    fontStyle = FontStyle.Italic,
                    style = TextStyle(
                        fontSize = 32.sp,
                    )
                )
            }
            LazyRow {
                items(row.cells) { cell ->
                    val border =  if (cell.done) {
                        BorderStroke(7.dp, Color.Green)
                    } else {
                        BorderStroke(1.dp, Color.Gray)
                    }
                    val background = if (!cell.done) cell.colour else cell.colour.copy(alpha = 0.5f)
                    Box(
                        modifier = Modifier
                            .padding(2.dp)
                            .background(background)
                            .clickable {
                                cell.onClick()
                            }
                            .border(border)
                    ) {
                        val cellWidth = if (cell.length / 1.35 >= 5) cell.length / 1.35 else 5
                        val shadowOffset = Offset(4.0f, 4.0f)
                        Text(
                            modifier = Modifier.padding(
                                top = 5.dp,
                                bottom = 5.dp,
                                start = cellWidth.toInt().dp,
                                end = cellWidth.toInt().dp
                            ),
                            text = cell.length.toString(),
                            color = cell.invertedColour(),
                            style = TextStyle(
                                fontSize = 48.sp,
                                shadow = Shadow(
                                    color = Color.Black, offset = shadowOffset, blurRadius = 1.5f
                                )
                            )
                        )
                    }
                }
            }
        }
    }
}
