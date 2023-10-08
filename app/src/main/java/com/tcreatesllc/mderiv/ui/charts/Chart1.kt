package com.tcreatesllc.mderiv.ui.charts

import android.graphics.Typeface
import android.util.Log
import androidx.compose.animation.core.spring
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.patrykandpatrick.vico.core.chart.decoration.ThresholdLine
import com.patrykandpatrick.vico.core.chart.layout.HorizontalLayout
import com.patrykandpatrick.vico.core.chart.scale.AutoScaleUp
import com.patrykandpatrick.vico.core.chart.values.AxisValuesOverrider
import com.patrykandpatrick.vico.core.component.shape.Shapes
import com.patrykandpatrick.vico.core.entry.ChartEntryModel
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.scroll.AutoScrollCondition
import com.patrykandpatrick.vico.core.scroll.InitialScroll
import com.tcreatesllc.mderiv.viewmodels.MainViewModel
import kotlinx.coroutines.launch


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

@Composable
fun ComposeChart2(
    chartEntryModelProducer: ChartEntryModelProducer,
    viewModel: MainViewModel/*, mods: Modifier*/
) {
    //var hey = viewModel.clickedContractList.observeAsState().value?.get(8)?.toFloat()
    var hey = viewModel.clickedContractThresholdMarker.observeAsState().value
    val coroutineScope = rememberCoroutineScope()

    //clickedContractList.value?.get(8)?
   // viewModel.clickedContractList.value?.get(8)?.observeAsState().value?.let {

    //var hey = 3114f
    Log.i("lll", hey.toString())
    var hey2 = hey
    val marker = rememberMarker()

    val thresholdLine =  rememberThresholdLine(viewModel)
    LaunchedEffect(hey){
        this.launch {
            Log.i("lll", hey.toString())
        }
    }
    ProvideChartStyle(rememberChartStyle(chartColors)) {

        var defaultLines = currentChartStyle.lineChart.lines
        val lineChart = lineChart(
            remember(defaultLines) {
                defaultLines.map { defaultLine -> defaultLine.copy(pointConnector = pointConnector) }
            },
            decorations = remember(thresholdLine) { listOf(thresholdLine) },
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

@Composable
private fun rememberThresholdLine(viewModel: MainViewModel): ThresholdLine {
    var tt: MutableState<Float> = remember{ mutableStateOf(3114f)}

    if (viewModel.clickedContractThresholdMarker.observeAsState().value != null) {
       tt.value = viewModel.clickedContractThresholdMarker.observeAsState().value!!
    }
    val line = shapeComponent(color = color2)
    val label = textComponent(
        color = Color.Black,
        background = shapeComponent(Shapes.pillShape, color2),
        padding = thresholdLineLabelPadding,
        margins = thresholdLineLabelMargins,
        typeface = Typeface.MONOSPACE,
    )
    return remember(line, label) {

            ThresholdLine(thresholdValue = tt.value, lineComponent = line, labelComponent = label)

    }
}

private const val COLOR_1_CODE = 0xffa485e0
private const val PERSISTENT_MARKER_X = 599f

private const val COLOR_2_CODE = 0xffd3d826
private val color1 = Color(COLOR_1_CODE)
private val color2 = Color(COLOR_2_CODE)
private val chartColors = listOf(color1)
private val pointConnector = DefaultPointConnector(cubicStrength = 0f)

private val axisTitleHorizontalPaddingValue = 8.dp
private val axisTitleVerticalPaddingValue = 2.dp
private val axisTitlePadding = dimensionsOf(axisTitleHorizontalPaddingValue, axisTitleVerticalPaddingValue)
private val axisTitleMarginValue = 4.dp
private val bottomAxisTitleMargins = dimensionsOf(top = axisTitleMarginValue)

private val thresholdLineLabelMarginValue = 4.dp
private val thresholdLineLabelHorizontalPaddingValue = 8.dp
private val thresholdLineLabelVerticalPaddingValue = 2.dp
private val thresholdLineLabelPadding =
    dimensionsOf(thresholdLineLabelHorizontalPaddingValue, thresholdLineLabelVerticalPaddingValue)
private val thresholdLineLabelMargins = dimensionsOf(thresholdLineLabelMarginValue)
