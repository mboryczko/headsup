package pl.michalboryczko.exercise.source.api.websocket

import com.google.gson.Gson
import io.reactivex.FlowableEmitter
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import pl.michalboryczko.exercise.model.api.HitBTCRequest
import pl.michalboryczko.exercise.model.api.HitBtcParams
import pl.michalboryczko.exercise.model.api.CryptocurrencyResponse
import pl.michalboryczko.exercise.model.exceptions.NoInternetException
import pl.michalboryczko.exercise.model.exceptions.UnknownException
import timber.log.Timber
import java.lang.Exception
import java.net.UnknownHostException
import javax.inject.Singleton
import kotlin.random.Random


@Singleton
class CryptocurrencyTickerListener(
    val product: String,
    val emitter: FlowableEmitter<CryptocurrencyResponse>
): WebSocketListener() {

    private val gson = Gson()


    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)
        val request = HitBTCRequest("subscribeTicker", HitBtcParams(product), Random.nextInt())
        webSocket.send(gson.toJson(request))
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
        try{
            val crypto = gson.fromJson(text, CryptocurrencyResponse::class.java)
            if(crypto.params != null)
                emitter.onNext(crypto)
        }catch (e: Exception){
            Timber.d("error parsing json object")
        }
    }

    override fun onFailure(webSocket: WebSocket?, t: Throwable?, response: Response?) {
        Timber.d("on failure websocket")
        if(t is UnknownHostException)
            emitter.onError(NoInternetException())

    }

}