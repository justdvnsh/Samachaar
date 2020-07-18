//package divyansh.tech.kotnewreader.adapters
//
//import android.app.PendingIntent
//import android.graphics.Bitmap
//import com.google.android.exoplayer2.Player
//import com.google.android.exoplayer2.ui.PlayerNotificationManager
//
//class DescriptionAdapter: PlayerNotificationManager.MediaDescriptionAdapter {
//    override fun createCurrentContentIntent(player: Player?): PendingIntent? {
//        TODO("Not yet implemented")
//    }
//
//    override fun getCurrentContentText(player: Player?): String? {
//        return player?.currentWindowIndex.toString()
//    }
//
//    override fun getCurrentContentTitle(player: Player?): String {
//        val window: Int = player?.getCurrentWindowIndex()!!
//        return window.toString()
//    }
//
//    override fun getCurrentLargeIcon(
//        player: Player?,
//        callback: PlayerNotificationManager.BitmapCallback?
//    ): Bitmap? {
////        TODO("Not yet implemented")
//    }
//}