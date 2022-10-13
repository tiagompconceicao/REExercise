package tiago.cognizant.reexercise2.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import tiago.cognizant.reexercise2.R
import tiago.cognizant.reexercise2.model.Message
import java.text.SimpleDateFormat

class MessageListAdapter(messagesList: List<Message>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val messagesList = messagesList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.messages_list_item, parent, false)
        return ContactHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message: Message = messagesList[position]
        return (holder as ContactHolder?)!!.bind(message)
    }

    override fun getItemCount(): Int {
        return messagesList.size
    }

    private inner class ContactHolder constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var name: TextView = itemView.findViewById(R.id.msg_name)
        var date: TextView = itemView.findViewById(R.id.msg_timestamp)
        var text : TextView = itemView.findViewById(R.id.msg_text)
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy hh:mm")


        fun bind(message: Message) {
            name.text = message.number
            date.text = simpleDateFormat.format(message.date.toLong())
            text.text = message.text
        }
    }

}