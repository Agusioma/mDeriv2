package com.tcreatesllc.mderiv

import android.content.Intent
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import com.google.gson.JsonParser
import com.tcreatesllc.mderiv.storage.AppContainer
import com.tcreatesllc.mderiv.storage.AppDataContainer
import com.tcreatesllc.mderiv.ui.AppViewModelProvider
import com.tcreatesllc.mderiv.ui.screens.TradeScreen
import com.tcreatesllc.mderiv.ui.theme.MDerivTheme
import com.tcreatesllc.mderiv.viewmodels.MainViewModel
import com.tcreatesllc.mderiv.websockets.MainSocket
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import java.lang.Exception

class MainActivity : ComponentActivity() {
    //private lateinit var mainViewModel: MainViewModel
    // lateinit var container: MainApplication
    private val mainViewModel: MainViewModel by viewModels { AppViewModelProvider.Factory }

    private lateinit var authWSlistener: WebSocketListener
    private val okHttpClient = OkHttpClient()
    private var authWebSocket: WebSocket? = null

    //private var balanceStreamWebSocket: WebSocket? = null
    var curTradeSymbol: MutableState<String> = mutableStateOf("1HZ100V")
    lateinit var container: AppContainer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        container = AppDataContainer(this)
        handleIntent(intent)
        //  mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        //  mainViewModel = ViewModelProvider(this).get( )

        mainViewModel.currentTradeSymbol.observe(this) {
            curTradeSymbol.value = it
            Log.i("curTradeSymbol", curTradeSymbol.value)
        }

        mainViewModel.tradeIt.observe(this) {
            if (mainViewModel.tradeIt.value == true) {
                //stopSubscription()
                tradeMultiplier()
            }
        }

        mainViewModel.refreshIt.observe(this) { it ->
            if (it == true) {
                //authWebSocket = okHttpClient.newWebSocket(initWebSocketSession(), authWSlistener)
                mainViewModel.userAuthTokenTemp.observe(this) {
                    Log.i("mainViewModel.userAuthTokenTemp", it)

                    authWebSocket?.send(
                        "{\n" +
                                "  \"authorize\": \"${it}\"\n" +
                                "}"
                    )
                    Log.i("authorize", it)
                }


                //streamBalance()
            }

        }

       /* mainViewModel.cancelIt.observe(this) {
            if (it == true) {
                Log.i("cancelIt","$it")
                mainViewModel.clickedContractID.observe(this) {
                    cancelMultiplier(it)
                    mainViewModel.cancelIt.value == false
                }
            }
        }*/


            mainViewModel.cancelIt.observe(this) {
                if(it == true){
                    //mainViewModel.cancelIt.value = false
                    Log.i("cancelIt2","${mainViewModel.cancelIt.value}")
                    authWebSocket?.send(
                        "{\n" +
                                "  \"sell\": ${mainViewModel.clickedContractID.value},\n" +
                                "  \"price\": 0\n" +
                                "}"
                    )
                    var textYou = "{\n" +
                            "  \"sell\": ${mainViewModel.cancelIt.value},\n" +
                            "  \"price\": 0\n" +
                            "}"

                    Log.d("MUL_MUL", textYou)
                    mainViewModel.cancelIt.value = false
                    Log.i("cancelIt3","${mainViewModel.cancelIt.value}")
                }
            }



        mainViewModel.subcribeIt.observe(this) {
            if (mainViewModel.subcribeIt.value == true) {
                streamContractDetails()
            }

        }

        authWSlistener = MainSocket(mainViewModel)

        //initialize session
        authWebSocket = okHttpClient.newWebSocket(initWebSocketSession(), authWSlistener)

