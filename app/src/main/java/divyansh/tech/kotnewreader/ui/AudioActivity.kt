package divyansh.tech.kotnewreader.ui

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
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DataSpec
import com.google.android.exoplayer2.upstream.FileDataSource
import com.google.android.exoplayer2.upstream.FileDataSource.FileDataSourceException
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory
import com.google.android.exoplayer2.upstream.cache.SimpleCache
import divyansh.tech.kotnewreader.R
import kotlinx.android.synthetic.main.activity_audio.*
import java.io.File


class AudioActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio)
    }

    override fun onStart() {
        super.onStart()
        initAudioPlayer()
    }

    private fun initAudioPlayer() {
        Log.i("Audio", "file://" + File(baseContext.getExternalFilesDir(null), "/KotNews/TechReads_news_0.wav").absolutePath)
        val mediaSource = extractMediaSourceFromUri(Uri.parse(File(baseContext.getExternalFilesDir(null), "/KotNews/TechReads_news_0.wav").absolutePath))
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
            prepare(mediaSource, true, true)
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
        val audioSource: MediaSource = ExtractorMediaSource(
            fileDataSource.uri,
            factory, DefaultExtractorsFactory(), null, null
        )
        return audioSource
    }
}