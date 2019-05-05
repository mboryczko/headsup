package pl.michalboryczko.exercise.model

import pl.michalboryczko.exercise.model.api.Session


data class ActiveSession(
        val isMaster: Boolean,
        val session: Session
)