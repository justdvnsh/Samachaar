package divyansh.tech.kotnewreader.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import divyansh.tech.kotnewreader.network.models.User
import divyansh.tech.kotnewreader.utils.Constants.Companion.USERS
import javax.inject.Inject

class SplashRepository @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {

    private val usersRef: CollectionReference = firestore.collection(USERS)
    private val user: User = User()

    fun checkIfUserIsAuthenticatedInFirebase(): MutableLiveData<User> {
        val isUserAuthenticatedInFirebaseLiveData: MutableLiveData<User> = MutableLiveData()
        val firebaseUser = firebaseAuth.currentUser
        if (firebaseUser == null) {
            user.isAuthenticated  = false
            isUserAuthenticatedInFirebaseLiveData.value = user
        } else {
            user.email = firebaseUser.email
            user.name = firebaseUser.displayName
            user.uid = firebaseUser.uid
            user.isAuthenticated = true
            isUserAuthenticatedInFirebaseLiveData.value = user
        }

        return isUserAuthenticatedInFirebaseLiveData
    }

    fun addUserToLiveData(email: String): MutableLiveData<User> {
        val userMutableLiveData: MutableLiveData<User> = MutableLiveData()
        usersRef.document(email).get().addOnCompleteListener {
            if (it.isSuccessful) {
                val document = it.result
                if (document?.exists()!!) {
                    val user: User = User(
                        uid = document.get("uid") as String,
                        email = document.get("email") as String,
                        name = document.get("name") as String
                    )
                    userMutableLiveData.value = user
                }
            } else {
                Log.i("SPLASHREPO", it.exception?.message)
            }
        }

        return userMutableLiveData
    }
}