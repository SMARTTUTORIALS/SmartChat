package com.smartechnologies.smartchat.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.smartechnologies.smartchat.R
import com.smartechnologies.smartchat.adapters.TabItemAcessAdapter
import com.smartechnologies.smartchat.fragments.CallsFragment
import com.smartechnologies.smartchat.fragments.ChatsFragment
import com.smartechnologies.smartchat.fragments.GroupsFragment
import com.smartechnologies.smartchat.fragments.StatusFragment
import com.smartechnologies.smartchat.models.User

class MainActivity : AppCompatActivity() {

    private lateinit var mainAppToolbar: Toolbar
    private lateinit var mainViewPager: ViewPager
    private lateinit var mainTabLayout: TabLayout
    private lateinit var openSysCam: ImageView

    private lateinit var sharedPreference: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreference =
            getSharedPreferences(getString(R.string.pref_file_name), Context.MODE_PRIVATE)

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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.itemLogOut) {

            FirebaseAuth.getInstance().signOut()

            sharedPreference.edit().putBoolean("isLoggedIn", false).apply()

            Toast.makeText(
                this@MainActivity,
                "Logged out",
                Toast.LENGTH_LONG
            ).show()

            val intent = Intent(this@MainActivity, SignInActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
            return true
        }
        return false

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_activity_chat_tab_fragment_menu, menu)
        return true
    }
}