package com.tcreatesllc.mderiv.viewmodels

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonArray
import com.google.gson.JsonParser
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.FloatEntry
import com.patrykandpatrick.vico.core.util.RandomEntriesGenerator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.json.JSONArray
import java.util.*

class MainViewModel : ViewModel() {

    private var GENERATOR_X_RANGE_TOP: MutableState<Int> = mutableIntStateOf(3600)
    var GENERATOR_Y_RANGE_BOTTOM: MutableState<Float> = mutableFloatStateOf(0.0f)
    private var GENERATOR_Y_RANGE_TOP: MutableState<Float> = mutableFloatStateOf(0.0f)
    private var UPDATE_FREQUENCY: MutableState<Long> = mutableLongStateOf(1000L)
    var preAyy: MutableState<JsonArray> = mutableStateOf(JsonArray())

    //websockets-START
    private val _socketStatus = MutableLiveData(false)
    val socketStatus: LiveData<Boolean> = _socketStatus

    var currentTradeSymbol: MutableLiveData<String> = MutableLiveData("1HZ100V")
    var currentTradeSymbolKey: MutableLiveData<String> = MutableLiveData("Volatility 100 (1s) Index")
    var textStake: MutableLiveData<String> = MutableLiveData("")
    var textSL: MutableLiveData<String> = MutableLiveData("0.10")
    var textSP: MutableLiveData<String> = MutableLiveData("0.10")
    var textMul: MutableLiveData<String> = MutableLiveData("10")
    var textOption: MutableLiveData<String> = MutableLiveData("MULTUP")
    var tradeIt: MutableLiveData<Boolean> = MutableLiveData(false)

    private val _messages = MutableLiveData<String>()
    val messages: LiveData<String> = _messages
    //websockets- END

    var tempList: MutableSet<Map<String, String>> = mutableSetOf()

    //var tempListTicks: MutableState<Queue<Float>> = mutableStateOf(java.util.ArrayDeque())
    var accountList: MutableLiveData<Set<Map<String, String>>> = MutableLiveData(setOf())
    var accountBalance: MutableLiveData<Float> = MutableLiveData(0.0f)
    var accountCurr: MutableLiveData<String> = MutableLiveData("EUR")
    var comparator_queue: MutableState<Deque<Float>> = mutableStateOf(ArrayDeque())

    val de_que: MutableLiveData<Queue<FloatEntry>> = MutableLiveData(ArrayDeque())

    internal val customStepChartEntryModelProducer: ChartEntryModelProducer =
        ChartEntryModelProducer()


    //ws methods - START
    fun addAuthDetails(message: String) = viewModelScope.launch(Dispatchers.Main) {
        if (_socketStatus.value == true) {
            val parser = JsonParser().parse(message).asJsonObject
            var balance = parser.get("authorize").asJsonObject.get("balance")
            var accCurr = parser.get("authorize").asJsonObject.get("currency")
            var accList = parser.get("authorize").asJsonObject.get("account_list").asJsonArray
            accountCurr.value = accCurr.asString
            accountBalance.value = balance.asFloat
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


            _messages.value = message
        }
    }

    fun addBalanceStream(message: String) = viewModelScope.launch(Dispatchers.Main) {
        if (_socketStatus.value == true) {
            val parser = JsonParser().parse(message).asJsonObject
            var streamedBalance = parser.get("balance").asJsonObject.get("balance")

            accountBalance.value = streamedBalance.asFloat

            _messages.value = message
        }
    }

    fun addPrepopulationTicks(message: String) = viewModelScope.launch(Dispatchers.Main) {
        if (_socketStatus.value == true) {
            de_que.value?.clear()
            comparator_queue.value.clear()
            val parser = JsonParser().parse(message).asJsonObject
            var prepopulation_ticks = parser.get("history").asJsonObject.get("prices").asJsonArray
            var preAyy = prepopulation_ticks
            preAyy?.get(0)?.let { Log.d("LOGGG", it.toString()) }

            for ((index, value) in prepopulation_ticks.withIndex()) {


                comparator_queue.value.add(value.asFloat)

                de_que.value?.add(

                    FloatEntry(x = index.toFloat(), y = value.asFloat)

                )



            }

            GENERATOR_Y_RANGE_BOTTOM.value = Collections.min(comparator_queue.value)
            GENERATOR_Y_RANGE_TOP.value = Collections.max(comparator_queue.value)

            var l: MutableState<List<FloatEntry>> = mutableStateOf( ArrayList(de_que.value))
            customStepChartEntryModelProducer.setEntries(l.value)
            _messages.value = message
        }
    }

    fun processTickStream(message: String) = viewModelScope.launch(Dispatchers.Main) {
        if (_socketStatus.value == true) {

            val parser = JsonParser().parse(message).asJsonObject
            var streamed_tick = parser.get("tick").asJsonObject.get("quote")

                de_que.value?.add(
                    FloatEntry(x = 299.toFloat(), y = streamed_tick.asFloat)
                )


                comparator_queue.value.add(streamed_tick.asFloat)




          GENERATOR_Y_RANGE_BOTTOM.value = Collections.min(comparator_queue.value)
            GENERATOR_Y_RANGE_TOP.value = Collections.max(comparator_queue.value)


            var l: MutableState<List<FloatEntry>> = mutableStateOf( ArrayList(de_que.value))
            customStepChartEntryModelProducer.setEntries(l.value)

            _messages.value = message
        }
    }

    fun setStatus(status: Boolean) = viewModelScope.launch(Dispatchers.Main) {
        _socketStatus.value = status
    }



    private companion object {
        const val MULTI_ENTRIES_COMBINED = 3

    }
}
