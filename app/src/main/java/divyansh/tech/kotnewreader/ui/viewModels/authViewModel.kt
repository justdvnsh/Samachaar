package divyansh.tech.kotnewreader.ui.viewModels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.AuthCredential
import divyansh.tech.kotnewreader.network.models.User
import divyansh.tech.kotnewreader.repositories.AuthRepository

class authViewModel @ViewModelInject constructor(
    val authRepository: AuthRepository
): ViewModel() {

    var authenticatedUserLiveData: LiveData<User>? = null
    var createdUserLiveData: LiveData<User>? = null

    fun signInWithGoogle(authCredential: AuthCredential) {
        authenticatedUserLiveData = authRepository.firebaseSignInWithGoogle(authCredential)
    }

    fun createUser(user: User) {
        createdUserLiveData = authRepository.createUserInFirestoreIfNotExists(user)
    }

}