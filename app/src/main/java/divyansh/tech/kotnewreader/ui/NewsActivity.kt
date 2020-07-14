package divyansh.tech.kotnewreader.ui

import android.Manifest
import android.app.SearchManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.MenuItemCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import divyansh.tech.kotnewreader.R
import divyansh.tech.kotnewreader.database.ArticleDao
import divyansh.tech.kotnewreader.network.api.NewsApi
import divyansh.tech.kotnewreader.network.models.User
import divyansh.tech.kotnewreader.ui.viewModels.newsViewModel
import kotlinx.android.synthetic.main.activity_news.*
import pub.devrel.easypermissions.EasyPermissions
import javax.inject.Inject

@AndroidEntryPoint
class NewsActivity : AppCompatActivity() {

    val viewModel: newsViewModel by viewModels()
    lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        bottomNavigationView.setupWithNavController(newsNavHostFragment.findNavController())
        viewModel.newRepository.testIfInjected()
        initUser()
    }

    private fun hasLocationPermission(): Boolean = EasyPermissions.hasPermissions(this, Manifest.permission.ACCESS_COARSE_LOCATION)

    private fun fetchLocation() {
        if (hasLocationPermission()) {
            // do something .. Fetch Location
        } else Toast.makeText(this, "Location Permissions have not been allowed", Toast.LENGTH_SHORT).show()
    }

    private fun initUser() {
        user = intent.getSerializableExtra("User") as User
    }
}