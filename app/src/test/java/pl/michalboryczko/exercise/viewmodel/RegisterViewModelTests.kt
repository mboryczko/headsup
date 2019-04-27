package pl.michalboryczko.quickmaths

import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import junit.framework.Assert
import org.junit.Test


import org.mockito.Mockito.*
import pl.michalboryczko.exercise.BaseTest
import pl.michalboryczko.exercise.R
import pl.michalboryczko.exercise.model.api.call.UserCall
import pl.michalboryczko.exercise.model.base.Resource
import pl.michalboryczko.exercise.model.exceptions.NoInternetException
import pl.michalboryczko.exercise.source.repository.UserRepository
import pl.michalboryczko.exercise.ui.register.RegisterViewModel


class RegisterViewModelTests: BaseTest() {

    private val repo = mock(UserRepository::class.java)
    private val viewmodel by lazy { RegisterViewModel(repo, checker, Schedulers.trampoline(), Schedulers.trampoline()) }


    @Test
    fun registerUserPositiveTest(){
        whenever(repo.createUser(anyObject())).thenReturn(Single.just(true))
        viewmodel.registerClicked(generateValidUser())
        Assert.assertEquals(Resource.success(R.string.account_created), viewmodel.status.value)
    }

    @Test
    fun registerWrongUserTest(){
        whenever(repo.createUser(anyObject())).thenReturn(Single.just(true))
        val invalidUser = UserCall("", random.generateStrongPassword(), random.generateRandomString(8))
        viewmodel.registerClicked(invalidUser)
        Assert.assertEquals(Resource.error<Int>(R.string.invalid_email), viewmodel.status.value)
    }

    @Test
    fun registerNoInternetTest(){
        whenever(repo.createUser(anyObject())).thenReturn(Single.error(NoInternetException()))
        viewmodel.registerClicked(generateValidUser())
        Assert.assertEquals(Resource.error<NoInternetException>(R.string.no_internet), viewmodel.status.value)
    }



}
