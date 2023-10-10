package com.tcreatesllc.mderiv.viewmodels

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonArray
import com.google.gson.JsonParser
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.FloatEntry
import com.tcreatesllc.mderiv.MainActivity
import com.tcreatesllc.mderiv.MainApplication
import com.tcreatesllc.mderiv.storage.TemporaryTokens
import com.tcreatesllc.mderiv.storage.TransactionDetails
import com.tcreatesllc.mderiv.storage.repositories.ContractsRepository
import com.tcreatesllc.mderiv.websockets.MainSocket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import java.lang.Exception
import java.util.ArrayDeque
import java.util.Collections
import java.util.Deque
import java.util.Queue

class MainViewModel(private val contractsRepository: ContractsRepository) : ViewModel() {


    private var GENERATOR_X_RANGE_TOP: MutableState<Int> = mutableIntStateOf(3600)
    var GENERATOR_Y_RANGE_BOTTOM: MutableState<Float> = mutableFloatStateOf(0.0f)
    private var GENERATOR_Y_RANGE_TOP: MutableState<Float> = mutableFloatStateOf(0.0f)
    private var UPDATE_FREQUENCY: MutableState<Long> = mutableLongStateOf(1000L)
    var preAyy: MutableState<JsonArray> = mutableStateOf(JsonArray())

    //websockets-START
    private val _socketStatus = MutableLiveData(false)
    val socketStatus: LiveData<Boolean> = _socketStatus

    var currentTradeSymbol: MutableLiveData<String> = MutableLiveData("1HZ100V")
    var currentTradeSymbolKey: MutableLiveData<String> =
        MutableLiveData("Volatility 100 (1s) Index")
    var textStake: MutableLiveData<String> = MutableLiveData("")
    var textSL: MutableLiveData<String> = MutableLiveData("0.10")
    var textSP: MutableLiveData<String> = MutableLiveData("0.10")
    var textMul: MutableLiveData<String> = MutableLiveData("10")
    var textOption: MutableLiveData<String> = MutableLiveData("MULTUP")
    var tradeIt: MutableLiveData<Boolean> = MutableLiveData(false)
    var cancelIt: MutableLiveData<Boolean> = MutableLiveData(false)
    var subcribeIt: MutableLiveData<Boolean> = MutableLiveData(false)
    var userAuthTokenTemp: MutableLiveData<String> = MutableLiveData("")
    var userLoginID: MutableLiveData<String> = MutableLiveData("")
    private val _messages = MutableLiveData<String>()
    val messages: LiveData<String> = _messages
    //websockets- END


    var textIndicativeAmt: MutableLiveData<String> = MutableLiveData("0")
    var textProfitOrLoss: MutableLiveData<String> = MutableLiveData("0")
    var textStatus: MutableLiveData<String> = MutableLiveData("0")
    var boolFire: MutableLiveData<Boolean> = MutableLiveData(false)
    var refreshIt: MutableLiveData<Boolean> = MutableLiveData(false)
    var stopIt: MutableLiveData<Boolean> = MutableLiveData(false)
    var tempList: MutableSet<Map<String, String>> = mutableSetOf()

    //var tempListTicks: MutableState<Queue<Float>> = mutableStateOf(java.util.ArrayDeque())
    var accountList: MutableLiveData<Set<Map<String, String>>> = MutableLiveData(setOf())
    var accountBalance: MutableLiveData<Float> = MutableLiveData(0.0f)
    var accountCurr: MutableLiveData<String> = MutableLiveData("EUR")
    var comparator_queue: MutableState<Deque<Float>> = mutableStateOf(ArrayDeque())

