package pl.michalboryczko.exercise.model.exceptions

import java.lang.Exception

data class ApiException(
        val errorMsg: String
): Exception()