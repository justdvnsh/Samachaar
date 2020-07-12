package divyansh.tech.kotnewreader.ui

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.GridLayout
import android.widget.GridView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import divyansh.tech.kotnewreader.R
import divyansh.tech.kotnewreader.adapters.NewsAdapter
import divyansh.tech.kotnewreader.ui.viewModels.newsViewModel
import divyansh.tech.kotnewreader.utils.Resource
import kotlinx.android.synthetic.main.activity_audio_player.*
import java.io.File
import java.io.PrintWriter
import javax.inject.Inject

@AndroidEntryPoint
class AudioPlayerActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    @Inject
    lateinit var newsAdapter: NewsAdapter
    lateinit var tts: TextToSpeech
    val viewModel: newsViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)
        initializeTextToSpeech()
        setupListeners()
        setupObservers()
    }

    private fun initializeTextToSpeech() {
        tts = TextToSpeech(this, this)
    }

    private fun setupListeners() {
        business.setOnClickListener {
            viewModel.getBreakingNews("in", getString(R.string.business))
        }
        entertainment.setOnClickListener {
            viewModel.getBreakingNews("in", getString(R.string.entertainment))
        }
        technology.setOnClickListener {
            viewModel.getBreakingNews("in", getString(R.string.tech))
        }
        sports.setOnClickListener {
            viewModel.getBreakingNews("in", getString(R.string.sports))
        }
        health.setOnClickListener {
            viewModel.getBreakingNews("in", getString(R.string.health))
        }
        science.setOnClickListener {
            viewModel.getBreakingNews("in", getString(R.string.science))
        }
        favorites.setOnClickListener {
            viewModel.getAllArticles().observe(this, Observer {
                newsAdapter.differ.submitList(it)
            })
        }
    }

    fun showProgress(progressBar: ProgressBar) {
        progressBar.visibility = View.VISIBLE
    }

    fun hideProgress(progressBar: ProgressBar) {
        progressBar.visibility = View.GONE
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun setupObservers() {
        viewModel.breakingNews.observe(this, Observer {
            when (it) {
                is Resource.Success -> {
                    hideProgress(paginationProgressBar)
                    it.data?.let {
                        var newses: MutableList<String?> = ArrayList<String?>()
                        for (news in it.articles) {
                            Log.i("Audio", news.title!!)
                            newses.add(news.title + "  " + news.description)
                        }
                        Log.i("Audio", newses.toString())
                        for (news in 0 until newses.size) {
                            Log.i("Audio", baseContext.getExternalFilesDir(null).toString())
                            val file = File(baseContext.getExternalFilesDir(null), "KotNews")
                            if (!file.exists()) file.mkdir()
                            val f = File(file, "news-${news}.txt")
                            tts.synthesizeToFile(newses.get(news), null, f, null)
                        }
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

    override fun onInit(status: Int) {
    }
}