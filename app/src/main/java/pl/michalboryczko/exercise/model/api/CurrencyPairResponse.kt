package pl.michalboryczko.exercise.model.api

data class CurrencyPairResponse(
        val id: String,
        val baseCurrency: String,
        val quoteCurrency: String,
        val quantityIncrement: String,
        val tickSize: String,
        val takeLiquidityRate: String,
        val provideLiquidityRate: String,
        val feeCurrency: String
)