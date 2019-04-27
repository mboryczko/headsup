package pl.michalboryczko.quickmaths

import org.junit.Test

import org.junit.Assert.*
import pl.michalboryczko.exercise.BaseTest


class RandomsTests: BaseTest() {


    @Test
    fun testRandomEmail(){
        val email = random.generateRandomEmail()
        assertNotEquals("", email)
    }

    @Test
    fun testRandomPassword(){
        val username = random.generateStrongPassword()
        assertNotEquals("", username)
    }

    @Test
    fun testRandomUsername(){
        val username = random.generateRandomString(7)
        assertNotEquals("", username)
    }


}
