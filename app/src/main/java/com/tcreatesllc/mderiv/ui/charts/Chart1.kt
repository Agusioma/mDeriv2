package com.tcreatesllc.mderiv.ui.charts

import androidx.compose.animation.core.spring
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberEndAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.chart.scroll.ChartScrollSpec
import com.patrykandpatrick.vico.compose.chart.scroll.rememberChartScrollState
import com.patrykandpatrick.vico.compose.style.ProvideChartStyle
import com.patrykandpatrick.vico.compose.style.currentChartStyle
import com.patrykandpatrick.vico.core.chart.DefaultPointConnector
import com.patrykandpatrick.vico.core.chart.copy
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.scroll.AutoScrollCondition
import com.patrykandpatrick.vico.core.scroll.InitialScroll


@Composable
fun ComposeChart1(chartEntryModelProducer: ChartEntryModelProducer/*, mods: Modifier*/) {
    val marker = rememberMarker()
    ProvideChartStyle(rememberChartStyle(chartColors)) {

        var defaultLines = currentChartStyle.lineChart.lines
        val lineChart = lineChart(
            remember(defaultLines) {
                defaultLines.map { defaultLine -> defaultLine.copy(pointConnector = pointConnector) }
            },
            persistentMarkers = remember(marker) {
                mapOf(PERSISTENT_MARKER_X to marker)
            }
            )
        Chart(

            chart = remember(lineChart) {lineChart },
            chartModelProducer = chartEntryModelProducer,
            endAxis = rememberEndAxis(),
            startAxis = rememberStartAxis(guideline = null, axis = null, tick=null, label=null),
            bottomAxis = rememberBottomAxis(  axis = null, tick=null, label=null),
            marker = marker,
            runInitialAnimation = false,
            isZoomEnabled = true,
            chartScrollSpec = ChartScrollSpec(initialScroll = InitialScroll.End,
                isScrollEnabled = true,
                autoScrollCondition = AutoScrollCondition.OnModelSizeIncreased,
                autoScrollAnimationSpec = spring()
                )
        )
    }
}

private const val COLOR_1_CODE = 0xffa485e0
private const val PERSISTENT_MARKER_X = 300f

private val color1 = Color(COLOR_1_CODE)
private val chartColors = listOf(color1)
private val pointConnector = DefaultPointConnector(cubicStrength = 0f)