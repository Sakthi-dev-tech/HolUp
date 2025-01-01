package com.adormantsakthi.holup.ui.components.forStatistics

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries

@Composable
fun Graph () {
    val modelProducer = remember { CartesianChartModelProducer() }

    var dates = remember { mutableListOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat") }
    var timeUsed = remember { mutableListOf(1.5f, 2.0f, 1.25f, 3.0f, 2.5f, 0.75f, 1.0f) }

    val verticalAxis = VerticalAxis.rememberStart(
        valueFormatter = {context, value, _ ->
            val hours = value.toInt()
            val minutes = ((value - hours) * 60).toInt()
            "$hours h $minutes m"
        }
    )

    val horizontalAxis = HorizontalAxis.rememberBottom(
        valueFormatter = { context, index, _ ->
            dates.getOrNull(index.toInt()) ?: ""
        }
    )


    LaunchedEffect(Unit) {
        modelProducer.runTransaction { lineSeries { series(timeUsed) } }
    }
    CartesianChartHost(
        rememberCartesianChart(
            rememberLineCartesianLayer(),
            startAxis = verticalAxis,
            bottomAxis = horizontalAxis,
        ),
        modelProducer,
    )
}