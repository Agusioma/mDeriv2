package com.tcreatesllc.mderiv

import BalanceStreamer
import android.content.pm.ActivityInfo
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import com.tcreatesllc.mderiv.ui.screens.TradeScreen
import com.tcreatesllc.mderiv.ui.theme.MDerivTheme
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.tcreatesllc.mderiv.viewmodels.MainViewModel
import com.tcreatesllc.mderiv.websockets.MainSocket
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: MainViewModel

    private lateinit var balanceStreamWSlistener: WebSocketListener
    private lateinit var authWSlistener: WebSocketListener
    private val okHttpClient = OkHttpClient()
    private var authWebSocket: WebSocket? = null
    private var balanceStreamWebSocket: WebSocket? = null
    var curTradeSymbol: MutableState<String> = mutableStateOf("1HZ100V")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        viewModel.currentTradeSymbol.observe(this, Observer {
            curTradeSymbol.value = it
        })

        authWSlistener = MainSocket(viewModel)
        balanceStreamWSlistener = BalanceStreamer(viewModel)

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
                    "  \"authorize\": \"a1-UJeCdXTnSVpX7D0kP6EEC9kFYUADN\"\n" +
                    "}"
        )
        lifecycleScope.launch {
            while (true) {
                delay(1000)

                getPrepopulationTicks(curTradeSymbol.value)
            }
        }
        streamBalance()



        //streamTicks("1HZ100V")

        // ATTENTION: This was auto-generated to handle app links.
        val appLinkIntent: Intent = intent
        val appLinkAction: String? = appLinkIntent.action
        val appLinkData: Uri? = appLinkIntent.data
        handleIntent(intent)
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

    private fun getPrepopulationTicks(marketIndex: String){
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

    private fun streamTicks(marketIndex: String){
        Thread.sleep(5000)
        authWebSocket?.send(
            "{\n" +
                    "  \"ticks\": \"${marketIndex}\",\n" +
                    "  \"subscribe\": 1\n" +
                    "}"
        )
    }

    private suspend fun pingDeriv(){

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

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    private fun handleIntent(intent: Intent) {
        val appLinkAction = intent.action
        val appLinkData: Uri? = intent.data
        Log.i("LINK!", appLinkData.toString())
        /*if (Intent.ACTION_VIEW == appLinkAction) {
            appLinkData?.lastPathSegment?.also { recipeId ->
                Uri.parse("/")
                    .buildUpon()
                    .appendPath(recipeId)
                    .build().also { appData ->
                        //showRecipe(appData)
                    }
            }
        }*/
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