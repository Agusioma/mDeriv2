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
    var textSL: MutableLiveData<String> = MutableLiveData("")
    var textSP: MutableLiveData<String> = MutableLiveData("")
    var textMul: MutableLiveData<Double> = MutableLiveData(100.0)
   /* var textStake by rememberSaveable { mutableStateOf("") }
    var textSL by rememberSaveable { mutableStateOf("") }
    var textSP by rememberSaveable { mutableStateOf("") }
    var textMul by rememberSaveable { mutableStateOf(100.0) }*/

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
            accountBalance.value = streamedBalance.asFloat
            //Log.d("TAG2", "${accountList.value}")
            //Creating JSONObject from String using parser
            //Creating JSONObject from String using parser

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
            //val comparator_queue: Deque<Float> = ArrayDeque()
            //val comparator_queue2: Deque<Float> = java.util.ArrayDeque()

            for ((index, value) in prepopulation_ticks.withIndex()) {

                //var ex:FloatEntry = FloatEntry(x=index.toFloat(),y=value.asFloat)
                comparator_queue.value.add(value.asFloat)

                de_que.value?.add(

                    FloatEntry(x = index.toFloat(), y = value.asFloat)

                )

                /*if(index >= 3560){
                    comparator_queue2.add(value.asFloat)
                }*/

            }

            /*val nullArray: Array<Float?> = arrayOfNulls<Float>(40)
            var idxDum: MutableState<Int> = mutableIntStateOf(0)
            for(i in 299..3559){

                nullArray[idxDum.value] = prepopulation_ticks[i].asFloat
                idxDum.value = idxDum.value + 1
            }*/
            //Log.d("o", comparator_queue2.size.toString())
            /*
                private var GENERATOR_X_RANGE_TOP : MutableState<Int> = mutableIntStateOf(300)
    private var GENERATOR_Y_RANGE_BOTTOM : MutableState<Int> = mutableIntStateOf(1000)
    private var GENERATOR_Y_RANGE_TOP : MutableState<Int> = mutableIntStateOf(4500)
    private var UPDATE_FREQUENCY : MutableState<Long> = mutableLongStateOf(1000L)pin
             */
            GENERATOR_Y_RANGE_BOTTOM.value = Collections.min(comparator_queue.value)
            GENERATOR_Y_RANGE_TOP.value = Collections.max(comparator_queue.value)
            //System.out.println(Collections.max(de_que));
            //Collections.max()
            ///var coll = prepopulation_ticks.maxOf { it  }\

            Log.d("TICKS-MAX", java.util.Collections.max(comparator_queue.value).toString())
            Log.d("TICKS-MIN", java.util.Collections.min(comparator_queue.value).toString())
            Log.d("TICKS", de_que.value.toString())
            Log.d("TICKS", prepopulation_ticks.toString())


            var l: MutableState<List<FloatEntry>> = mutableStateOf( ArrayList(de_que.value))
            customStepChartEntryModelProducer.setEntries(l.value)
            _messages.value = message
        }
    }

    fun processTickStream(message: String) = viewModelScope.launch(Dispatchers.Main) {
        if (_socketStatus.value == true) {

            val parser = JsonParser().parse(message).asJsonObject
            var streamed_tick = parser.get("tick").asJsonObject.get("quote")
            Log.d("----", "-----------------------------------")
            Log.d("DE_QUE LEN:", de_que.value?.size.toString())
            Log.d("FIRST ELEM:", de_que.value?.first().toString())
            Log.d("LAST ELEM:", de_que.value?.last().toString())

            Log.e("comparator_queue:", comparator_queue.value?.size.toString())
            Log.e("comparator_queue:", comparator_queue.value?.first().toString())
            Log.e("comparator_queue:", comparator_queue.value?.last().toString())


          /*  if (de_que.value?.size!! > 299) {

                Log.d("STREAMED TICK", streamed_tick.toString())
                for (i in 1..10){
                    de_que.value?.remove();
                    comparator_queue.value?.remove();
                }
                //de_que.value?.remove();
                Log.d("DE_QUE LEN2:", de_que.value?.size.toString())
                //Log.d("FIRST ELEM:", de_que.value?.first().toString())
                //Log.d("LAST ELEM:", de_que.value?.last().toString())
                de_que.value?.add(
                    FloatEntry(x = 299.toFloat(), y = streamed_tick.asFloat)
                )
                Log.d("DE_QUE LEN3:", de_que.value?.size.toString())
                Log.d("FIRST ELEM:", de_que.value?.first().toString())
                Log.d("LAST ELEM:", de_que.value?.last().toString())

                comparator_queue.value.add(streamed_tick.asFloat)

                Log.e("comparator_queue:", comparator_queue.value?.size.toString())
                Log.e("comparator_queue:", comparator_queue.value?.first().toString())
                Log.e("comparator_queue:", comparator_queue.value?.last().toString())

            }else{*/
                de_que.value?.add(
                    FloatEntry(x = 299.toFloat(), y = streamed_tick.asFloat)
                )
                Log.d("DE_QUE LEN4:", de_que.value?.size.toString())
                Log.d("FIRST ELEM4:", de_que.value?.first().toString())
                Log.d("LAST ELEM4:", de_que.value?.last().toString())

                comparator_queue.value.add(streamed_tick.asFloat)

                Log.e("comparator_queue:", comparator_queue.value?.size.toString())
                Log.e("comparator_queue:", comparator_queue.value?.first().toString())
                Log.e("comparator_queue:", comparator_queue.value?.last().toString())
           // }


            Log.d("****", "*****************************************")
            /*for ((index, value) in prepopulation_ticks.withIndex()) {

                comparator_queue.value.add(value.asFloat)
                de_que.value?.add(
                    FloatEntry(x = index.toFloat(), y = value.asFloat)
                )
            }*/

          GENERATOR_Y_RANGE_BOTTOM.value = Collections.min(comparator_queue.value)
            GENERATOR_Y_RANGE_TOP.value = Collections.max(comparator_queue.value)

            /* Log.d("TICKS-MAX", java.util.Collections.max(comparator_queue.value).toString())
             Log.d("TICKS-MIN", java.util.Collections.min(comparator_queue.value).toString())
             Log.d("TICKS", de_que.value.toString())*/
            var l: MutableState<List<FloatEntry>> = mutableStateOf( ArrayList(de_que.value))
            customStepChartEntryModelProducer.setEntries(l.value)
            //delay(UPDATE_FREQUENCY.value)
            _messages.value = message
        }
    }

    fun setStatus(status: Boolean) = viewModelScope.launch(Dispatchers.Main) {
        _socketStatus.value = status
    }

    //ws methods - END
    /*init {
        viewModelScope.launch {
            while (currentCoroutineContext().isActive) {

                var l: List<FloatEntry> = ArrayList(de_que.value)
                customStepChartEntryModelProducer.setEntries(l)
                delay(UPDATE_FREQUENCY.value)
            }
        }
    }*/

    private companion object {
        const val MULTI_ENTRIES_COMBINED = 3

    }
}
