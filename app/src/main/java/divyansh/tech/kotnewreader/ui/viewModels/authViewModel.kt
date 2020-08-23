package divyansh.tech.kotnewreader.ui.viewModels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import divyansh.tech.kotnewreader.models.User
import divyansh.tech.kotnewreader.repositories.AuthRepository

class authViewModel @ViewModelInject constructor(
    val authRepository: AuthRepository
): ViewModel() {

    var authenticatedUserLiveData: LiveData<User>? = null
    var createdUserLiveData: LiveData<User>? = null

    fun login(email: String, password: String) {
        authenticatedUserLiveData = authRepository.firebaseSignInWithEmailAndPassword(email, password)
    }

    fun register(email: String, password: String) {
        authenticatedUserLiveData = authRepository.firebaseSignUpWithEmailAndPassword(email, password)
    }

    fun createUser(user: User) {
        createdUserLiveData = authRepository.createUserInFirestoreIfNotExists(user)
    }

}