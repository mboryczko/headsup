package pl.michalboryczko.exercise.extensions

import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

fun String.convertTimestampToPrintableDate(): String{
    return try {
        val clearedTime = this.replace("T", " ")
                .replace("Z", "")

        val ts = Timestamp.valueOf(clearedTime)
        val startDate: Date = ts
        val df = SimpleDateFormat("HH:mm:ss")
        df.format(startDate)
    } catch (e: Exception) {
        return this
    }
}