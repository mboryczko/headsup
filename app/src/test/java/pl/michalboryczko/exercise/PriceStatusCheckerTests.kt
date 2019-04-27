package pl.michalboryczko.exercise

import org.junit.Test
import pl.michalboryczko.exercise.source.PriceStatusChecker
import junit.framework.Assert.assertEquals
import pl.michalboryczko.exercise.model.PriceStatus

class PriceStatusCheckerTests {

    private val priceStatusChecker = PriceStatusChecker()

    @Test
    fun shouldBeHigherTest(){
        val status = priceStatusChecker.getPriceStatus(12.4, 18.3)
        assertEquals(PriceStatus.HIGHER, status)
    }

    @Test
    fun shouldBeLowerTest(){
        val status = priceStatusChecker.getPriceStatus(11.0, 9.9)
        assertEquals(PriceStatus.LOWER, status)
    }

    @Test
    fun shouldBeSameTest(){
        val status = priceStatusChecker.getPriceStatus(8.8, 8.8)
        assertEquals(PriceStatus.SAME, status)
    }


}