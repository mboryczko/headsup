package pl.michalboryczko.exercise.ui.register

import androidx.lifecycle.MutableLiveData
import pl.michalboryczko.exercise.R
import pl.michalboryczko.exercise.model.api.call.LoginCall
import pl.michalboryczko.exercise.model.api.call.UserCall
import pl.michalboryczko.exercise.model.base.Resource


class UserValidator(val status: MutableLiveData<Resource<Int>> = MutableLiveData()) {

    var msg: Int? = null

    fun validateUser(user: UserCall): Boolean {
        return (
                isEmailValid(user.email)
                        && isPasswordValid(user.password)
                        && isUsernameValid(user.username)
                )
    }

    fun validateLoginInput(loginInput: LoginCall): Boolean{
        return ( isLoginNotEmpty(loginInput.email)
                && isPasswordNotEmpty(loginInput.password) )
    }

    private fun isLoginNotEmpty(login: String): Boolean{
        if(login.isEmpty()){
            msg = R.string.login_empty
            status.value = Resource.error(R.string.login_empty)
            return false
        }

        return true
    }

    private fun isPasswordNotEmpty(password: String): Boolean{
        if(password.isEmpty()){
            msg = R.string.password_empty
            status.value = Resource.error(R.string.password_empty)
            return false
        }

        return true
    }

    private fun isPasswordValid(password: String): Boolean{
        val charNumbers = listOf('0', '1','2', '3', '4', '5', '6', '7', '8', '9')
        val numbers = password
                .asSequence()
                .filter { it in charNumbers }
                .count()

        if(password.length < 3){
            msg = R.string.invalid_password_too_short
            status.value = Resource.error(R.string.invalid_password_too_short)
            return false
        }

        if(numbers == 0){
            msg = R.string.invalid_password_no_number
            status.value = Resource.error(R.string.invalid_password_no_number)
            return false
        }

        return true
    }

    private fun isUsernameValid(username: String): Boolean{
        if(username.length < 3){
            msg =  R.string.invalid_username_too_short
            status.value = Resource.error(R.string.invalid_username_too_short)
            return false
        }

        return true
    }


    private fun isEmailValid(email: String): Boolean {
        val before = email.substringBefore("@")
        val after = email.substringAfter("@")
        val statement = before.isNotEmpty() && after.isNotEmpty() && email.contains("@")

        if(!statement){
            msg =  R.string.invalid_email
            status.value = Resource.error(R.string.invalid_email)
            return false
        }

        return true
    }
}