package pl.michalboryczko.exercise.model


class CryptocurrencyPairDetails(
        val symbol: String,
        val timeUpdated: String,
        val baseCurrencyUrl: String,
        val quoteCurrencyUrl: String,
        val volume: Double,
        val low: Double,
        val high: Double,
        val priceFormatted: String,
        val price: Double
)