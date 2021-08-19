package be.condictum.move_up.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.icu.text.CaseMap
import android.widget.Toast
import be.condictum.move_up.util.Constants
import io.karn.notify.Notify

class AlarmReceiver:BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val timeInMillis = intent?.getLongExtra(Constants.EXTRA_EXACT_ALARM_TIME,0L)
        when(intent?.action){
            Constants.EXTRA_EXACT_ALARM_TIME ->{
                buildNotification(context, "set exact time","msg")
            }
        }

    }

    private fun buildNotification(context: Context?,title:String,message:String){
        if (context != null) {
            Notify
                .with(context)
                .content {
                    this.title=title
                    this.text="your exam ıs today"
                }.show()
         }
    }

}