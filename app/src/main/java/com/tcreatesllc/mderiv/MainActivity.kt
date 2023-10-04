package com.tcreatesllc.mderiv

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
import androidx.lifecycle.ViewModelProvider
import com.tcreatesllc.mderiv.viewmodels.MainViewModel
import com.tcreatesllc.mderiv.websockets.AuthorizeUser
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: MainViewModel

    private lateinit var webSocketListener: WebSocketListener
    private val okHttpClient = OkHttpClient()
    private var webSocket: WebSocket? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        webSocketListener = AuthorizeUser(viewModel)

        //initialize session
        webSocket = okHttpClient.newWebSocket(initWebSocketSession(), webSocketListener)

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

        webSocket?.send("{\n" +
                "  \"authorize\": \"a1-UJeCdXTnSVpX7D0kP6EEC9kFYUADN\"\n" +
                "}")
        // ATTENTION: This was auto-generated to handle app links.
        val appLinkIntent: Intent = intent
        val appLinkAction: String? = appLinkIntent.action
        val appLinkData: Uri? = appLinkIntent.data
        handleIntent(intent)
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