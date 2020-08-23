package divyansh.tech.kotnewreader.ui.activities

import android.Manifest
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import divyansh.tech.kotnewreader.R
import divyansh.tech.kotnewreader.adapters.NewsAdapter
import divyansh.tech.kotnewreader.models.Article
import divyansh.tech.kotnewreader.ui.viewModels.newsViewModel
import divyansh.tech.kotnewreader.utils.Alert
import divyansh.tech.kotnewreader.utils.Constants
import divyansh.tech.kotnewreader.utils.Resource
import kotlinx.android.synthetic.main.activity_audio_player.*
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.io.File
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

@AndroidEntryPoint
class AudioPlayerActivity : AppCompatActivity(), TextToSpeech.OnInitListener, EasyPermissions.PermissionCallbacks {

    @Inject
    lateinit var newsAdapter: NewsAdapter
    lateinit var tts: TextToSpeech
    val viewModel: newsViewModel by viewModels()
    val utteranceId: String = "TechReads"
    val placeholderList: MutableList<String> = ArrayList()
    lateinit var alertDialog: AlertDialog

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)
        initializeTextToSpeech()
        setupListeners()
    }

    override fun onResume() {
        super.onResume()
        val directory = File(baseContext.getExternalFilesDir(null), getString(R.string.audioDir))
        if (!directory.list().isNullOrEmpty()) {
            for (child in directory.list()) {
                File(directory, child).delete()
            }
        }
    }

    private fun initializeTextToSpeech() {
        tts = TextToSpeech(this, this)
        alertDialog = Alert.createAlertDialog(this)
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
        viewModel.getBreakingNews(getString(R.string.countryCode), name)
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.breakingNews.observe(this, Observer {it ->
            when (it) {
                is Resource.Success -> {
                    alertDialog.dismiss()
                    it.data?.let {
                        saveAudio(it.articles)
                    }
                }

                is Resource.Error -> {
                    alertDialog.dismiss()
                    it.message?.let {
                        Toast.makeText(this@AudioPlayerActivity, getString(R.string.failed), Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                is Resource.Loading -> {
                    alertDialog.show()
                }
            }
        })
    }

    private fun hasWritePermissions(): Boolean =
        EasyPermissions.hasPermissions(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)

    private fun hasReadPermissions(): Boolean =
        EasyPermissions.hasPermissions(this, Manifest.permission.READ_EXTERNAL_STORAGE)

    private fun saveAudio(articles: List<Article>) {
        Alert.createAlertDialog(this).show()
        val newses: MutableList<String?> = ArrayList<String?>()
        for (news in articles) {
            Log.i("Audio", news.title!!)
            newses.add(news.title + "  " + news.description)
        }
        if (hasWritePermissions() && hasReadPermissions()) {
            val directory = File(baseContext.getExternalFilesDir(null), getString(R.string.audioDir))
//            if (!directory.list().isNullOrEmpty()) {
//                for (child in directory.list()) {
//                    File(directory, child).delete()
//                }
//            }
            if (!directory.exists()) directory.mkdir()
            for (index in 0 until newses.size) {
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
                    tts.synthesizeToFile(
                        newses.get(index),
                        params,
                        File(cacheDir, utteranceId + "_news-${index}.mp3").path
                    )
                }
            }
            tts.setOnUtteranceProgressListener(object: UtteranceProgressListener() {
                override fun onDone(utteranceId: String?) {
                    if (placeholderList.size == 19) startActivity(Intent(this@AudioPlayerActivity, AudioActivity::class.java))
                    else {
                        Log.i("Audio", "Addind File ${placeholderList.size}")
                        placeholderList.add("Done")
                    }
                }

                override fun onError(utteranceId: String?) {
//                    TODO("Not yet implemented")
                }

                override fun onStart(utteranceId: String?) {
//                    TODO("Not yet implemented")
                }

            })
        } else {
            EasyPermissions.requestPermissions(
                this,
                getString(R.string.rationale_write),
                Constants.RC_WRITE_PERM,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )

            EasyPermissions.requestPermissions(
                this,
                getString(R.string.rationale_read),
                Constants.RC_READ_PERM,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }
    }


    override fun onInit(status: Int) {
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // EasyPermissions handles the request result.
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
//        TODO("Not yet implemented")
    }
}