package com.tcreatesllc.mderiv.ui.charts

import android.graphics.Typeface
import androidx.compose.animation.core.spring
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberEndAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.chart.scroll.ChartScrollSpec
import com.patrykandpatrick.vico.compose.chart.scroll.rememberChartScrollState
import com.patrykandpatrick.vico.compose.component.shapeComponent
import com.patrykandpatrick.vico.compose.component.textComponent
import com.patrykandpatrick.vico.compose.dimensions.dimensionsOf
import com.patrykandpatrick.vico.compose.style.ProvideChartStyle
import com.patrykandpatrick.vico.compose.style.currentChartStyle
import com.patrykandpatrick.vico.core.axis.AxisItemPlacer
import com.patrykandpatrick.vico.core.axis.AxisPosition
import com.patrykandpatrick.vico.core.axis.formatter.AxisValueFormatter
import com.patrykandpatrick.vico.core.chart.DefaultPointConnector
import com.patrykandpatrick.vico.core.chart.copy
import com.patrykandpatrick.vico.core.chart.layout.HorizontalLayout
import com.patrykandpatrick.vico.core.chart.scale.AutoScaleUp
import com.patrykandpatrick.vico.core.chart.values.AxisValuesOverrider
import com.patrykandpatrick.vico.core.component.shape.Shapes
import com.patrykandpatrick.vico.core.entry.ChartEntryModel
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.scroll.AutoScrollCondition
import com.patrykandpatrick.vico.core.scroll.InitialScroll
import com.tcreatesllc.mderiv.viewmodels.MainViewModel


@Composable
fun ComposeChart1(
    chartEntryModelProducer: ChartEntryModelProducer,
    viewModel: MainViewModel/*, mods: Modifier*/
) {
    val marker = rememberMarker()
    ProvideChartStyle(rememberChartStyle(chartColors)) {

        var defaultLines = currentChartStyle.lineChart.lines
        val lineChart = lineChart(
            remember(defaultLines) {
                defaultLines.map { defaultLine -> defaultLine.copy(pointConnector = pointConnector) }
            },
            persistentMarkers = remember(marker) {
                mapOf(PERSISTENT_MARKER_X to marker)
            },
            axisValuesOverrider = AxisValuesOverrider.fixed(
                minY = viewModel.GENERATOR_Y_RANGE_BOTTOM.value,
                maxX = 605f
            )
        )
        Chart(

            chart = remember(lineChart) { lineChart },
            chartModelProducer = chartEntryModelProducer,
            endAxis = rememberEndAxis(guideline = null),
            //startAxis = rememberStartAxis(guideline = null, axis = null, tick=null, label=null),
            bottomAxis = rememberBottomAxis(
                tick = null,
                guideline = null,
                label = null,
                axis = null,
                titleComponent = textComponent(
                    color = MaterialTheme.colorScheme.primary,
                    padding = axisTitlePadding,
                    margins = bottomAxisTitleMargins,
                    typeface = Typeface.MONOSPACE,
                ),
            title = "10-minute tick interval"
            ),
            marker = marker,
            runInitialAnimation = false,
            isZoomEnabled = true,
            chartScrollSpec = ChartScrollSpec(
                initialScroll = InitialScroll.End,
                isScrollEnabled = false,
                autoScrollCondition = AutoScrollCondition.OnModelSizeIncreased,
                autoScrollAnimationSpec = spring()
            )
        )
    }
}

private const val COLOR_1_CODE = 0xffa485e0
private const val PERSISTENT_MARKER_X = 599f

private val color1 = Color(COLOR_1_CODE)
private val chartColors = listOf(color1)
private val pointConnector = DefaultPointConnector(cubicStrength = 0f)

private val axisTitleHorizontalPaddingValue = 8.dp
private val axisTitleVerticalPaddingValue = 2.dp
private val axisTitlePadding = dimensionsOf(axisTitleHorizontalPaddingValue, axisTitleVerticalPaddingValue)
private val axisTitleMarginValue = 4.dp
private val bottomAxisTitleMargins = dimensionsOf(top = axisTitleMarginValue)