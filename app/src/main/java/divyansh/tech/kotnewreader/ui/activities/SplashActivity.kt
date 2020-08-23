package divyansh.tech.kotnewreader.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import divyansh.tech.kotnewreader.R
import divyansh.tech.kotnewreader.ui.viewModels.splashViewModel

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    val viewModel: splashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        checkIfUserIsAuthenticated()
    }

    private fun checkIfUserIsAuthenticated() {
        viewModel.checkIfUserIsAuthenticated()
        viewModel.isUserAuthenticatedLiveData?.observe(this, Observer {
            if (it.email == null) {
                Log.i("SPLASH", it.toString() + it.name)
            }
            if (!it.isAuthenticated!!) startActivity(Intent(this@SplashActivity, AuthActivity::class.java))
            else getUserFromDatabase(it.email!!)
        })
    }

    private fun getUserFromDatabase(email: String?) {
        viewModel.setEmail(email!!)
        viewModel.userLiveData?.observe(this, Observer {
            val intent = Intent(this@SplashActivity, NewsActivity::class.java)
            intent.putExtra(getString(R.string.userArgument), it)
            startActivity(intent)
            finish()
        })
    }
}