package com.daymax.crochetpixels

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.*
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import kotlin.math.roundToInt
import kotlin.math.sqrt

@RequiresApi(Build.VERSION_CODES.O)
class HomeViewModel : ViewModel() {

    private val dataCells = mutableStateListOf<DataCell>()
    val rows = mutableStateListOf<GridRow>()
    var fetchingData by mutableStateOf(true)

    init {
        getDataCells()
    }

    interface PixelsApi {
        @GET("data.json")
        suspend fun getDataCells(): MutableList<DataCell>
    }

    private fun getDataCells() {
        viewModelScope.launch {
            fetchingData = true

            val BASE_URL =
                "https://daymax.co.uk/"

            val pixelsService: PixelsApi = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(PixelsApi::class.java)

            val response = pixelsService.getDataCells()
            dataCells.addAll(response)
        }.invokeOnCompletion {
            fetchingData = false
            convertToCells()
        }
    }

    private fun convertToCells() {
        val gridCells: MutableList<Cell> = mutableListOf()

        val i: Int =
            sqrt(dataCells.size.toDouble()).roundToInt() // Width of image (assuming it's a square!)

        var n: Int = 1 // Number of consecutive pixels of same colour
        var previousCell: DataCell? = null
        var currentCell = Cell(-1, -1, Color.Black, -1)

        dataCells.forEach { dc ->
            // The dataCells are all in the right order already so they can just be looped through
            previousCell = dataCells.elementAtOrNull(dataCells.indexOf(dc) - 1)
            if (previousCell == null) {
                // Must be the first cell so set to current cell for colour comparison purposes
                previousCell = dc
            } else {
                currentCell = Cell(
                    x = previousCell!!.x,
                    y = previousCell!!.y,
                    colour = Color(
                        red = previousCell!!.colour[0],
                        green = previousCell!!.colour[1],
                        blue = previousCell!!.colour[2],
                    ),
                    length = n
                )

                if (previousCell!!.x >= i - 1) {
                    // End of row
                    // Commit current cell
                    gridCells.add(currentCell)
                    n = 1
                    Log.d("", "Row number ${dc.y} done")
                } else {
                    if (previousCell!!.colour == dc.colour) {
                        n++
                    } else {
                        // New colour so commit cell to row
                        gridCells.add(currentCell)
                        n = 1
                    }
                }
            }
        }.apply {
            gridCells.add(
                Cell(
                    x = currentCell!!.x,
                    y = currentCell!!.y,
                    colour = currentCell!!.colour,
                    length = n
                )
            )
        }

        var k = 0
        val tempList = mutableListOf<Cell>()
        gridCells.forEach { gc ->
            Log.v("", "Grid cell of length ${gc.length} being added")
            tempList.add(gc)
            k += gc.length
            if (k >= i) {
                rows.add(
                    GridRow.createFromCells(tempList)
                )
                Log.wtf("", "Row being committed, k = ${k}")
                tempList.clear()
                k = 0
            }
        }

        Log.d("rows", "Rows.size = " + "${rows.size}")
    }
}

data class Cell(
    val x: Int,
    val y: Int,
    val colour: Color,
    val length: Int,
) {
    fun invertedColour(): Color {
        return colour.copy(
            alpha = 1f,
            red = 1 - colour.red,
            green = 1 - colour.green,
            blue = 1 - colour.blue,
        )
    }
}

class GridRow private constructor(
    val cells: List<Cell>,
) {
    companion object {
        fun createFromCells(
            input: List<Cell>,
        ): GridRow = GridRow(input.toList())
    }
}

data class DataCell(
    @SerializedName("x") var x: Int,
    @SerializedName("y") var y: Int,
    @SerializedName("colour") var colour: MutableList<Int> = mutableListOf()
)
