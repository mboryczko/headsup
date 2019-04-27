package pl.michalboryczko.exercise.model.api

data class CurrencyTickerResponse(
        val ask: Double,
        val bid: Double,
        val last: Double,
        val open: Double,
        val low: Double,
        val high: Double,
        val volume: Double,
        val volumeQuote: Double,
        val timestamp: String,
        val symbol: String
)