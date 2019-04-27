package pl.michalboryczko.exercise.model.api

data class CryptocurrencyResponse(
        val jsonrpc: String,
        val method: String,
        val params: Params
)