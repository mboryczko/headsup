package pl.michalboryczko.exercise.model.source.local.room.db

import androidx.room.Database
import androidx.room.RoomDatabase
import pl.michalboryczko.exercise.model.source.local.room.QuestionDao
import pl.michalboryczko.exercise.model.source.local.room.QuestionRoom

@Database(entities = arrayOf(QuestionRoom::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun questionDao(): QuestionDao
}