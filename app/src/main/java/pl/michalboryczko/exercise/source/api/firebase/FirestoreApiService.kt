package pl.michalboryczko.exercise.source.api.firebase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import io.reactivex.*
import pl.michalboryczko.exercise.model.api.Estimation
import javax.inject.Singleton
import java.lang.Exception
import pl.michalboryczko.exercise.model.api.Session
import pl.michalboryczko.exercise.model.api.Story
import pl.michalboryczko.exercise.model.api.call.LoginCall
import pl.michalboryczko.exercise.model.api.call.UserCall
import pl.michalboryczko.exercise.model.exceptions.NotFoundException
import pl.michalboryczko.exercise.model.exceptions.WrongPasswordException
import timber.log.Timber


@Singleton
class FirestoreApiService {

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

    private val SESSIONS = "sessions"
    private val CURRENT_STORY = "currentStory"
    private val STORIES = "stories"
    private val SESSION_ID = "sessionId"
    private val STORY_ID = "storyId"

    fun logIn(input: LoginCall): Single<Boolean> {
        return Single
                .create { emitter ->
                    auth.signInWithEmailAndPassword(input.email, input.password)
                            .addOnSuccessListener{
                                emitter.onSuccess(true)

                            }
                            .addOnFailureListener{
                                emitter.onError(Exception("no internet"))
                            }
                }
    }



    fun isUserLoggedIn(): Single<Boolean> {
        return Single.just(auth.currentUser)
                .map { it != null }
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

    fun updateSession(sessionId: String, currentStoryId: String): Single<Boolean>{
        return Single
                .create { emitter ->
                    db.collection(SESSIONS)
                            .document(sessionId)
                            .update(CURRENT_STORY, currentStoryId)
                            .addOnSuccessListener { emitter.onSuccess(true) }
                            .addOnFailureListener{ emitter.onError(it)}
                }
    }

    //save Session object AT sessionId node
    fun createSession(managerId:String, name: String, password: String): Single<Session> {
        //val sessionId = db.collection(SESSIONS).document().id
        val sessionId = name
        val session = Session(sessionId, managerId, name, password, null)
        return Single
                .create { emitter ->
                    db.collection(SESSIONS)
                            .document(sessionId)
                            .set(session)
                            .addOnSuccessListener { emitter.onSuccess(session) }
                            .addOnFailureListener{ emitter.onError(it)}
                }
    }

    fun observeSessionById(sessionId: String): Flowable<Session>{
        return Flowable.create(
                { emitter ->
                    db.collection(SESSIONS)
                            .whereEqualTo(SESSION_ID, sessionId)
                            .limit(1)
                            .addSnapshotListener{ value, e ->
                                if (e != null) {
                                    Timber.d("session changed error: %s", e.toString())
                                    //emitter.onError(e)
                                    //return@addSnapshotListener
                                }else{
                                    val sessions = value!!.toObjects(Session::class.java)
                                    if(sessions.size > 0){
                                        Timber.d("session changed %s", sessions.first().toString())
                                        emitter.onNext(sessions.first())
                                    }else{
                                        Timber.d("session changed size < 1")
                                        emitter.onError(NotFoundException())
                                    }
                                }

                            }
                }, BackpressureStrategy.BUFFER)
    }

    fun joinSession(sessionId: String, password: String): Single<Session>{
        return Single.create {emitter ->
            db.collection(SESSIONS)
                    .whereEqualTo(SESSION_ID, sessionId)
                    .limit(1)
                    .addSnapshotListener{ value, e ->
                        if (e != null) {
//                            emitter.onError(e)
//                            return@addSnapshotListener
                        }else{
                            val sessions = value!!.toObjects(Session::class.java)
                            if(sessions.size > 0){
                                val session = sessions.first()
                                if(session.password == password){
                                    emitter.onSuccess(session)
                                }else{
                                    emitter.onError(WrongPasswordException())
                                }
                            }else{
                                emitter.onError(NotFoundException())
                            }
                        }

                    }
        }
    }



    fun saveStory(sessionId: String, story:String, description: String): Single<Story> {
        val storyId = db.collection(STORIES).document().id
        val story = Story(storyId, sessionId, story, description, null)
        return Single
                .create { emitter ->
                    db.collection(STORIES)
                            .document(storyId)
                            .set(story)
                            .addOnSuccessListener { emitter.onSuccess(story) }
                            .addOnFailureListener{ emitter.onError(it)}
                }
    }

    fun setEstimation(estimation: Estimation): Single<Boolean>{
        val map = mapOf(
                "points" to estimation.points,
                "storyId" to estimation.storyId,
                "username" to estimation.username,
                "userId" to estimation.userId)

        return Single
                .create { emitter ->
                    db.collection(STORIES)
                            .document(estimation.storyId)
                            //.update("estimations.${estimation.userId}", estimation)
                            .update("estimations.${estimation.userId}", map)
                            .addOnSuccessListener { emitter.onSuccess(true) }
                            .addOnFailureListener{ emitter.onError(it)}
                }
    }

    fun loadStories(sessionId: String): Flowable<List<Story>>{
        return Flowable.create( { emitter ->
            db.collection(STORIES)
                    .whereEqualTo(SESSION_ID, sessionId)
                    .addSnapshotListener{ value, e ->
                        if (e != null) {
                            emitter.onError(e)
                        }

                        val stories = value!!.toObjects(Story::class.java)
                        emitter.onNext(stories)
                    }
        }, BackpressureStrategy.BUFFER)
    }

    fun loadStory(sessionId: String, currentStory: String): Flowable<Story>{
        return Flowable.create( { emitter ->
            db.collection(STORIES)
                    .whereEqualTo(SESSION_ID, sessionId)
                    .whereEqualTo(STORY_ID, currentStory)
                    .addSnapshotListener{ value, e ->
                        if (e != null) {
                            Timber.d("loadStory error: %s", e.toString())
                            emitter.onError(e)
                            return@addSnapshotListener
                        }


                        val stories = value!!.toObjects(Story::class.java)
                        if(stories.size > 0){
                            Timber.d("loadStory story: %s", stories.first())
                            emitter.onNext(stories.first())
                        }else{
                            //emitter.onError(NotFoundException())
                        }
                    }
        }, BackpressureStrategy.BUFFER)
    }


}