package com.tcreatesllc.mderiv.viewmodels

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonParser
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.FloatEntry
import com.patrykandpatrick.vico.core.util.RandomEntriesGenerator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.*

class MainViewModel : ViewModel() {

    private var GENERATOR_X_RANGE_TOP : MutableState<Int> = mutableIntStateOf(3600)
    var GENERATOR_Y_RANGE_BOTTOM : MutableState<Float> = mutableFloatStateOf(0.0f)
    private var GENERATOR_Y_RANGE_TOP : MutableState<Float> = mutableFloatStateOf(0.0f)
    private var UPDATE_FREQUENCY : MutableState<Long> = mutableLongStateOf(1000L)

    //websockets-START
    private val _socketStatus = MutableLiveData(false)
    val socketStatus: LiveData<Boolean> = _socketStatus

    private val _messages = MutableLiveData<String>()
    val messages: LiveData<String> = _messages
    //websockets- END

    var tempList: MutableSet<Map<String, String>> = mutableSetOf()
    //var tempListTicks: MutableState<Queue<Float>> = mutableStateOf(java.util.ArrayDeque())
    var accountList: MutableLiveData<Set<Map<String, String>>> = MutableLiveData(setOf())
    var accountBalance: MutableLiveData<Float> = MutableLiveData(0.0f)
    var accountCurr: MutableLiveData<String> = MutableLiveData("EUR")

    val de_que: MutableLiveData<Queue<FloatEntry>> = MutableLiveData(java.util.ArrayDeque())


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

    fun addPrepopulationTicks(message: String) = viewModelScope.launch(Dispatchers.Main) {
        if (_socketStatus.value == true) {

            val parser = JsonParser().parse(message).asJsonObject
            var prepopulation_ticks = parser.get("history").asJsonObject.get("prices").asJsonArray

            val comparator_queue: Deque<Float> = java.util.ArrayDeque()
            val comparator_queue2: Deque<Float> = java.util.ArrayDeque()

            for ((index, value) in prepopulation_ticks.withIndex()) {

                //var ex:FloatEntry = FloatEntry(x=index.toFloat(),y=value.asFloat)
                comparator_queue.add(value.asFloat)

                de_que.value?.add(

                    FloatEntry(x=index.toFloat(),y=value.asFloat)

                )

                if(index >= 3560){
                    comparator_queue2.add(value.asFloat)
                }

            }

            /*val nullArray: Array<Float?> = arrayOfNulls<Float>(40)
            var idxDum: MutableState<Int> = mutableIntStateOf(0)
            for(i in 3599..3559){

                nullArray[idxDum.value] = prepopulation_ticks[i].asFloat
                idxDum.value = idxDum.value + 1
            }*/
            Log.d("o", comparator_queue2.size.toString())
            /*
                private var GENERATOR_X_RANGE_TOP : MutableState<Int> = mutableIntStateOf(300)
    private var GENERATOR_Y_RANGE_BOTTOM : MutableState<Int> = mutableIntStateOf(1000)
    private var GENERATOR_Y_RANGE_TOP : MutableState<Int> = mutableIntStateOf(4500)
    private var UPDATE_FREQUENCY : MutableState<Long> = mutableLongStateOf(1000L)pin
             */
            GENERATOR_Y_RANGE_BOTTOM.value = Collections.min(comparator_queue)
            GENERATOR_Y_RANGE_TOP.value = Collections.max(comparator_queue)
            //System.out.println(Collections.max(de_que));
            //Collections.max()
            ///var coll = prepopulation_ticks.maxOf { it  }\

            Log.d("TICKS-MAX", java.util.Collections.max(comparator_queue).toString())
            Log.d("TICKS-MIN", java.util.Collections.min(comparator_queue).toString())
            Log.d("TICKS", de_que.value.toString())
            Log.d("TICKS", prepopulation_ticks.toString())
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

                var l: List<FloatEntry> = ArrayList(de_que.value)
                //customStepChartEntryModelProducer.setEntries(l)
                customStepChartEntryModelProducer.setEntries(l)
                //customStepGenerator.hey(l)
                delay(UPDATE_FREQUENCY.value)
            }
        }
    }

    private companion object {
        const val MULTI_ENTRIES_COMBINED = 3

    }
}

private fun RandomEntriesGenerator.hey(arg:List<FloatEntry>): List<FloatEntry>{
    return arg
    TODO("Not yet implemented")
}
