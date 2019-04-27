package pl.michalboryczko.exercise.model.exceptions

import java.lang.Exception

data class UnknownException(val msg: String): Exception()