package divyansh.tech.kotnewreader.ui

import android.app.SearchManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.MenuItemCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import divyansh.tech.kotnewreader.R
import divyansh.tech.kotnewreader.database.ArticleDao
import divyansh.tech.kotnewreader.network.api.NewsApi
import divyansh.tech.kotnewreader.network.models.User
import divyansh.tech.kotnewreader.ui.viewModels.newsViewModel
import kotlinx.android.synthetic.main.activity_news.*
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

    private fun initUser() {
        user = intent.getSerializableExtra("User") as User
    }
}