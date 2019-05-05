package pl.michalboryczko.exercise.source.api.firebase

import android.util.Log
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.*
import javax.inject.Singleton
import java.lang.Exception
import pl.michalboryczko.exercise.model.api.Estimation
import pl.michalboryczko.exercise.model.api.Session
import pl.michalboryczko.exercise.model.api.Story
import pl.michalboryczko.exercise.model.api.call.LoginCall
import pl.michalboryczko.exercise.model.api.call.UserCall
import pl.michalboryczko.exercise.model.exceptions.NotFoundException
import pl.michalboryczko.exercise.model.exceptions.UnathorizedException
import pl.michalboryczko.exercise.model.exceptions.WrongPasswordException
import pl.michalboryczko.exercise.model.presentation.User
import timber.log.Timber
import java.util.*





@Singleton
class FirebaseApiService {

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val mDatabase: DatabaseReference by lazy { FirebaseDatabase.getInstance().reference }

    private val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    fun logout(): Single<Boolean>{
        return Single.create{emitter ->
            auth.signOut()
            emitter.onSuccess(true)
            Timber.d("logged out")
        }
    }

    fun logIn(input: LoginCall): Single<Boolean> {
        return Single
                .create { emitter ->
                    auth.signInWithEmailAndPassword(input.email, input.password)
                            .addOnSuccessListener{
                                emitter.onSuccess(true)

                            }
                            .addOnFailureListener{
                                emitter.onError(Exception(it.message))
                            }
                }
    }

    fun isUserLoggedIn(): Flowable<Boolean> {
        return Flowable.create(
                { emitter ->
                    emitter.onNext(auth.currentUser != null)
                }, BackpressureStrategy.BUFFER
        )
    }

    fun getCurrentUser(): Single<User>{
        val currentUser = auth.currentUser
        if(currentUser != null){
            val uid = currentUser.uid

            return Single.create {emitter ->
                db.collection("users")
                        .whereEqualTo("id", uid)
                        .limit(1)
                        .addSnapshotListener{ value, e ->
                            if (e != null) {
//                            emitter.onError(e)
//                            return@addSnapshotListener
                            }else{
                                val users = value!!.toObjects(User::class.java)
                                if(users.size > 0){
                                    val user = users.first()
                                    emitter.onSuccess(user)
                                }else{
                                    emitter.onError(NotFoundException())
                                }
                            }

                        }
            }
        }



        return Single.error(UnathorizedException())
    }


    fun addUser(user: UserCall, uid: String): Single<User>{
        val user = User(uid, user.email, user.username)
        return Single
                .create { emitter ->
                    db.collection("users")
                            .document(uid)
                            .set(user)
                            .addOnSuccessListener { emitter.onSuccess(user) }
                            .addOnFailureListener{ emitter.onError(it)}
                }
    }


    fun createUser(user: UserCall): Single<String> {
        return Single
                .create { emitter ->
                    auth.createUserWithEmailAndPassword(user.email, user.password)
                            .addOnSuccessListener{
                                Log.d("apiLog", "onsuccess ")
                                emitter.onSuccess(it.user.uid)

                            }
                            .addOnFailureListener{
                                Log.d("apiLog", "onfailure listener message: ${it.localizedMessage}")
                                emitter.onError(it)
                            }
                }
    }

    fun saveSession(session: Session, sessionId: String): Single<Boolean> {
        return Single
                .create { emitter ->
                    mDatabase
                            .child("sessions/$sessionId")
                            .setValue(session)
                            .addOnSuccessListener { emitter.onSuccess(true) }
                            .addOnFailureListener{ emitter.onError(it)}
                }

    }

    fun saveStory(story: Story, sessionId: String, storyId: String): Single<Boolean> {

        val initialPath = "sessions/$sessionId/stories/$storyId"
        val key = mDatabase.child(initialPath).push().key

        val storyMap = HashMap<String, Any>()
        storyMap.put("story", story.story)
        storyMap.put("description", story.description)

        val childUpdates = HashMap<String, Any>()
        childUpdates[initialPath] = storyMap

        return Single
                .create { emitter ->
                    mDatabase
                            .updateChildren(childUpdates)
                            .addOnSuccessListener { emitter.onSuccess(true) }
                            .addOnFailureListener{ emitter.onError(it)}
                }

    }

    fun saveEstimation(estimation: Estimation, sessionId: String, storyId: String): Single<Boolean> {

        val initialPath = "sessions/$sessionId/stories/$storyId/estimations"
        val key = mDatabase.child(initialPath).push().key

        val estimationMap = HashMap<String, Any>()
        estimationMap.put("points", estimation.points)
        estimationMap.put("description", estimation.userId)

        val childUpdates = HashMap<String, Any>()
        childUpdates["$initialPath/$key"] = estimationMap

        return Single
                .create { emitter ->
                    mDatabase
                            .updateChildren(childUpdates)
                            .addOnSuccessListener { emitter.onSuccess(true) }
                            .addOnFailureListener{ emitter.onError(it)}
                }
    }

}