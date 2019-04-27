package pl.michalboryczko.exercise.source.api.firebase

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.*
import pl.michalboryczko.exercise.model.api.Estimation
import javax.inject.Singleton
import java.lang.Exception
import pl.michalboryczko.exercise.model.api.Session
import pl.michalboryczko.exercise.model.api.Story
import pl.michalboryczko.exercise.model.api.call.LoginCall
import pl.michalboryczko.exercise.model.api.call.UserCall


@Singleton
class FirestoreApiService {

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val db: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }

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

    //save Session object AT sessionId node
    fun saveSession(session: Session): Single<Boolean> {
        return Single
                .create { emitter ->
                    db.collection("sessions")
                            .document(session.sessionId)
                            .set(session)
                            .addOnSuccessListener { emitter.onSuccess(true) }
                            .addOnFailureListener{ emitter.onError(it)}
                }
    }

    fun saveStory(story: Story): Single<Boolean> {
        return Single
                .create { emitter ->
                    db.collection("stories")
                            .document(story.storyId)
                            .set(story)
                            .addOnSuccessListener { emitter.onSuccess(true) }
                            .addOnFailureListener{ emitter.onError(it)}
                }
    }

    fun setEstimation(estimation: Estimation): Single<Boolean>{
        return Single
                .create { emitter ->
                    db.collection("stories")
                            .document(estimation.storyId)
                            .update("estimations.${estimation.userId}", estimation.points)
                            .addOnSuccessListener { emitter.onSuccess(true) }
                            .addOnFailureListener{ emitter.onError(it)}
                }
    }

    /*fun saveStory(story: Story): Single<Boolean> {
        val batch = db.batch()

        //set the value of story
        val storyRef = db.collection("stories").document(story.sessionId)
        batch.set(storyRef, story)

        //update current pointer
        val sessionRef = db.collection("sessions").document(story.sessionId)
        batch.update(sessionRef, "currentStory", story.storyId)


        return Single
                .create { emitter ->
                    batch.commit()
                            .addOnSuccessListener { emitter.onSuccess(true) }
                            .addOnFailureListener{ emitter.onError(it)}
                }
    }*/



    /*fun saveEstimation(estimation: Estimation): Single<Boolean> {
        return Single
                .create { emitter ->
                    db.collection("estimations")
                            .document("${estimation.storyId}/")
                            .set()
                            .addOnSuccessListener { emitter.onSuccess(true) }
                            .addOnFailureListener{ emitter.onError(it)}
                }
    }*/


    /*fun findFriendByEmail(email: String): Single<User> {
        val dbRef = db.collection("users")
        return Single
                .create { emitter ->
                    dbRef
                            .whereEqualTo("email", email)
                            .get()
                            .addOnCompleteListener{ task ->
                                if (task.isSuccessful) {
                                    val document = task.result
                                    document?.let {
                                        val foundUsers = it.toObjects(User::class.java)
                                        if(foundUsers.isNotEmpty())
                                            emitter.onSuccess(foundUsers.first())
                                        else
                                            emitter.onError(NoSuchElementException())
                                    }
                                } else {
                                    emitter.onError(Exception("no friend found"))
                                }
                            }
                }
    }

    fun findFriendByUid(uid: String): Single<User> {
        val dbRef = db.collection("users")
        return Single
                .create { emitter ->
                    dbRef
                            .whereEqualTo("storyId", uid)
                            .get()
                            .addOnCompleteListener{ task ->
                                if (task.isSuccessful) {
                                    val document = task.result
                                    document?.let {
                                        val foundUsers = it.toObjects(User::class.java)
                                        if(foundUsers.isNotEmpty())
                                            emitter.onSuccess(foundUsers.first())
                                        else
                                            emitter.onError(NoSuchElementException())
                                    }
                                } else {
                                    emitter.onError(Exception("no friend found"))
                                }
                            }
                }
    }

    fun getUserInvitations(): Single<List<Game>> {
        val currentUid = auth.currentUser?.uid
        val dbRef = db.collection("games")

        return if(currentUid != null){
            Single.create { emitter ->
                dbRef
                        .whereEqualTo("contestantId", currentUid)
                        .get()
                        .addOnCompleteListener{ task ->
                            if (task.isSuccessful) {
                                val document = task.result
                                document?.let {
                                    val foundGames = it.toObjects(Game::class.java)
                                    emitter.onSuccess(foundGames)
                                }
                            } else {
                                emitter.onError(Exception("no friend found"))
                            }
                        }
            }
        } else
            Single.create { emitter -> emitter.onError(UnauthorizedException())}
    }

    fun inviteFriendToGame(contestant: UserVisibleData): Single<Boolean> {
        return getUser()
                .flatMap { inviteFriendToGame(contestant, it) }
    }

    private fun inviteFriendToGame(contestant: UserVisibleData, owner: UserVisibleData): Single<Boolean> {
        val seed = Random().nextLong()
        return if(contestant != null && owner != null){
            val ref = db.collection("games").document()
            val game = Game(ref.storyId, contestant.storyId, owner.storyId, contestant, owner, seed, GameStatus.WAITING_FOR_OPONENT,
                    0, 0, false, false)
            Single.create { emitter ->
                ref.set(game)
                        .addOnSuccessListener { emitter.onSuccess(true) }
                        .addOnFailureListener{ emitter.onError(it)}
            }
        }else
            Single.create { emitter -> emitter.onError(UnauthorizedException())}
    }

    fun getGameObservable(gameId: String): Observable<Oponent> {
        val ref = db.collection("games").document(gameId)
        return Observable.create { emitter ->
            ref.addSnapshotListener{ snapshot, e ->
                if (e != null) {
                    //emitter.onError()
                }

                *//* val source = if (snapshot != null && snapshot.metadata.hasPendingWrites())
                     "Local"
                 else
                     "Server"*//*

                if (snapshot != null && snapshot.exists()) {
                    val game = snapshot.toObject(Game::class.java)
                    if(game != null){
                        val uid = auth.currentUser?.uid
                        if(uid != null){
                            val point = if(uid == game.contestantId) game.contestantPoints else game.ownerPoints
                            val isFinished = if(uid == game.contestantId) game.contestantFinished else game.ownerFinished
                            val oponent = Oponent(game.seed, point, game.status, isFinished)
                            emitter.onNext(oponent)
                        }

                        else
                            emitter.onError(UnauthorizedException())
                    }

                } else {
                    emitter.onError(UnknownError())
                }
            }
        }
    }

    fun finishContestantGame(gameId: String): Single<Boolean>
            = finishGame(gameId, "contestantFinished")

    fun finishOwnerGame(gameId: String): Single<Boolean>
            = finishGame(gameId, "ownerFinished")

    fun saveCurrentContestantScore(gameId: String, score: Int): Single<Boolean>
            = saveCurrentScore(gameId, score, "contestantPoints")

    fun saveCurrentOwnerScore(gameId: String, score: Int): Single<Boolean>
            = saveCurrentScore(gameId, score, "ownerPoints")

    private fun finishGame(gameId: String, field: String): Single<Boolean> {
        val ref = db.collection("games").document(gameId)
        return Single
                .create { emitter ->
                    ref
                            .update(field, true)
                            .addOnSuccessListener { emitter.onSuccess(true) }
                            .addOnFailureListener{ emitter.onError(it)}
                }
    }

    private fun saveCurrentScore(gameId: String, score: Int, field: String): Single<Boolean> {
        val ref = db.collection("games").document(gameId)
        return Single
                .create { emitter ->
                    ref
                            .update(field, score)
                            .addOnSuccessListener { emitter.onSuccess(true) }
                            .addOnFailureListener{ emitter.onError(it)}
                }
    }

    fun getUser(): Single<UserVisibleData> {
        val currentUser = auth.currentUser
        return if(currentUser != null){
            val email = if(currentUser.email != null) currentUser.email!! else ""
            val username = if(currentUser.displayName != null) currentUser.displayName!! else ""
            Single.just(UserVisibleData(currentUser.uid, email, username))
        } else
            Single.error(NoSuchFieldException())
    }

    fun saveUserToDatabase(user: User): Single<String> {
        return Single
                .create { emitter ->
                    db.collection("users")
                            .add(user)
                            .addOnSuccessListener { emitter.onSuccess(user.storyId) }
                            .addOnFailureListener{ emitter.onError(it)}
                }
    }*/
}