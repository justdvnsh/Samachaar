package divyansh.tech.kotnewreader.utils

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import divyansh.tech.kotnewreader.R

class Alert() {

    companion object {
        fun createAlertDialog(context: Context){
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            builder.setView(R.layout.alert_view).setCancelable(true)
            val alert: AlertDialog = builder.create()
            alert.show()
        }
    }

}