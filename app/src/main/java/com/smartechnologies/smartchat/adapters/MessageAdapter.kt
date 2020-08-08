package com.smartechnologies.smartchat.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthActionCodeException
import com.google.firebase.auth.FirebaseUser
import com.smartechnologies.smartchat.R
import com.smartechnologies.smartchat.activities.MessageActivity
import com.smartechnologies.smartchat.models.Chat
import com.squareup.picasso.Picasso

class MessageAdapter(
    private val context: Context,
    private val chatList: ArrayList<Chat>
) : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    private val MSG_TYPE_LEFT = 0
    private val MSG_TYPE_RIGHT = 1

    lateinit var firebaseUser: FirebaseUser

    class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val showMessage: TextView = view.findViewById(R.id.txtChatMessage)
        val showMessageTimeStamp: TextView = view.findViewById(R.id.txtMessageTimeStamp)
        val chatMessageContainer: RelativeLayout = view.findViewById(R.id.txtChatMessageContainer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return if (viewType == MSG_TYPE_LEFT) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.chat_item_left, parent, false)
            MessageViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.chat_item_right, parent, false)
            MessageViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return chatList.size
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val chat: Chat = chatList[position]
        val messageTime =
            chat.timeStamp.split(" ")[1] + " " + chat.timeStamp.split(" ")[2]

        holder.showMessage.text = chat.message
        holder.showMessageTimeStamp.text = messageTime
        holder.chatMessageContainer.setOnClickListener {
            Toast.makeText(context, "Feature is Currently Under Development", Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun getItemViewType(position: Int): Int {
        firebaseUser = FirebaseAuth.getInstance().currentUser!!
        return if (chatList[position].sender == firebaseUser.uid) {
            MSG_TYPE_RIGHT
        } else {
            MSG_TYPE_LEFT
        }
    }
}