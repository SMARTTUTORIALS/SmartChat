package com.smartechnologies.smartchat.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.smartechnologies.smartchat.R
import com.smartechnologies.smartchat.adapters.MessageAdapter
import com.smartechnologies.smartchat.models.Chat
import com.smartechnologies.smartchat.models.User
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class MessageActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var profileImage: ImageView
    private lateinit var toolbarTitle: TextView
    private lateinit var userLastActiveStatus: TextView
    private lateinit var customHomeButton: RelativeLayout
    private lateinit var messageInput: EditText
    private lateinit var messageOrVoiceButton: ImageView
    private lateinit var addImageButton: ImageView

    private lateinit var userModel: User
    private lateinit var chatList: ArrayList<Chat>
    private lateinit var chatsRecycler: RecyclerView
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var linearLayoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)

        val uid = intent.getStringExtra("userId").toString()
        val otherUserName = intent.getStringExtra("username").toString()

        val currentUser = FirebaseAuth.getInstance().currentUser

        toolbar = findViewById(R.id.messageToolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        customHomeButton = findViewById(R.id.profileImageHolder)
        profileImage = findViewById(R.id.imgToolbarUserProfileImage)
        toolbarTitle = findViewById(R.id.txtToolbarUserName)
        userLastActiveStatus = findViewById(R.id.txtLastSeenTimeStamp)
        messageInput = findViewById(R.id.etMessageInput)
        messageOrVoiceButton = findViewById(R.id.imgAddVoiceNoteOrSendMessage)
        addImageButton = findViewById(R.id.imgAddImages)

        chatsRecycler = findViewById(R.id.chatsRecycler)
        chatsRecycler.setHasFixedSize(true)
        linearLayoutManager = LinearLayoutManager(applicationContext)
        linearLayoutManager.stackFromEnd = true
        chatsRecycler.layoutManager = linearLayoutManager

        chatList = ArrayList()

        messageAdapter = MessageAdapter(this@MessageActivity, chatList)
        chatsRecycler.adapter = messageAdapter

        toolbarTitle.text = otherUserName

        val databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(uid)
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                finish()
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {

                    //creating single user data class model object for ease of usage
                    userModel = User(
                        (snapshot.child("Username").value.toString()),
                        (snapshot.child("Id").value.toString()),
                        (snapshot.child("ImageURL").value.toString()),
                        (snapshot.child("Contact").value.toString()),
                        (snapshot.child("EmailID").value.toString())
                    )
                    Picasso.get().load(userModel.imageUrl).error(R.drawable.default_clipart_profile)
                        .into(profileImage)

                    readMessage(currentUser!!.uid, userModel.id)
                }
            }
        })


        //click listener for custom back button
        customHomeButton.setOnClickListener { finish() }

        messageInput.doOnTextChanged { _, _, _, count ->
            if (count > 0) {
                messageOrVoiceButton.setImageResource(R.drawable.ic_send)
            } else {
                messageOrVoiceButton.setImageResource(R.drawable.ic_mic)
            }
        }

        messageOrVoiceButton.setOnClickListener {
            val messageText = messageInput.text.toString()

            if (messageText.isNotEmpty()) {
                sendMessage(currentUser!!.uid, userModel.id, messageText)
                messageInput.text.clear()
            }
        }
    }

    private fun sendMessage(senderId: String, receiverId: String, message: String) {

        val reference = FirebaseDatabase.getInstance().getReference("Chats")
        val timeStamp = SimpleDateFormat("dd-MMM-yy hh:mm aa")

        val messageData = HashMap<String, Any>()
        messageData["senderId"] = senderId
        messageData["receiverId"] = receiverId
        messageData["message"] = message
        messageData["timeStamp"] = timeStamp.format(Date()).toString()

        reference.push().setValue(messageData)

    }

    private fun readMessage(myId: String, userId: String) {


        val reference = FirebaseDatabase.getInstance().getReference("Chats")
        reference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MessageActivity, "Unable to reach server", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                chatList.clear()
                if (snapshot.exists()) {
                    for (data in snapshot.children) {
                        val chat = Chat(
                            (data.child("senderId").value.toString()),
                            (data.child("receiverId").value.toString()),
                            (data.child("message").value.toString()),
                            (data.child("timeStamp").value.toString())
                        )

                        if ((chat.receiver == userId && chat.sender == myId) ||
                            (chat.receiver == myId && chat.sender == userId)
                        ) {
                            chatList.add(chat)
                        }
                    }
                    messageAdapter.notifyDataSetChanged()
                    chatsRecycler.smoothScrollToPosition(chatList.size - 1)
                }
            }
        })
    }
}