package divyansh.tech.kotnewreader.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.AndroidEntryPoint
import divyansh.tech.kotnewreader.R
import divyansh.tech.kotnewreader.network.models.User
import divyansh.tech.kotnewreader.ui.viewModels.authViewModel
import divyansh.tech.kotnewreader.utils.Constants.Companion.RC_SIGN_IN
import kotlinx.android.synthetic.main.activity_auth.*
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {

    val viewModel: authViewModel by viewModels()
    lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        initSignInButton()
        initGoogleSignInClient()
        viewModel.authRepository.testIfInjected()
    }

    private fun initGoogleSignInClient() {
        val googleSignInOptions: GoogleSignInOptions = GoogleSignInOptions.Builder(
            GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()

        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)
    }

    private fun initSignInButton() {
        google_sign_in_button.setOnClickListener {
            startActivityForResult(googleSignInClient.signInIntent, RC_SIGN_IN)
        }
    }

    private fun getGoogleAuthCredentials(googleSignInAccount: GoogleSignInAccount) {
        val googleAuthCredentials: AuthCredential = GoogleAuthProvider.getCredential(googleSignInAccount.idToken, null)
        signInWithGoogleAuthCredential(googleAuthCredentials)
    }

    private fun signInWithGoogleAuthCredential(googleAuthCredential: AuthCredential) {
        viewModel.signInWithGoogle(googleAuthCredential)
        viewModel.authenticatedUserLiveData?.observe(this@AuthActivity, Observer {
            if (it.isNew!!) createNewUser(it)
            else goToNewsActivity(it)
        })
    }

    private fun createNewUser(user: User) {
        viewModel.createUser(user)
        viewModel.createdUserLiveData?.observe(this@AuthActivity, Observer {
            goToNewsActivity(user)
        })
    }

    private fun goToNewsActivity(user: User) {
        val intent: Intent = Intent(this, NewsActivity::class.java)
        intent.putExtra("User", user)
        startActivity(intent)
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.i("AUTHACTIVITY", "firebaseAuthWithGoogle:" + account.id)
                getGoogleAuthCredentials(account)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.i("AUTHACTIVITY", "Google sign in failed", e)
                // ...
            }
        }
    }
}