package pl.michalboryczko.exercise.source

import pl.michalboryczko.exercise.model.PriceStatus

class PriceStatusChecker {

    fun getPriceStatus(previousPrice: Double?, currentPrice: Double): PriceStatus {
        if(previousPrice != null){
            return when{
                currentPrice > previousPrice -> PriceStatus.HIGHER
                currentPrice < previousPrice -> PriceStatus.LOWER
                else -> PriceStatus.SAME
            }
        }

        return PriceStatus.SAME
    }
}