    val de_que: MutableLiveData<Queue<FloatEntry>> = MutableLiveData(ArrayDeque())
    val listItemsMarkets: Map<String, String> = mapOf(
        Pair("AUD/JPY", "frxAUDJPY"),
        Pair("AUD/USD", "frxAUDUSD"),
        Pair("EUR/AUD", "frxEURAUD"),
        Pair("EUR/CHF", "frxEURCHF"),
        Pair("EUR/GBP", "frxEURGBP"),
        Pair("EUR/JPY", "frxEURJPY"),
        Pair("EUR/USD", "frxEURUSD"),
        Pair("GBP/AUD", "frxGBPAUD"),
        Pair("GBP/JPY", "frxGBPJPY"),
        Pair("GBP/USD", "frxGBPUSD"),
        Pair("USD/CAD", "frxUSDCAD"),
        Pair("USD/CHF", "frxUSDCHF"),
        Pair("USD/JPY", "frxUSDJPY"),
        Pair("Volatility 10 Index", "R_10"),
        Pair("Volatility 10 (1s) Index", "1HZ10V"),
        Pair("Volatility 25 Index", "R_25"),
        Pair("Volatility 25 (1s) Index", "1HZ25V"),
        Pair("Volatility 50 Index", "R_50"),
        Pair("Volatility 50 (1s) Index", "1HZ50V"),
        Pair("Volatility 75 Index", "R_75"),
        Pair("Volatility 75 (1s) Index", "1HZ75V"),
        Pair("Volatility 100 Index", "R_100"),
        Pair("Volatility 100 (1s) Index", "1HZ100V"),
        Pair("Boom 1000 Index", "BOOM1000"),
        Pair("Boom 500 Index", "BOOM500"),
        Pair("Crash 1000 Index", "CRASH1000"),
        Pair("Crash 500 Index", "CRASH500"),
        Pair("Jump 10 Index", "JD10"),
        Pair("Jump 25 Index", "JD25"),
        Pair("Jump 50 Index", "JD50"),
        Pair("Jump 75 Index", "JD75"),
        Pair("Jump 100 Index", "JD100"),
        Pair("Step Index", "stpRNG"),
        Pair("Gold Basket", "WLDXAU"),
        Pair("AUD Basket", "WLDAUD"),
        Pair("EUR Basket", "WLDEUR"),
        Pair("GBP Basket", "WLDGBP"),
        Pair("USD Basket", "WLDUSD"),
        Pair("BTC/USD", "cryBTCUSD"),
        Pair("ETH/USD", "cryETHUSD"),
    )

    var openDialogError: MutableLiveData<Boolean> = MutableLiveData(false)
    var errorMessage: MutableLiveData<String> = MutableLiveData("")
    var dialogTitle: MutableLiveData<String> = MutableLiveData("")

    internal val customStepChartEntryModelProducer: ChartEntryModelProducer =
        ChartEntryModelProducer()

    var listOpenPositions2: MutableLiveData<Map<String, List<String>>> =
        MutableLiveData(mutableMapOf())
    val listOpenPositions: MutableLiveData<Queue<Map<String?, List<String>>>> =
        MutableLiveData(ArrayDeque())

    var clickedContractList: MutableLiveData<List<String>> = MutableLiveData(listOf())
    val clickedContractDetails: MutableLiveData<List<String>> = MutableLiveData(listOf())
    var clickedContractThresholdMarker: MutableLiveData<Float> = MutableLiveData(0.0f)
    var streamContract: MutableLiveData<String> = MutableLiveData("NO")
    var clickedContractID: MutableLiveData<String> = MutableLiveData("")
    var accTokenMapping: MutableLiveData<Map<String, String>> = MutableLiveData(mapOf())

    var prevSubscriptionID: MutableLiveData<String> = MutableLiveData("NNN")
    private lateinit var authWSlistener: WebSocketListener
    private val okHttpClient = OkHttpClient()
    private var authWebSocket: WebSocket? = null

    private fun initWebSocketSession(): Request {
        val websocketURL = "wss://ws.derivws.com/websockets/v3?app_id=38697"

        return Request.Builder()
            .url(websocketURL)
            .build()
    }
    fun storeAuthToDB(holderMap: MutableMap<String, String>) =
        viewModelScope.launch(Dispatchers.Main) {
            holderMap.forEach {
                /*contractsRepository.insertTempToken(
                    temporaryTokens = TemporaryTokens(
                        userTradeAccountNo = it.key,
                        userTradeAuthToken = it.value
                    )
                )*/
                Log.i("contractsRepository", "$it")
                contractsRepository.insertTempToken(
                    temporaryTokens = TemporaryTokens(
                        userTradeAccountNo = it.key,
                        userTradeAuthToken = it.value
                    )
                )
            }
        }

    fun getAuthTokenFromDB(loginID: String) = viewModelScope.launch(Dispatchers.Main) {

        try {
            contractsRepository.getAuthToken(loginID).collect {
                Log.i("NOOO", "${it.userTradeAuthToken}")
                userAuthTokenTemp.value = it.userTradeAuthToken.toString()
            }
        } catch (e: Exception) {
            Log.i("Exception", e.toString())
        }
    }

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

            var l: MutableState<List<FloatEntry>> = mutableStateOf(ArrayList(de_que.value))
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


            var l: MutableState<List<FloatEntry>> = mutableStateOf(ArrayList(de_que.value))
            customStepChartEntryModelProducer.setEntries(l.value)

