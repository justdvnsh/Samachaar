package divyansh.tech.kotnewreader.ui

import android.Manifest
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import divyansh.tech.kotnewreader.R
import divyansh.tech.kotnewreader.adapters.NewsAdapter
import divyansh.tech.kotnewreader.network.models.Article
import divyansh.tech.kotnewreader.ui.viewModels.newsViewModel
import divyansh.tech.kotnewreader.utils.Resource
import kotlinx.android.synthetic.main.activity_audio_player.*
import pub.devrel.easypermissions.EasyPermissions
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

@AndroidEntryPoint
class AudioPlayerActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    @Inject
    lateinit var newsAdapter: NewsAdapter
    lateinit var tts: TextToSpeech
    val viewModel: newsViewModel by viewModels()
    val utteranceId: String = "TechReads"

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)
        initializeTextToSpeech()
        setupListeners()
    }

    private fun initializeTextToSpeech() {
        tts = TextToSpeech(this, this)
    }

    private fun setupListeners() {
        business.setOnClickListener {
            loadArticlesAndDirectory(getString(R.string.business))
        }
        entertainment.setOnClickListener {
            loadArticlesAndDirectory(getString(R.string.entertainment))
        }
        technology.setOnClickListener {
            loadArticlesAndDirectory(getString(R.string.tech))
        }
        sports.setOnClickListener {
            loadArticlesAndDirectory(getString(R.string.sports))
        }
        health.setOnClickListener {
            loadArticlesAndDirectory(getString(R.string.health))
        }
        science.setOnClickListener {
            loadArticlesAndDirectory(getString(R.string.science))
        }
        favorites.setOnClickListener {
            viewModel.getAllArticles().observe(this, Observer {
                saveAudio(it)
            })
        }
    }

    private fun loadArticlesAndDirectory(name: String) {
        viewModel.getBreakingNews("in", name)
        setupObservers()
    }

    fun showProgress(progressBar: ProgressBar) {
        progressBar.visibility = View.VISIBLE
    }

    fun hideProgress(progressBar: ProgressBar) {
        progressBar.visibility = View.GONE
    }

    fun setupObservers() {
        viewModel.breakingNews.observe(this, Observer {
            when (it) {
                is Resource.Success -> {
                    hideProgress(paginationProgressBar)
                    it.data?.let {
                        saveAudio(it.articles)
                    }
                }

                is Resource.Error -> {
                    hideProgress(paginationProgressBar)
                    it.message?.let {
                        Toast.makeText(this@AudioPlayerActivity, "Failed", Toast.LENGTH_SHORT).show()
                    }
                }

                is Resource.Loading -> {
                    showProgress(paginationProgressBar)
                }
            }
        })
    }

    private fun hasWritePermissions(): Boolean = EasyPermissions.hasPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    private fun hasReadPermissions(): Boolean = EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)

    private fun saveAudio(articles: List<Article>) {
        var newses: MutableList<String?> = ArrayList<String?>()
        for (news in articles) {
            Log.i("Audio", news.title!!)
            newses.add(news.title + "  " + news.description)
        }
        if (hasWritePermissions() && hasReadPermissions()) {
            val directory = File(baseContext.getExternalFilesDir(null) , "/KotNews")
            if (!directory.exists()) directory.mkdir()
            for (index in 0 until newses.size) {
                Log.i("Audio", cacheDir.toString())
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    tts.synthesizeToFile(
                        newses.get(index),
                        null,
                        File(directory, utteranceId + "_news_${index}.wav"),
                        utteranceId
                    )
                } else {
                    val params: HashMap<String, String> = HashMap()
                    params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, utteranceId);
                    tts.synthesizeToFile(newses.get(index), params, File(cacheDir, utteranceId + "_news-${index}.mp3").path)
                }
            }
        } else Toast.makeText(this, "Permissions Not Allowed", Toast.LENGTH_SHORT).show()
    }


    override fun onInit(status: Int) {
    }
}