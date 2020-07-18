package divyansh.tech.kotnewreader.ui

import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
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
import kotlinx.android.synthetic.main.activity_audio.*
import java.io.File


class AudioActivity : AppCompatActivity() {

    lateinit var playerNotificationManager: PlayerNotificationManager
    private var CHANNEL_ID: String = "TechReads"
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
        val audioSource: MediaSource = ExtractorMediaSource(
            fileDataSource.uri,
            factory, DefaultExtractorsFactory(), null, null
        )
        return audioSource
    }

//    private fun showNotification(state: PlaybackStateCompat) {
//
//        // You only need to create the channel on API 26+ devices
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            createChannel()
//        }
//        val builder: NotificationCompat.Builder = NotificationCompat.Builder(this, CHANNEL_ID)
//        val icon: Int
//        val play_pause: String
//        if (state.getState() === PlaybackStateCompat.STATE_PLAYING) {
//            icon = R.drawable.exo_controls_pause
//            play_pause = "Pause"
//        } else {
//            icon = R.drawable.exo_controls_play
//            play_pause = "play"
//        }
//        val playPauseAction: NotificationCompat.Action = Action(
//            icon, play_pause,
//            MediaButtonReceiver.buildMediaButtonPendingIntent(
//                this,
//                PlaybackStateCompat.ACTION_PLAY_PAUSE
//            )
//        )
//        val restartAction: NotificationCompat.Action = Action(
//            R.drawable.exo_controls_previous, getString(R.string.restart),
//            MediaButtonReceiver.buildMediaButtonPendingIntent(
//                this,
//                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
//            )
//        )
//        val contentPendingIntent: PendingIntent = PendingIntent.getActivity(
//            this, 0, Intent(
//                this,
//                MainActivity::class.java
//            ), 0
//        )
//        token = mMediaSession.getSessionToken()
//        builder.setContentTitle(getString(R.string.guess))
//            .setContentText(getString(R.string.notification_text))
//            .setContentIntent(contentPendingIntent)
//            .setSmallIcon(R.drawable.ic_music_note)
//            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
//            .addAction(restartAction)
//            .addAction(playPauseAction)
//            .setStyle(
//                MediaStyle()
//                    .setMediaSession(token)
//                    .setShowActionsInCompactView(0, 1)
//            )
//        mNotificationManager =
//            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        mNotificationManager.notify(0, builder.build())
//    }
//
//    /**
//     * Broadcast Receiver registered to receive the MEDIA_BUTTON intent coming from clients
//     */
//    class MediaReceiver : BroadcastReceiver() {
//        fun onReceive(context: Context?, intent: Intent?) {
//            MediaButtonReceiver.handleIntent(mMediaSession, intent)
//        }
//    }
//
//    /**
//     * The NotificationCompat class does not create a channel for you. You still have to create a channel yourself
//     */
//    @RequiresApi(Build.VERSION_CODES.O)
//    private fun createChannel() {
//        val mNotificationManager: NotificationManager =
//            this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//        // The id of the channel.
//        val id = CHANNEL_ID
//        // The user-visible name of the channel.
//        val name: CharSequence = "Media playback"
//        // The user-visible description of the channel.
//        val description = "Media playback controls"
//        val importance: Int = NotificationManager.IMPORTANCE_LOW
//        val mChannel = NotificationChannel(id, name, importance)
//        // Configure the notification channel.
//        mChannel.setDescription(description)
//        mChannel.setShowBadge(false)
//        mChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC)
//        mNotificationManager.createNotificationChannel(mChannel)
//    }
}