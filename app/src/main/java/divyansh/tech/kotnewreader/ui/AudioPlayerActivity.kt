package divyansh.tech.kotnewreader.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import divyansh.tech.kotnewreader.R

class AudioPlayerActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    lateinit var tts: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player)
        initializeTextToSpeech()
    }

    private fun initializeTextToSpeech() {
        tts = TextToSpeech(this, this)
    }

    override fun onInit(status: Int) {
    }
}