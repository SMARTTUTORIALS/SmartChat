package com.smartechnologies.smartchat.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.smartechnologies.smartchat.R
import com.smartechnologies.smartchat.adapters.ChatsFragmentUserAdapter
import com.smartechnologies.smartchat.models.User

class ChatsFragment : Fragment() {

    private lateinit var recyclerViewChats: RecyclerView
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var recyclerAdapter: ChatsFragmentUserAdapter

    private lateinit var userList: ArrayList<User>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_chats, container, false)

        recyclerViewChats = view.findViewById(R.id.chatsFragmentRecyclerView)
        layoutManager = LinearLayoutManager(activity)
        recyclerViewChats.layoutManager = layoutManager
        recyclerViewChats.addItemDecoration(
            DividerItemDecoration(
                recyclerViewChats.context,
                layoutManager.orientation
            )
        )

        userList = ArrayList<User>() // Initialise an empty ArrayList

        getFirebaseUsers()
        // println("executed ${userList.size}")
        recyclerAdapter = ChatsFragmentUserAdapter(activity as Context, userList)
        recyclerViewChats.adapter = recyclerAdapter

        return view
    }

    private fun getFirebaseUsers() {

        //println("Inside Function")
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val dbReference = FirebaseDatabase.getInstance().getReference("Users")


        dbReference.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(activity, "Unable to reach server", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                if (snapshot.exists()) {

                    for (data in snapshot.children) {
                        //creating single user data class model object for ease of usage
                        val userModel = User(
                            (data.child("Username").value.toString()),
                            (data.child("Id").value.toString()),
                            (data.child("ImageURL").value.toString()),
                            (data.child("Contact").value.toString()),
                            (data.child("EmailID").value.toString())
                        )

                        if (userModel.id != firebaseUser!!.uid) {
                            userList.add(userModel)
                        }
                    }
                    recyclerAdapter.notifyDataSetChanged()
                    //println("executed ${userList.size}")
                }

            }
        })

        //println("returning from function")
    }

}