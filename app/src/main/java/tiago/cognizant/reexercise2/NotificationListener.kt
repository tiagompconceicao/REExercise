package tiago.cognizant.reexercise2

import android.content.Context
import android.content.Intent
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.util.Log

class NotificationListener : NotificationListenerService() {

    var context: Context? = null

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }

    override fun onListenerConnected() {
        super.onListenerConnected()
        Log.d("Notifications","Listener connected")
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        Log.d("Notifications","Notification received")

        val intent = Intent("tiago.cognizant.notificationListener")
        intent.putExtra("Notification", sbn)
        sendBroadcast(intent)
    }

    override fun onNotificationRemoved(sbn: StatusBarNotification) {
        Log.d("Notifications","Notification removed")
    }
}