package pl.michalboryczko.exercise.source

import pl.michalboryczko.exercise.model.CryptocurrencyPair
import pl.michalboryczko.exercise.model.CryptocurrencyPairSimple
import pl.michalboryczko.exercise.model.api.CurrencyTickerResponse

class CryptocurrencyMapper{


   /* private val baseCryptoUrl = "https://raw.githubusercontent.com/atomiclabs/cryptocurrency-icons/master/128/color/"

    fun mergeCryptocurrencyPairAndTicker(
            pair: CurrencyPairResponse,
            ticker: CurrencyTickerResponse): CryptocurrencyPair
            = CryptocurrencyPair("${pair.baseCurrency}/${pair.quoteCurrency}", pair.baseCurrency, createCryptocurrencyUrl(pair.baseCurrency),
            pair.quoteCurrency, createCryptocurrencyUrl(pair.quoteCurrency), ticker.volume, ticker.last, pair.id)


    fun createCryptocurrencyUrl(currency: String)
            = "$baseCryptoUrl${currency.toLowerCase()}.png"

    fun createBaseCryptocurrencyUrlFromPair(pair: String): String{
        val base = pair.substringBefore("/")
        return "$baseCryptoUrl${base.toLowerCase()}.png"
    }

    fun createQuoteCryptocurrencyUrlFromPair(pair: String): String{
        val quote = pair.substringAfter("/")
        return "$baseCryptoUrl${quote.toLowerCase()}.png"
    }

    fun createPairString(pair: CryptocurrencyPairSimple)
        = "${pair.baseCurrency.toLowerCase()}${pair.quoteCurrency.toLowerCase()}"


    fun createPairStringSlashSeparated(pair: CryptocurrencyPairSimple)
        = "${pair.baseCurrency}/${pair.quoteCurrency}"*/
}