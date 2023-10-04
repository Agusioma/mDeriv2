package com.tcreatesllc.mderiv.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonParser
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

    var tempList: MutableSet<Map<String, String>> = mutableSetOf()
    var accountList: MutableLiveData<Set<Map<String, String>>> = MutableLiveData(setOf())
    var accountBalance: MutableLiveData<Float> = MutableLiveData(0.0f)
    var accountCurr: MutableLiveData<String> = MutableLiveData("EUR")

    private val generator = RandomEntriesGenerator(
        xRange = 0..GENERATOR_X_RANGE_TOP,
        yRange = GENERATOR_Y_RANGE_BOTTOM..GENERATOR_Y_RANGE_TOP,
    )

    private val customStepGenerator = RandomEntriesGenerator(
        xRange = IntProgression.fromClosedRange(
            rangeStart = 0,
            rangeEnd = GENERATOR_X_RANGE_TOP,
            step = 2
        ),
        yRange = GENERATOR_Y_RANGE_BOTTOM..GENERATOR_Y_RANGE_TOP,
    )

    internal val chartEntryModelProducer: ChartEntryModelProducer = ChartEntryModelProducer()

    internal val customStepChartEntryModelProducer: ChartEntryModelProducer =
        ChartEntryModelProducer()

    internal val multiDataSetChartEntryModelProducer: ChartEntryModelProducer =
        ChartEntryModelProducer()

    internal val composedChartEntryModelProducer: ComposedChartEntryModelProducer<ChartEntryModel> =
        multiDataSetChartEntryModelProducer + chartEntryModelProducer

    //ws methods - START
    fun addAuthDetails(message: String) = viewModelScope.launch(Dispatchers.Main) {
        if (_socketStatus.value == true) {
            val parser = JsonParser().parse(message).asJsonObject
            var balance = parser.get("authorize").asJsonObject.get("balance")
            var accCurr = parser.get("authorize").asJsonObject.get("currency")
            var accList = parser.get("authorize").asJsonObject.get("account_list").asJsonArray
            accountCurr.value = accCurr.asString
            accountBalance.value =  balance.asFloat
            for (e in accList) {
                tempList.add(
                    mapOf(
                        Pair("currency", e.asJsonObject.get("currency").toString()),
                        Pair("is_virtual", e.asJsonObject.get("is_virtual").toString()),
                        Pair("loginid", e.asJsonObject.get("loginid").toString()),
                    )
                )

            }

            accountList.value = tempList
            Log.d("TAG2", "${accountList.value}")
            //Creating JSONObject from String using parser
            //Creating JSONObject from String using parser


            _messages.value = message
        }
    }

    fun addBalanceStream(message: String) = viewModelScope.launch(Dispatchers.Main) {
        if (_socketStatus.value == true) {
            val parser = JsonParser().parse(message).asJsonObject
            var streamedBalance = parser.get("balance").asJsonObject.get("balance")
           // Log.d("TAG2", "${accountList.value}")
            accountBalance.value =  streamedBalance.asFloat
            //Log.d("TAG2", "${accountList.value}")
            //Creating JSONObject from String using parser
            //Creating JSONObject from String using parser

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