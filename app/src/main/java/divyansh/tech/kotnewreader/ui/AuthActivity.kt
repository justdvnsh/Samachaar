package divyansh.tech.kotnewreader.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import divyansh.tech.kotnewreader.R
import kotlinx.android.synthetic.main.activity_auth.*

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        google_sign_in_button.setOnClickListener {
            startActivity(Intent(this@AuthActivity, NewsActivity::class.java))
        }
    }
}