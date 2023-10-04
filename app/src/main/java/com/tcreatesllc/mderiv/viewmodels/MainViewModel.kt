package com.tcreatesllc.mderiv.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.patrykandpatrick.vico.core.entry.ChartEntryModel
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.composed.ComposedChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.composed.plus
import com.patrykandpatrick.vico.core.util.RandomEntriesGenerator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    //websockets-START
    private val _socketStatus = MutableLiveData(false)
    val socketStatus: LiveData<Boolean> = _socketStatus

    private val _messages = MutableLiveData<String>()
    val messages: LiveData<String> = _messages
    //websockets- END

    private val generator = RandomEntriesGenerator(
        xRange = 0..GENERATOR_X_RANGE_TOP,
        yRange = GENERATOR_Y_RANGE_BOTTOM..GENERATOR_Y_RANGE_TOP,
    )

    private val customStepGenerator = RandomEntriesGenerator(
        xRange = IntProgression.fromClosedRange(rangeStart = 0, rangeEnd = GENERATOR_X_RANGE_TOP, step = 2),
        yRange = GENERATOR_Y_RANGE_BOTTOM..GENERATOR_Y_RANGE_TOP,
    )

    internal val chartEntryModelProducer: ChartEntryModelProducer = ChartEntryModelProducer()

    internal val customStepChartEntryModelProducer: ChartEntryModelProducer = ChartEntryModelProducer()

    internal val multiDataSetChartEntryModelProducer: ChartEntryModelProducer = ChartEntryModelProducer()

    internal val composedChartEntryModelProducer: ComposedChartEntryModelProducer<ChartEntryModel> =
        multiDataSetChartEntryModelProducer + chartEntryModelProducer

//ws methods - START
    fun addMessage(message:  String) = viewModelScope.launch(Dispatchers.Main) {
        if (_socketStatus.value == true) {
            _messages.value = message
        }
    }

    fun setStatus(status: Boolean) = viewModelScope.launch(Dispatchers.Main) {
        _socketStatus.value = status
    }

    //ws methods - END
    init {
        viewModelScope.launch {
            while (currentCoroutineContext().isActive) {
                chartEntryModelProducer.setEntries(generator.generateRandomEntries())
                multiDataSetChartEntryModelProducer.setEntries(
                    entries = List(size = MULTI_ENTRIES_COMBINED) {
                        generator.generateRandomEntries()
                    },
                )
                customStepChartEntryModelProducer.setEntries(customStepGenerator.generateRandomEntries())
                delay(UPDATE_FREQUENCY)
            }
        }
    }

    private companion object {
        const val MULTI_ENTRIES_COMBINED = 3
        const val GENERATOR_X_RANGE_TOP = 300
        const val GENERATOR_Y_RANGE_BOTTOM = 1000
        const val GENERATOR_Y_RANGE_TOP = 4500
        const val UPDATE_FREQUENCY = 1000L
    }
}