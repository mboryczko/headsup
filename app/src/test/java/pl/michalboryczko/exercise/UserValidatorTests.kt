package pl.michalboryczko.exercise

import androidx.lifecycle.MutableLiveData
import org.junit.Test
import org.junit.Assert
import pl.michalboryczko.exercise.model.api.call.UserCall
import pl.michalboryczko.exercise.model.base.Resource
import pl.michalboryczko.exercise.ui.register.UserValidator

class UserValidatorTests: BaseTest() {

    private val userValidator = UserValidator(MutableLiveData<Resource<Int>>())



    @Test
    fun registerValidationsTest(){
        Assert.assertTrue(userValidator.validateUser(generateValidUser()))
        Assert.assertNull(userValidator.msg)
    }

    @Test
    fun wrongEmailTest(){
        val result = userValidator.validateUser(UserCall("", random.generateStrongPassword(), random.generateRandomString(8)))
        Assert.assertFalse(result)
        Assert.assertEquals(userValidator.msg,  R.string.invalid_email)
    }

    @Test
    fun wrongEmailTest2(){
        val result = userValidator.validateUser(UserCall(random.generateRandomString(), random.generateStrongPassword(), random.generateRandomString(8)))
        Assert.assertFalse(result)
        Assert.assertEquals(userValidator.msg,  R.string.invalid_email)
    }

    @Test
    fun wrongUsernameTest(){
        val result = userValidator.validateUser(UserCall(random.generateRandomEmail(), random.generateStrongPassword(), ""))
        Assert.assertFalse(result)
        Assert.assertEquals(userValidator.msg,  R.string.invalid_username_too_short)
    }

    @Test
    fun tooShortPasswordTest(){
        val result = userValidator.validateUser(UserCall(random.generateRandomEmail(), random.generateRandomString(2), random.generateRandomString(8)))
        Assert.assertFalse(result)
        Assert.assertEquals(userValidator.msg,  R.string.invalid_password_too_short)
    }

    @Test
    fun noNumberPasswordTest(){
        val result = userValidator.validateUser(UserCall(random.generateRandomEmail(), random.generateRandomString(8), random.generateRandomString(8)))
        Assert.assertFalse(result)
        Assert.assertEquals(userValidator.msg,  R.string.invalid_password_no_number)
    }


}