            _messages.value = message
        }
    }

    fun addInitialBuyDetails(message: String) = viewModelScope.launch(Dispatchers.Main) {
        if (_socketStatus.value == true) {
            de_que.value?.clear()
            comparator_queue.value.clear()
            val parser = JsonParser().parse(message).asJsonObject
            var buy_Response = parser.get("buy").asJsonObject
            var buy_echo_req = parser.get("echo_req").asJsonObject

            prevSubscriptionID.value = parser.get("subscription").asJsonObject.get("id").asString

            contractsRepository.insertTransactionDetails(
                transactionDetails = TransactionDetails(
                    loginID = userLoginID.value,
                    contractID = buy_Response.get("contract_id").asString,
                    marketName = listItemsMarkets.entries.find {
                        it.value == buy_echo_req.get("parameters").asJsonObject.get(
                            "symbol"
                        ).asString
                    }?.key,
                    contractType = if (buy_echo_req.get("parameters").asJsonObject.get("contract_type").asString == "MULTUP") {
                        "UP"
                    } else {
                        "DOWN"
                    },
                    buyPrice = buy_echo_req.get("parameters").asJsonObject.get("amount").asString,
                    stopLoss = buy_echo_req.get("parameters").asJsonObject.get("limit_order").asJsonObject.get(
                        "stop_loss"
                    ).asString,
                    takeProfit = buy_echo_req.get("parameters").asJsonObject.get("limit_order").asJsonObject.get(
                        "take_profit"
                    ).asString,
                    indicativeAmt = buy_echo_req.get("parameters").asJsonObject.get("amount").asString,
                    multiplierChosen = buy_echo_req.get("parameters").asJsonObject.get("multiplier").asString,
                    entrySpot = "0",
                    profitOrLoss = "0",
                    tickSpotEntryTime = buy_Response.get("start_time").asString,
                    contractStatusOpenOrClosed = "open",
                    symbolName = buy_echo_req.get("parameters").asJsonObject.get("symbol").asString
                )
            )
            /*


    @ColumnInfo(name = "underlying") var symbolName: String
)
             */

            /*contractsRepository.insertTempToken(temporaryTokens = TemporaryTokens(
                userTradeAccountNo = e.asJsonObject.get("loginid").toString(),
                userTradeAuthToken = "HEEEEY"
            ))*/

            _messages.value = message
        }
    }


    fun addErrorMessage(message: String) = viewModelScope.launch(Dispatchers.Main) {
        if (_socketStatus.value == true) {

            val parser = JsonParser().parse(message).asJsonObject
            var errorMessageRes = parser.get("error").asJsonObject.get("message").asString
            if (errorMessageRes == "You are already subscribed to proposal_open_contract.") {
                openDialogError.value = false
            } else if (errorMessageRes == "Unknown contract sell proposal.") {
                openDialogError.value = false
            }else{
                openDialogError.value = true
            }
            errorMessage.value = errorMessageRes
            dialogTitle.value = "ERROR"

            _messages.value = message
        }
    }

    fun updateContractDetails(message: String) = viewModelScope.launch(Dispatchers.Main) {
        if (_socketStatus.value == true) {

            try {
                val parser = JsonParser().parse(message).asJsonObject
                var buy_Response = parser.get("proposal_open_contract").asJsonObject
                var buy_echo_req = parser.get("echo_req").asJsonObject

                if (buy_Response.get("status").isJsonNull) {
                    Log.i("CLOSED!", "Contract not open")
                }else{
                    try {
                        prevSubscriptionID.value =
                            parser.get("subscription").asJsonObject.get("id").asString
                    }catch(e:Exception){
                        prevSubscriptionID.value =
                            "NNN"
                    }
                }

                contractsRepository.update(

                    id = buy_echo_req.get("contract_id").asString,
                    amount = (buy_Response.get("profit").asFloat + buy_Response.get("buy_price").asFloat).toString(),
                    profit = buy_Response.get("profit").asString,
                    status = if (buy_Response.get("status").isJsonNull) {
                        "Expired"
                    } else {
                        buy_Response.get("status").asString
                    },
                    entry_spot = buy_Response.get("entry_spot").asString
                )

                clickedContractThresholdMarker.value = buy_Response.get("entry_spot").asFloat
                Log.i("TTT", clickedContractThresholdMarker.value.toString())
                Log.i("TTT2", buy_Response.get("entry_spot").toString())

                textIndicativeAmt.value =
                    (buy_Response.get("profit").asFloat + buy_Response.get("buy_price").asFloat).toString()
                textProfitOrLoss.value = buy_Response.get("profit").asString
                textStatus.value = if (buy_Response.get("status").isJsonNull) {
                    "Closed"
                } else {
                    buy_Response.get("status").asString
                }
                contractsRepository.getRecentTenContracts(userLoginID.value).collect {
                    listOpenPositions.value?.clear()

                    for (e in it.withIndex()) {

                        //e.value
                        listOpenPositions.value?.add(
                            mapOf(
                                Pair(
                                    e.value.contractID, listOf(
                                        e.value.contractID,
                                        e.value.marketName,
                                        e.value.contractType,
                                        e.value.buyPrice,
                                        e.value.stopLoss,
                                        e.value.takeProfit,
                                        e.value.indicativeAmt,
                                        e.value.multiplierChosen,
                                        e.value.entrySpot,
                                        e.value.profitOrLoss,
                                        e.value.tickSpotEntryTime,
                                        e.value.contractStatusOpenOrClosed,
                                        e.value.symbolName
                                    ) as List<String>
                                )
                            )
                        )
                    }

                }

                listOpenPositions.value?.forEach {
                    try {

                        it.getValue(clickedContractID.value).let {
                            clickedContractDetails.value = it
                        }
                        //Log.i("TEEEST", it.getValue("220437164568").toString())

                    } catch (e: Exception) {

                    }

                    // it.getValue()

                }
            } catch (e: Exception) {
                Log.e("EXception!!", "${e}")
                errorMessage.value = "An unexpected error occurred. Please try closing the app and then reopening it. Sorry for this"
                dialogTitle.value = "ERROR"
            }
            _messages.value = message
        }
    }

    fun fireUnexpectedError() = viewModelScope.launch(Dispatchers.Main) {
        errorMessage.value = "SOCKET!!"
        dialogTitle.value = "ERROR"
        openDialogError.value = true
    }

    fun reconnectIT(){
        authWSlistener = MainSocket(this)

        //initialize session
        authWebSocket = okHttpClient.newWebSocket(initWebSocketSession(), authWSlistener)
        authWebSocket?.send(
            "{\n" +
                    "  \"authorize\": \"${userAuthTokenTemp.value}\"\n" +
                    "}"
        )
        Thread.sleep(2000)
        authWebSocket?.send(
            "{\n" +
                    "  \"balance\": 1,\n" +
                    "  \"subscribe\": 1\n" +
                    "}"
        )
    }

    fun setStatus(status: Boolean) = viewModelScope.launch(Dispatchers.Main) {
        _socketStatus.value = status
    }

     fun getRecentTenPos(loginID: String) = viewModelScope.launch(Dispatchers.Main) {
         Log.i("getRecentTenPoS", "FIRED ${loginID}")



                 // listOpenPositions.value.
                 try {
                     contractsRepository.getRecentTenContracts(userLoginID.value)
                         .collect {
                             listOpenPositions.value?.clear()

                             for (e in it.withIndex()) {

                                 //e.value
                                 listOpenPositions.value?.add(
                                     mapOf(
                                         Pair(
                                             e.value.contractID, listOf(
                                                 e.value.contractID,
                                                 e.value.marketName,
                                                 e.value.contractType,
                                                 e.value.buyPrice,
                                                 e.value.stopLoss,
                                                 e.value.takeProfit,
                                                 e.value.indicativeAmt,
                                                 e.value.multiplierChosen,
                                                 e.value.entrySpot,
                                                 e.value.profitOrLoss,
                                                 e.value.tickSpotEntryTime,
                                                 e.value.contractStatusOpenOrClosed,
                                                 e.value.symbolName
                                             ) as List<String>
                                         )
                                     )
                                 )
                             }
                             Log.i("getRecentTenPoS", "FIRED ${listOpenPositions.value}")

                         }
                 } catch (e: Exception) {
                     Log.e("oo", e.toString())
                 }


             /*if(streamContract.value == "YES") {
                 contractsRepository.getContractDetails(clickedContractID.value).collect {
                     clickedContractDetails.value?.clear()
                     for (e in it.withIndex()) {
                         clickedContractDetails.value?.add(
                             listOf(
                                 e.value.contractID,
                                 e.value.marketName,
                                 e.value.contractType,
                                 e.value.buyPrice,
                                 e.value.stopLoss,
                                 e.value.takeProfit,
                                 e.value.indicativeAmt,
                                 e.value.multiplierChosen,
                                 e.value.entrySpot,
                                 e.value.profitOrLoss,
                                 e.value.tickSpotEntryTime,
                                 e.value.contractStatusOpenOrClosed,
                                 e.value.symbolName
                             ) as List<String>
                         )
                     }


                 }
             }*/

             //listOpenPositions.values = recentTenPositions

     }

    private companion object {
        const val MULTI_ENTRIES_COMBINED = 3

    }
}
