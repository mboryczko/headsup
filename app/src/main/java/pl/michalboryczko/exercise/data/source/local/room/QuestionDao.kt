package pl.michalboryczko.exercise.model.source.local.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import io.reactivex.Single
import pl.michalboryczko.exercise.model.models.api.response.Question

@Dao
interface QuestionDao{

    @Query("SELECT * FROM question")
    fun getAll(): Single<List<Question>>

    @Query("SELECT * FROM question WHERE id IN (:ids)")
    fun loadAllByIds(ids: IntArray): Single<List<Question>>

    @Insert
    fun insertAll(vararg users: Question)

    @Delete
    fun delete(user: Question)
}