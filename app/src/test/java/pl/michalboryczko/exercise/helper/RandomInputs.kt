package pl.michalboryczko.exercise.helper

import java.util.*

fun ClosedRange<Char>.randomString(lenght: Int) =
        (1..lenght)
                .map { (Random().nextInt(endInclusive.toInt() - start.toInt()) + start.toInt()).toChar() }
                .joinToString("")

fun ClosedRange<Int>.randomStringFromNumbers(lenght: Int) =
        (1..lenght)
                .map { (Random().nextInt(endInclusive - start) + start) }

class RandomInputs {

    val r = Random()
    val lettersCapitals = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    val letters = "abcdefghijklmnopqrstuvwxyz".toCharArray()

    fun generateRandomEmail(): String{
        val before = generateRandomString()
        val after = generateRandomString()
        return "${before}@${after}.com"
    }

    fun generateStrongPassword(): String = "5" + ('a' .. 'z').randomString(5) + (0 .. 9).randomStringFromNumbers(2) + "6"

    fun generateRandomString(length: Int): String = ('a' .. 'z').randomString(length)

    fun generateRandomString(min: Int = 3, max: Int = 10): String{
        val length = r.nextInt(max + min) + min
        return ('a' .. 'z').randomString(length)
    }

    fun generateRandomLong(): Long{
        val value = r.nextLong() + r.nextLong() + r.nextInt(99999999)
        return if(value < 0) return -value else value
    }


}