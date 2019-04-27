package pl.michalboryczko.exercise.source.api.websocket

import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.FlowableEmitter
import okhttp3.*
import pl.michalboryczko.exercise.model.api.CryptocurrencyResponse

class WebSocketApiService(
        val endpoint: String,
        val clientCoinPrice: OkHttpClient
) {
    private var webSocket: WebSocket? = null

    fun observePrice(product: String): Flowable<CryptocurrencyResponse> {
        return Flowable
                .create( { emitter ->
                    getCoinPrice(product, emitter)
                }, BackpressureStrategy.BUFFER)
    }

    fun cancelWebSocket(){
        webSocket?.cancel()
        webSocket?.close(1000, "")
    }

    private fun getCoinPrice(product: String, emitter: FlowableEmitter<CryptocurrencyResponse>) {
        val requestCoinPrice = Request.Builder().url(endpoint).build()

        webSocket = clientCoinPrice.newWebSocket(requestCoinPrice, CryptocurrencyTickerListener(product, emitter))
    }


}