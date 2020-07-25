package divyansh.tech.kotnewreader.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DataSpec
import com.google.android.exoplayer2.upstream.FileDataSource
import com.google.android.exoplayer2.upstream.FileDataSource.FileDataSourceException
import divyansh.tech.kotnewreader.R
import divyansh.tech.kotnewreader.services.AudioService
import kotlinx.android.synthetic.main.activity_audio.*
import java.io.File


class AudioActivity : AppCompatActivity() {

    lateinit var playerNotificationManager: PlayerNotificationManager
    private var CHANNEL_ID: String = getString(R.string.chennelId)
    private var NOTIFICATION_ID: Int = 12

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio)
    }

    override fun onStart() {
        super.onStart()
        initAudioPlayer()
//        setupPlayerNotificationManager()
    }

    private fun initAudioPlayer() {
        Log.i("Audio", "file://" + File(baseContext.getExternalFilesDir(null), "/KotNews/TechReads_news_0.wav").absolutePath)
        var concatenatedSource: ConcatenatingMediaSource = ConcatenatingMediaSource()
        val directory = File(baseContext.getExternalFilesDir(null), "/KotNews")
        for (child in directory.list()) {
            val mediaSource = extractMediaSourceFromUri(Uri.parse(File(directory, child).absolutePath))
            concatenatedSource.addMediaSource(mediaSource)
        }
        val exoplayer = ExoPlayerFactory.newSimpleInstance(
            baseContext,
            DefaultRenderersFactory(
                baseContext,
                DefaultRenderersFactory.EXTENSION_RENDERER_MODE_OFF
            ),
            DefaultTrackSelector(),
            DefaultLoadControl())
        exoplayer.apply {
            // AudioAttributes here from exoplayer package !!!
            val attr = AudioAttributes.Builder().setUsage(C.USAGE_MEDIA)
                .setContentType(C.CONTENT_TYPE_MUSIC)
                .build()
            // In 2.9.X you don't need to manually handle audio focus :D
            setAudioAttributes(attr, true)
            prepare(concatenatedSource, true, true)
            // THAT IS ALL YOU NEED
            playWhenReady = true
        }
        player_view.player = exoplayer
    }

    private fun extractMediaSourceFromUri(uri: Uri): MediaSource {
        val dataSpec = DataSpec(uri)
        val fileDataSource =
            FileDataSource()
        try {
            fileDataSource.open(dataSpec)
        } catch (e: FileDataSourceException) {
            e.printStackTrace()
        }

        val factory =
            DataSource.Factory { fileDataSource }
        return ExtractorMediaSource(
            fileDataSource.uri,
            factory, DefaultExtractorsFactory(), null, null
        )
    }

    private fun sendCommandToService(action: String) {
        Intent(this, AudioService::class.java).also {
            it.action = action
            startService(it)
        }
    }
}