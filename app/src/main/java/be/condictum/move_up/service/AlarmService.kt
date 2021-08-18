package be.condictum.move_up.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.AlarmClock
import be.condictum.move_up.receiver.AlarmReceiver
import be.condictum.move_up.util.Constants
import be.condictum.move_up.util.RandomIntUtil
import android.media.RingtoneManager

import android.media.Ringtone
import android.net.Uri


class AlarmService(private val context: Context) {
    private val alarmManager : AlarmManager? = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager?


    fun setExactAlarm(timeInMillis:Long){

        setAlarm(timeInMillis,
            getPendingIntent(getIntent().apply { action= Constants.ACTION_SET_EXACT_ALARM
            putExtra(Constants.EXTRA_EXACT_ALARM_TIME,timeInMillis)
            }))



    }
    fun setRepetetiveAlarm(timeInMillis:Long){

        setAlarm(timeInMillis,
            getPendingIntent(getIntent().apply { action= Constants.ACTION_SET_REPETITIVE_ALARM
                putExtra(Constants.EXTRA_EXACT_ALARM_TIME,timeInMillis)
            }))

    }



    private fun setAlarm(timeInMillis:Long, pendingIntent: PendingIntent){
        alarmManager?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
               alarmManager.setExactAndAllowWhileIdle(
                   AlarmManager.RTC_WAKEUP,
                   timeInMillis,
                   pendingIntent
               )
            }
            else{
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    timeInMillis,
                    pendingIntent
                )

            }
        }
    }



    private fun getIntent() = Intent(context,AlarmReceiver::class.java)

    private fun getPendingIntent(intent: Intent) = PendingIntent.getBroadcast(context,RandomIntUtil.getRandomInt(),intent,PendingIntent.FLAG_UPDATE_CURRENT)


}