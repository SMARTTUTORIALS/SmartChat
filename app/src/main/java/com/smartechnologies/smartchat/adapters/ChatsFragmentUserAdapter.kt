package com.smartechnologies.smartchat.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.smartechnologies.smartchat.R
import com.smartechnologies.smartchat.activities.MessageActivity
import com.smartechnologies.smartchat.models.User
import com.squareup.picasso.Picasso

class ChatsFragmentUserAdapter(
    private val context: Context,
    private val userList: ArrayList<User>
) : RecyclerView.Adapter<ChatsFragmentUserAdapter.ChatsFragmentUserViewHolder>() {

    class ChatsFragmentUserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val otherUserProfileImage: ImageView =
            view.findViewById(R.id.imgOtherUserRoundedProfileImage)
        val otherUserName: TextView = view.findViewById(R.id.txtOtherUserName)
        val lastChatMessageText: TextView = view.findViewById(R.id.txtLastChatMessage)
        val lastChatMessageTime: TextView = view.findViewById(R.id.txtLastMessageTimeStamp)
        val viewContainer: CardView = view.findViewById(R.id.singleRowLayoutContainer)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatsFragmentUserViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.chats_fragment_recycler_singlerow, parent, false)
        return ChatsFragmentUserViewHolder(view)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: ChatsFragmentUserViewHolder, position: Int) {

        val otherUser = userList[position]
        holder.otherUserName.text = otherUser.username
        holder.lastChatMessageText.text = "Last Message"
        holder.lastChatMessageTime.text = "12:30 PM"
        //Load profile Image
        Picasso.get().load(otherUser.imageUrl).error(R.drawable.default_clipart_profile)
            .into(holder.otherUserProfileImage)

        holder.viewContainer.setOnClickListener {

            //Toast.makeText(context,"Feature is Currently Under Development",Toast.LENGTH_SHORT).show()
            val intent = Intent(context, MessageActivity::class.java)
            intent.putExtra("userId", otherUser.id)
            intent.putExtra("username", otherUser.username)
            context.startActivity(intent)

        }
        holder.otherUserProfileImage.setOnClickListener {
            Toast.makeText(context, "Feature is Currently Under Development", Toast.LENGTH_SHORT)
                .show()
        }

    }
}