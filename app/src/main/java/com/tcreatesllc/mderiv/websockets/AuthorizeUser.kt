package com.tcreatesllc.mderiv.websockets

import android.util.Log
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.tcreatesllc.mderiv.viewmodels.MainViewModel
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener


class AuthorizeUser(
    private val viewModel: MainViewModel
): WebSocketListener() {

    private val TAG = "Test"

    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)
        viewModel.setStatus(true)
        //webSocket.send("Android Device Connected")
        Log.d(TAG, "onOpen: $response")
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
        viewModel.addMessage(text)
        val parser = JsonParser().parse(text).asJsonObject
        //Creating JSONObject from String using parser
        //Creating JSONObject from String using parser

        Log.d(TAG, "onMessage: ${parser.get("authorize").asJsonObject.get("account_list").asJsonArray[0]}")
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosing(webSocket, code, reason)
        Log.d(TAG, "onClosing: $code $reason")
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosed(webSocket, code, reason)
        viewModel.setStatus(false)
        Log.d(TAG, "onClosed: $code $reason")
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        Log.d(TAG, "onFailure: ${t.message} $response")
        super.onFailure(webSocket, t, response)
    }
}