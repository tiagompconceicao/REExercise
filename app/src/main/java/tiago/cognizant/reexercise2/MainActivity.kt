package tiago.cognizant.reexercise2

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.service.notification.StatusBarNotification
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import tiago.cognizant.reexercise2.databinding.ActivityMainBinding
import tiago.cognizant.reexercise2.view.ContactListAdapter
import tiago.cognizant.reexercise2.view.MessageListAdapter

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModel: MyViewModel by viewModels()
    private lateinit var broadcastReceiver: BroadcastNotificationReceiver
    private var notificationID = 0

    private external fun getString(): String
    private external fun getContentView(string:String): String

    companion object {
        init {
            System.loadLibrary("native-lib")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        var aaa = getContentView("RUxGX0NURl9lbmNyeXB0ZWRfeQ==")
        //val intent = Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS")
        //startActivity(intent)

        val mContactRecycler = instantiateRecyclerView(binding.recyclerView)
        val mMessageRecycler = instantiateRecyclerView(binding.recyclerViewMsg)


        loadMessages()
        loadContacts()
        isNotificationServiceEnabled()


        viewModel.messages.observe(this) {
            val mMessageAdapter = MessageListAdapter(it)
            mMessageRecycler.adapter = mMessageAdapter
        }

        viewModel.contacts.observe(this) {
            val mContactAdapter = ContactListAdapter(it)
            mContactRecycler.adapter = mContactAdapter
        }

        binding.send.setOnClickListener{
            viewModel.send(this)
        }

        // Register a receiver to tell the MainActivity when a notification has been received
        broadcastReceiver = BroadcastNotificationReceiver(this)
        val intentFilter = IntentFilter()
        intentFilter.addAction("tiago.cognizant.notificationListener")
        registerReceiver(broadcastReceiver, intentFilter)

        binding.genNotification.setOnClickListener{
            createNotificationChannel()

            val builder = NotificationCompat.Builder(this, "0")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("My notification")
                .setContentText("Much longer text that cannot fit one line...")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            with(NotificationManagerCompat.from(this)) {
                // notificationId is a unique int for each notification that you must define
                notify(notificationID, builder.build())
            }

            notificationID++
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(broadcastReceiver)
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        val name = "channel_name"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel("0", name, importance).apply {
        }
        // Register the channel with the system
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun isNotificationServiceEnabled(): Boolean {
        val notificationString = getString()
        return if (checkSelfPermission(Manifest.permission.BIND_NOTIFICATION_LISTENER_SERVICE) != PackageManager.PERMISSION_GRANTED) {
            //Request read contacts permission
            requestPermissions(arrayOf(Manifest.permission.BIND_NOTIFICATION_LISTENER_SERVICE), 2)
            false
        } else {
            true
        }
    }

    private fun loadContacts() {
        if (checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            //Request read contacts permission
            requestPermissions(arrayOf(Manifest.permission.READ_CONTACTS, Manifest.permission.READ_SMS, Manifest.permission.BIND_NOTIFICATION_LISTENER_SERVICE),
                0)
        } else {
            Log.d("Contacts","Gonna load contacts")
            viewModel.loadContacts(this)
        }
    }

    private fun loadMessages() {
        if (checkSelfPermission(Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            //Request read contacts permission
            requestPermissions(arrayOf(Manifest.permission.READ_SMS, Manifest.permission.READ_CONTACTS, Manifest.permission.BIND_NOTIFICATION_LISTENER_SERVICE),
                1)
        } else {
            Log.d("Contacts","Gonna load contacts")
            viewModel.loadMessages(this)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        //Log.d("Permissions","Permissions result: ${grantResults[0]}, for request: $requestCode")

        if (requestCode == 0 && grantResults.isNotEmpty()) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadContacts()
            } else {
                //Permission not granted after request
            }
        } else if(requestCode == 1 && grantResults.isNotEmpty()){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadMessages()
            } else {
                //Permission not granted after request
            }
        }
    }

    private fun instantiateRecyclerView(recyclerView: RecyclerView): RecyclerView {
        val mLayoutManager = LinearLayoutManager(this)
        mLayoutManager.reverseLayout = false
        recyclerView.layoutManager = mLayoutManager
        return recyclerView
    }

    class BroadcastNotificationReceiver(private val mContext: Context) : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            Log.d("Notifications","Notification received in Activity")

            val receivedNotification: StatusBarNotification? = intent.getParcelableExtra("Notification")
            val title = receivedNotification?.notification?.extras?.getString("android.title");
            Toast.makeText(mContext, title,Toast.LENGTH_SHORT).show()
        }
    }

}

