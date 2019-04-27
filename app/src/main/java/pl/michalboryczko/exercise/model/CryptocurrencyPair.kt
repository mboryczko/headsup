package pl.michalboryczko.exercise.model

data class CryptocurrencyPair(
        val pair: String,
        val baseCurrency: String,
        val baseCurrencyUrl: String,
        val quoteCurrency: String,
        val quoteCurrencyUrl: String,
        val volume: Double,
        val lastPrice: Double,
        val id: String
)

fun CryptocurrencyPair.convertToSimple():CryptocurrencyPairSimple
    = CryptocurrencyPairSimple(this.baseCurrency, this.quoteCurrency)

data class CryptocurrencyPairSimple(
        val baseCurrency: String,
        val quoteCurrency: String
)