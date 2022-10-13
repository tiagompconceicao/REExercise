package tiago.cognizant.reexercise2

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import tiago.cognizant.reexercise2.model.Contact
import tiago.cognizant.reexercise2.model.Message
import tiago.cognizant.reexercise2.repo.Repository

class MyViewModel(app: Application) : AndroidViewModel(app) {

    val contacts: LiveData<List<Contact>> = MutableLiveData()
    val messages: LiveData<List<Message>> = MutableLiveData()

    private val repository by lazy {
        Repository()
    }

    fun loadContacts(mContext: Context){
        repository.getContacts(mContext, onSuccess = {
            (contacts as MutableLiveData<List<Contact>>).value = it
        }, onError = {
            Toast.makeText(mContext, "Could not get contact list", Toast.LENGTH_LONG).show()
        })
    }

    fun loadMessages(mContext: Context){
        repository.getMessages(mContext, onSuccess = {
            (messages as MutableLiveData<List<Message>>).value = it
        }, onError = {
            Toast.makeText(mContext, "Could not get contact list", Toast.LENGTH_LONG).show()
        })
    }

    fun send(mContext: Context){
        if (contacts.value == null){
            Toast.makeText(mContext,"Contacts not available yet",Toast.LENGTH_SHORT).show()
        } else {
            Log.d("Contacts","View Model: send contacts and messages")
            repository.sendRequest(mContext, contacts.value!!,"contacts")
            repository.sendRequest(mContext, messages.value!!,"messages")
        }


    }
}