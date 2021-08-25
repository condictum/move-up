package be.condictum.move_up.notification.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

internal class ResultReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent) {
        if (intent.action != null && intent.action.equals(ACTION_CLICK, ignoreCase = true)) {
            Toast.makeText(context, "CLICK", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        var ACTION_CLICK = "ACTION_CLICK"
    }
}