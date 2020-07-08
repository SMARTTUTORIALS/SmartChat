package com.smartechnologies.smartchat.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.google.android.material.tabs.TabItem
import com.smartechnologies.smartchat.R

class MainActivity : AppCompatActivity() {

    private lateinit var mainAppToolbar : Toolbar
    private lateinit var chatsTabItem : TabItem
    private lateinit var statusTabItem: TabItem
    private lateinit var callsTabItem: TabItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainAppToolbar = findViewById(R.id.appMainToolBar)
        setSupportActionBar(mainAppToolbar)
        supportActionBar?.title="Smart Chat"

        chatsTabItem = findViewById(R.id.tabItemChats)
        statusTabItem = findViewById(R.id.tabItemStatus)
        callsTabItem =findViewById(R.id.tabItemCalls)


    }
}