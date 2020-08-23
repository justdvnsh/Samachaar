package divyansh.tech.kotnewreader.ui.viewModels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import divyansh.tech.kotnewreader.models.User
import divyansh.tech.kotnewreader.repositories.SplashRepository

class splashViewModel @ViewModelInject constructor(
    private val splashRepository: SplashRepository
): ViewModel() {

    var isUserAuthenticatedLiveData: LiveData<User>? = null
    var userLiveData: LiveData<User>? = null

    fun checkIfUserIsAuthenticated() {
        isUserAuthenticatedLiveData = splashRepository.checkIfUserIsAuthenticatedInFirebase()
    }

    fun setEmail(email: String) {
        userLiveData = splashRepository.addUserToLiveData(email)
    }

}