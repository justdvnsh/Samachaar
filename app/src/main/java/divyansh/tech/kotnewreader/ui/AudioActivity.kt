package divyansh.tech.kotnewreader.ui

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import divyansh.tech.kotnewreader.R
import java.io.File

class AudioActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio)

        initExoplayer()
    }

    private fun initExoplayer() {
        val mediaSource = extractMediaSourceFromUri(Uri.parse(File(baseContext.getExternalFilesDir(null), "/KotNews/news_0.mp3").absolutePath))
        val exoplayer = ExoPlayerFactory.newSimpleInstance(baseContext, DefaultRenderersFactory(baseContext), DefaultTrackSelector(), DefaultLoadControl())
        exoplayer.apply {
            // AudioAttributes here from exoplayer package !!!
            val attr = AudioAttributes.Builder().setUsage(C.USAGE_MEDIA)
                .setContentType(C.CONTENT_TYPE_MUSIC)
                .build()
            // In 2.9.X you don't need to manually handle audio focus :D
            setAudioAttributes(attr, true)
            prepare(mediaSource)
            // THAT IS ALL YOU NEED
            playWhenReady = true
        }
    }

    private fun extractMediaSourceFromUri(uri: Uri): MediaSource {
        val userAgent = Util.getUserAgent(this, "exo")
        return ExtractorMediaSource.Factory(DefaultDataSourceFactory(this, userAgent)).setExtractorsFactory(DefaultExtractorsFactory()).createMediaSource(uri)
    }
}