        setContent {
            MDerivTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TradeScreen()
                }
                hideSystemUIAndDisableAutorotation()
            }
        }

        authWebSocket?.send(
            "{\n" +
                    "  \"authorize\": \"${mainViewModel.userAuthTokenTemp.value}\"\n" +
                    "}"
        )

        lifecycleScope.launch {
            while (true) {
                delay(1000L)

                getPrepopulationTicks(curTradeSymbol.value)

            }

        }

        streamBalance()

        //streamTicks("1HZ100V")

        // ATTENTION: This was auto-generated to handle app links.


    }

    fun refresh(token: String) {

        authWSlistener = MainSocket(mainViewModel)
        //initialize session
        authWebSocket = okHttpClient.newWebSocket(initWebSocketSession(), authWSlistener)


        authWebSocket?.send(
            "{\n" +
                    "  \"authorize\": \"${token}\"\n" +
                    "}"
        )

        streamBalance()

        mainViewModel.refreshIt.value = false
    }

    private fun streamBalance() {
        //okHttpClient.
        Thread.sleep(3000)
        authWebSocket?.send(
            "{\n" +
                    "  \"balance\": 1,\n" +
                    "  \"subscribe\": 1\n" +
                    "}"
        )
    }

    private fun stopSubscription() {
        if (mainViewModel.prevSubscriptionID.value !== "NNN") {

            authWebSocket?.send(
                "{\n" +
                        "  \"forget\": \"${mainViewModel.prevSubscriptionID.value}\"\n" +
                        "}"
            )
            var textYou = "{\n" +
                    "  \"forget\": \"${mainViewModel.prevSubscriptionID.value}\"\n" +
                    "}"

            Log.d("MUL_MUL", textYou)
        }

    }

    private fun streamContractDetails() {
        Log.i("mainViewModel.prevSubscriptionID.value", "${mainViewModel.prevSubscriptionID.value}")
        if (mainViewModel.prevSubscriptionID.value !== "NNN") {


            authWebSocket?.send(
                "{\n" +
                        "  \"forget\": \"${mainViewModel.prevSubscriptionID.value}\"\n" +
                        "}"
            )
            var textYou2 = "{\n" +
                    "  \"forget\": \"${mainViewModel.prevSubscriptionID.value}\"\n" +
                    "}"
            Log.d("MUL_MUL", textYou2)

            Thread.sleep(2000L)
            authWebSocket?.send(
                "{\n" +
                        "  \"proposal_open_contract\": 1,\n" +
                        "  \"contract_id\": ${mainViewModel.clickedContractID.value},\n" +
                        "  \"subscribe\": 1\n" +
                        "}"
            )
            var textYou = "{\n" +
                    "  \"proposal_open_contract\": 1,\n" +
                    "  \"contract_id\": ${mainViewModel.clickedContractID.value},\n" +
                    "  \"subscribe\": 1\n" +
                    "}"

            Log.d("MUL_MUL", textYou)

        } else {
            authWebSocket?.send(
                "{\n" +
                        "  \"proposal_open_contract\": 1,\n" +
                        "  \"contract_id\": ${mainViewModel.clickedContractID.value},\n" +
                        "  \"subscribe\": 1\n" +
                        "}"
            )
            var textYou = "{\n" +
                    "  \"proposal_open_contract\": 1,\n" +
                    "  \"contract_id\": ${mainViewModel.clickedContractID.value},\n" +
                    "  \"subscribe\": 1\n" +
                    "}"

            Log.d("MUL_MUL:ALONE", textYou)
        }

    }

    private fun cancelMultiplier(contractID: String) {
        mainViewModel.cancelIt.value = false
        Log.i("cancelIt2","${mainViewModel.cancelIt.value}")
        authWebSocket?.send(
            "{\n" +
                    "  \"sell\": $contractID,\n" +
                    "  \"price\": 0\n" +
                    "}"
        )


        var textYou = "{\n" +
                "  \"sell\": $contractID,\n" +
                "  \"price\": 0\n" +
                "}"

        Log.d("MUL_MUL", textYou)



    }

    private fun tradeMultiplier() {
        if (mainViewModel.prevSubscriptionID.value !== "NNN") {


            authWebSocket?.send(
                "{\n" +
                        "  \"forget\": \"${mainViewModel.prevSubscriptionID.value}\"\n" +
                        "}"
            )
            var textYou2 = "{\n" +
                    "  \"forget\": \"${mainViewModel.prevSubscriptionID.value}\"\n" +
                    "}"
            Log.d("MUL_MUL", textYou2)

            Thread.sleep(2000L)
            authWebSocket?.send(
                "{\n" +
                        "  \"buy\": 1,\n" +
                        "  \"price\": ${mainViewModel.textStake.value?.toDouble()},\n" +
                        "  \"subscribe\": 1,\n" +
                        "  \"parameters\":{\n" +
                        "        \"limit_order\":{\n" +
                        "            \"take_profit\":${mainViewModel.textSP.value}, \n" +
                        "            \"stop_loss\":${mainViewModel.textSL.value}\n" +
                        "        },\n" +
                        "        \"amount\": ${mainViewModel.textStake.value?.toDouble()},\n" +
                        "        \"basis\": \"stake\",\n" +
                        "        \"contract_type\": \"${mainViewModel.textOption.value}\",\n" +
                        "        \"currency\": \"${mainViewModel.accountCurr.value}\",\n" +
                        "        \"multiplier\": ${mainViewModel.textMul.value?.toInt()},\n" +
                        "        \"symbol\": \"${mainViewModel.currentTradeSymbol.value}\"\n" +
                        "}\n" +
                        "}"
            )

        } else {
            authWebSocket?.send(
                "{\n" +
                        "  \"buy\": 1,\n" +
                        "  \"price\": ${mainViewModel.textStake.value?.toDouble()},\n" +
                        "  \"subscribe\": 1,\n" +
                        "  \"parameters\":{\n" +
                        "        \"limit_order\":{\n" +
                        "            \"take_profit\":${mainViewModel.textSP.value}, \n" +
                        "            \"stop_loss\":${mainViewModel.textSL.value}\n" +
                        "        },\n" +
                        "        \"amount\": ${mainViewModel.textStake.value?.toDouble()},\n" +
                        "        \"basis\": \"stake\",\n" +
                        "        \"contract_type\": \"${mainViewModel.textOption.value}\",\n" +
                        "        \"currency\": \"${mainViewModel.accountCurr.value}\",\n" +
                        "        \"multiplier\": ${mainViewModel.textMul.value?.toInt()},\n" +
                        "        \"symbol\": \"${mainViewModel.currentTradeSymbol.value}\"\n" +
                        "}\n" +
                        "}"
            )
        }


        var textYou = "{\n" +
                "  \"buy\": 1,\n" +
                "  \"price\": ${mainViewModel.textStake.value?.toDouble()},\n" +
                "  \"subscribe\": 1,\n" +
                "  \"parameters\":{\n" +
                "        \"limit_order\":{\n" +
                "            \"take_profit\":${mainViewModel.textSP.value}, \n" +
                "            \"stop_loss\":${mainViewModel.textSL.value}\n" +
                "        },\n" +
                "        \"amount\": ${mainViewModel.textStake.value?.toDouble()},\n" +
                "        \"basis\": \"stake\",\n" +
                "        \"contract_type\": \"${mainViewModel.textOption.value}\",\n" +
                "        \"currency\": \"${mainViewModel.accountCurr.value}\",\n" +
                "        \"multiplier\": ${mainViewModel.textMul.value},\n" +
                "        \"symbol\": \"${mainViewModel.currentTradeSymbol.value}\"\n" +
                "}\n" +
                "}"

        Log.d("MUL_MUL", textYou)

        mainViewModel.tradeIt.value = false

    }

    private fun getPrepopulationTicks(marketIndex: String) {
        //Thread.sleep(1000)
        authWebSocket?.send(
            "{\n" +
                    "  \"ticks_history\": \"${marketIndex}\",\n" +
                    "  \"adjust_start_time\": 1,\n" +
                    "  \"count\": 600,\n" +
                    "  \"end\": \"latest\",\n" +
                    "  \"start\": 1,\n" +
                    "  \"style\": \"ticks\"\n" +
                    "}"
        )
    }

    private fun streamTicks(marketIndex: String) {
        Thread.sleep(5000)
        authWebSocket?.send(
            "{\n" +
                    "  \"ticks\": \"${marketIndex}\",\n" +
                    "  \"subscribe\": 1\n" +
                    "}"
        )
    }

    private suspend fun pingDeriv() {

        //delay(1000)
        authWebSocket?.send(
            "{\n" +
                    "  \"ping\": 1\n" +
                    "}"
        )
    }

    private fun initWebSocketSession(): Request {
        val websocketURL = "wss://ws.derivws.com/websockets/v3?app_id=38697"

        return Request.Builder()
            .url(websocketURL)
            .build()
    }

    private fun initTickWebSocketSession(): Request {
        val websocketURL = "wss://ws.derivws.com/websockets/v3?app_id=39735"

        return Request.Builder()
            .url(websocketURL)
            .build()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }


    private fun handleIntent(intent: Intent) {
       //. mainViewModel
        try {
            mainViewModel.clearAuthDB()
            var accTokenMapping: MutableMap<String, String> = mutableMapOf()
            val appLinkData: Uri? = intent.data
            mainViewModel.userLoginID.value = "\"${appLinkData?.getQueryParameter("acct1")}\""
            mainViewModel.userAuthTokenTemp.value = appLinkData?.getQueryParameter("token1")
            // val v = uri.getQueryParameter("v")
            for (i in 1..5) {
                if (appLinkData?.getQueryParameter("acct$i") != null) {
                    Log.i("key-ac", appLinkData?.getQueryParameter("acct$i").toString())
                    appLinkData?.getQueryParameter("acct$i")?.let {
                        accTokenMapping.put(
                            it,
                            appLinkData?.getQueryParameter("token$i")!!
                        )
                    }
                    Log.i("key-ac", appLinkData?.getQueryParameter("token$i").toString())
                }
                //appLinkData?.getQueryParameter("acct$i")?.let { Log.i("pp", it) }
            }
            if (accTokenMapping.isNotEmpty()) {
                //mainViewModel.accTokenMapping.value = accTokenMapping2
                mainViewModel.storeAuthToDB(accTokenMapping)

                mainViewModel.getAuthTokenFromDB(JsonParser().parse(mainViewModel.userLoginID.value).asString)
                mainViewModel.getRecentTenPos(mainViewModel.userLoginID.value.toString())
            }

            //Log.i( "accTokenMapping.toString()",accTokenMapping.toString())
        }catch(e: Exception){
            Log.i("CLEARException", "$e")
        }
    }

    fun hideSystemUIAndDisableAutorotation() {

        //Hides the ugly action bar at the top
        actionBar?.hide()

        //Hide the status bars

        WindowCompat.setDecorFitsSystemWindows(window, true)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        } else {
            window.insetsController?.apply {
                hide(WindowInsets.Type.systemBars())
                systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
    }

}