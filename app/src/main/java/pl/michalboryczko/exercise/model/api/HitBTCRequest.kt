package pl.michalboryczko.exercise.model.api

data class HitBTCRequest(
        val method: String,
        val params: HitBtcParams,
        val id: Int
)

data class HitBtcParams(
        val symbol: String
)