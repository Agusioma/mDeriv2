

import android.util.Log
import com.google.gson.JsonParser
import com.tcreatesllc.mderiv.viewmodels.MainViewModel
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener


class BalanceStreamer(
    private val mainViewModel: MainViewModel
): WebSocketListener() {

    private val TAG = "Test"

    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)
        mainViewModel.setStatus(true)
        //webSocket.send("Android Device Connected")
        Log.d(TAG, "onOpen: $response")
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
        //if

        val parser = JsonParser().parse(text).asJsonObject
        if (JsonParser().parse(text).asJsonObject.get("error") == null) {

            if (JsonParser().parse(text).asJsonObject.get("history") !== null) {
                mainViewModel.addPrepopulationTicks(text)
            }
        } else {
            mainViewModel.addErrorMessage(text)
        }
        //Creating JSONObject from String using parser
        //Creating JSONObject from String using parser

        Log.d(TAG, "onMessage3: $parser")
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosing(webSocket, code, reason)
        Log.d(TAG, "onClosing: $code $reason")
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosed(webSocket, code, reason)
        mainViewModel.setStatus(false)
        Log.d(TAG, "onClosed: $code $reason")
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        Log.d(TAG, "onFailure: ${t.message} $response")
        super.onFailure(webSocket, t, response)
    }
}