package divyansh.tech.kotnewreader.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import divyansh.tech.kotnewreader.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            // navigate after 3 sec
            startActivity(Intent(this, AuthActivity::class.java))

            // finish
            finish()
        }, 3000)
    }
}