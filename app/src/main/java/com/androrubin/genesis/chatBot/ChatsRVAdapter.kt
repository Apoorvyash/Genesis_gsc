package com.androrubin.genesis.chatBot

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.androrubin.genesis.R
import com.androrubin.genesis.ui.aid.book_appointment.AvailableDoctorsDC

class ChatRVAdapter(private val chatsModalArrayList: ArrayList<ChatsModal>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {     // constructor class.
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//        TODO("Not yet implemented")
//    }
//
//    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
//        TODO("Not yet implemented")
//    }
//
//    override fun getItemCount(): Int {
//        TODO("Not yet implemented")
//    }
// variable for our array list and context.

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View
        if(viewType == 0) {
                // below line we are inflating user message layout.
                view = LayoutInflater.from(parent.context).inflate(R.layout.user_msg, parent, false)
                return UserViewHolder(view)
            }
            else {
                // below line we are inflating bot message layout.
                view = LayoutInflater.from(parent.context).inflate(R.layout.bot_msg, parent, false)
                return BotViewHolder(view)
            }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        // this method is use to set data to our layout file.
        val modal = chatsModalArrayList[position]

        if (holder.javaClass == UserViewHolder::class.java){

            val viewHolder = holder as UserViewHolder
            viewHolder.userTV.text = modal.message

        }else{
            val viewHolder = holder as BotViewHolder
            viewHolder.botTV.setText(modal.message)
        }
    }

    override fun getItemCount(): Int {
        // return the size of array list
        return chatsModalArrayList.size
    }

    override fun getItemViewType(position: Int): Int {
        // below line of code is to set position.
        return when (chatsModalArrayList[position].sender) {
            "user" -> 0
            "bot" -> 1
            else -> -1
        }
    }



}

class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var userTV: TextView = itemView.findViewById<TextView>(R.id.idTVUser)
}

class BotViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    // creating a variable
    // for our text view.
    var botTV: TextView = itemView.findViewById<TextView>(R.id.idTVBot)

}
