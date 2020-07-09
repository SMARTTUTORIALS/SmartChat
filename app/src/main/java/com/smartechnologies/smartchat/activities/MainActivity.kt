package com.smartechnologies.smartchat.activities

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.smartechnologies.smartchat.R
import com.smartechnologies.smartchat.adapters.TabItemAcessAdapter
import com.smartechnologies.smartchat.fragments.CallsFragment
import com.smartechnologies.smartchat.fragments.ChatsFragment
import com.smartechnologies.smartchat.fragments.GroupsFragment
import com.smartechnologies.smartchat.fragments.StatusFragment

class MainActivity : AppCompatActivity() {

    private lateinit var mainAppToolbar: Toolbar
    private lateinit var mainViewPager: ViewPager
    private lateinit var mainTabLayout: TabLayout
    private lateinit var openSysCam: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainAppToolbar = findViewById(R.id.appMainToolBar)
        setSupportActionBar(mainAppToolbar)
        supportActionBar?.title = "Smart Chat"

        openSysCam = findViewById(R.id.imgOpenSystemCamera)
        mainTabLayout = findViewById(R.id.mainTabLayout)
        mainViewPager = findViewById(R.id.viewPagerFragmentHolder)

        val viewPagerAdapter =
            TabItemAcessAdapter(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT)

        viewPagerAdapter.addFragments(ChatsFragment(), "CHATS")
        viewPagerAdapter.addFragments(GroupsFragment(), "GROUPS")
        viewPagerAdapter.addFragments(StatusFragment(), "STATUS")
        viewPagerAdapter.addFragments(CallsFragment(), "CALLS")

        mainViewPager.adapter = viewPagerAdapter
        mainTabLayout.setupWithViewPager(mainViewPager)

    }
}