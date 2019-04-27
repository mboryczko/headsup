package pl.michalboryczko.exercise

import junit.framework.Assert.assertEquals
import org.junit.Test
import pl.michalboryczko.exercise.extensions.convertTimestampToPrintableDate

class DateTests {

    @Test
    fun timestampToPrintableDateTest(){
        val timestamp = "2019-02-23T23:26:25.664Z"
        assertEquals("23:26:25", timestamp.convertTimestampToPrintableDate())
    }

    @Test
    fun invalidTimestampToPrintableDateTest(){
        val timestamp = "2019-02-2w3T23:26:25.664Zggw"
        assertEquals(timestamp, timestamp.convertTimestampToPrintableDate())
    }

}