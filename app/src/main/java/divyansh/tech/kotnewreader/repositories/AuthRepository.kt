package divyansh.tech.kotnewreader.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import divyansh.tech.kotnewreader.models.User
import divyansh.tech.kotnewreader.utils.Constants.Companion.USERS
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFirestore: FirebaseFirestore
) {

    private val usersRef = firebaseFirestore.collection(USERS)

    fun testIfInjected() {
        Log.i("INJECTED_FIREBASE", firebaseAuth.hashCode().toString())
    }

    fun firebaseSignInWithGoogle(authCredential: AuthCredential): MutableLiveData<User> {
        val authenticatedUserMutableLiveData: MutableLiveData<User> = MutableLiveData()
        firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener {authResult ->
            if (authResult.isSuccessful) {
                val isNewUser = authResult.result?.additionalUserInfo?.isNewUser
                val firebaseUser = firebaseAuth.currentUser
                firebaseUser?.let {
                    val user = User(
                        it.uid, it.displayName.toString(), it.email.toString()
                    )
                    user.isNew = isNewUser
                    authenticatedUserMutableLiveData.value = user
                }
            } else {
                Log.i("AUTHREPO", "FAILED ${authResult.exception?.message}")
            }
        }

        return authenticatedUserMutableLiveData
    }

    fun firebaseSignUpWithEmailAndPassword(email: String, password: String): MutableLiveData<User> {
        val authenticatedUserMutableLiveData: MutableLiveData<User> = MutableLiveData()
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { authResult ->
                if (authResult.isSuccessful) {
                    Log.i("Auth", "Registration succesful")
                    val isNewUser = authResult.result?.additionalUserInfo?.isNewUser
                    val firebaseUser = firebaseAuth.currentUser
                    val uidRef: DocumentReference = usersRef.document(firebaseUser?.email!!)
                    firebaseUser.let {
                        val user = User(
                            uid = it.uid,
                            email = it.email.toString(),
                            isCreated = true,
                            isNew = isNewUser
                        )
                        authenticatedUserMutableLiveData.value = user
                    }
                } else {
                    Log.i("AUTHREPO SETTING", authResult.exception?.message)
                }
            }
        return authenticatedUserMutableLiveData
    }

    fun firebaseSignInWithEmailAndPassword(email: String, password: String): MutableLiveData<User> {
        val authenticatedUserMutableLiveData: MutableLiveData<User> = MutableLiveData()
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {authResult ->
            if (authResult.isSuccessful) {
                val isNewUser = authResult.result?.additionalUserInfo?.isNewUser
                val firebaseUser = firebaseAuth.currentUser
                firebaseUser?.let {
                    val user = User(
                        it.uid, it.email.toString()
                    )
                    user.isNew = isNewUser
                    authenticatedUserMutableLiveData.value = user
                }
            } else {
                Log.i("AUTHREPO", "FAILED ${authResult.exception?.message}")
            }
        }

        return authenticatedUserMutableLiveData
    }

    fun createUserInFirestoreIfNotExists(user: User): MutableLiveData<User> {
        val newUserMutableLiveData: MutableLiveData<User> = MutableLiveData()
        val uidRef: DocumentReference = usersRef.document(user.email!!)
        uidRef.get().addOnCompleteListener {task ->
            if (task.isSuccessful) {
                val document = task.result
                if (!document?.exists()!!) {
                    uidRef.set(user).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            user.isCreated = true
                            newUserMutableLiveData.value = user
                        } else {
                            Log.i("AUTHREPO SETTING", task.exception?.message)
                        }
                    }
                } else {
                    newUserMutableLiveData.value = user
                }
            } else {
                Log.i("AUTHREPO CREATING USER", "FAILED ${task.exception?.message}")
            }
        }

        return newUserMutableLiveData
    }

}