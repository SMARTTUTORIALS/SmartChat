package com.smartechnologies.smartchat.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.smartechnologies.smartchat.R

class RegistrationActivity : AppCompatActivity() {

    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var emailid: EditText
    private lateinit var phonenumber: EditText
    private lateinit var btnCreateUser: Button
    private lateinit var toolbar: Toolbar
    private lateinit var progressBar: RelativeLayout

    private lateinit var authenticator: FirebaseAuth
    private lateinit var dbReference: DatabaseReference

    private lateinit var sharedPreference: SharedPreferences

    private val passwordRegex: Regex =
        Regex("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#\$%!\\-_?&])(?=\\S+\$).{6,}")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        //Initialize preference file and get instance
        sharedPreference =
            getSharedPreferences(getString(R.string.pref_file_name), Context.MODE_PRIVATE)

        //initialize the progress bar
        progressBar = findViewById(R.id.ProgressBarContainer)
        progressBar.visibility = View.GONE   //Hide the Progress bar


        // initialize the other view elements
        username = findViewById(R.id.etUserName)
        password = findViewById(R.id.etUserPassword)
        emailid = findViewById(R.id.etUserEmail)
        phonenumber = findViewById(R.id.etUserPhone)
        btnCreateUser = findViewById(R.id.btnCreateUser)

        //set up tool bar and action bar properties
        toolbar = findViewById(R.id.registrationToolBar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Create User"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //make the hamburger icon work. adding a Navigation on click listener
        toolbar.setNavigationOnClickListener {
            finish()
        }

        //get Firebase authentication instance
        authenticator = FirebaseAuth.getInstance()

        //register button click listener
        btnCreateUser.setOnClickListener {

            //read inputs from view in layout
            val txtUsername = username.text.toString()
            val txtUserEmail = emailid.text.toString()
            val userPhoneNumber = phonenumber.text.toString()
            val txtUserPassword = password.text.toString()

            //check for valid inputs
            if (txtUsername.isEmpty() || txtUserEmail.isEmpty() || userPhoneNumber.isEmpty() || txtUserPassword.isEmpty()) {
                Toast.makeText(
                    this@RegistrationActivity,
                    "All Fields are Required",
                    Toast.LENGTH_LONG
                ).show()
            } else if (!txtUserPassword.matches(passwordRegex)) {
                //Matching password Regex criteria
                Toast.makeText(
                    this@RegistrationActivity, getString(R.string.passwordCriteria),
                    Toast.LENGTH_LONG
                ).show()
            } else {
                registerNewUser(txtUsername, txtUserEmail, txtUserPassword, userPhoneNumber)
            }
        }
    }

    private fun registerNewUser(
        username: String,
        email: String,
        password: String,
        userContact: String
    ) {

        progressBar.visibility = View.VISIBLE   //Show the Progress bar

        //attempt to create new user with Email and password
        authenticator.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            // do the following when task completed
            if (it.isSuccessful) {
                //check if user creation successful then perform following task
                val firebaseUser = authenticator.currentUser    //get current user reference
                val userId: String = firebaseUser!!.uid     //get current user UID

                //initialize firebase Realtime DB instance. create new child with reference Users
                dbReference = FirebaseDatabase.getInstance().getReference("Users").child(userId)

                //Create Hash map for no SQL db Data entry (Attribute , Value)
                val hashMap = HashMap<String, String>()
                hashMap["Id"] = userId
                hashMap["Username"] = username
                hashMap["ImageURL"] = "default"
                hashMap["EmailID"] = email
                hashMap["Contact"] = userContact

                //push values of hash map to DB
                dbReference.setValue(hashMap).addOnCompleteListener { task: Task<Void> ->
                    if (task.isSuccessful) {
                        //check if task was successful
                        progressBar.visibility = View.GONE   //Hide the Progress bar

                        //make Entry in preferences file for Auto-login
                        sharedPreference.edit().putBoolean("isLoggedIn", true).apply()


                        //create intent and launch main activity
                        val intent = Intent(this@RegistrationActivity, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        finish()
                    }
                }


            } else {

                //do the following if task fails user creation
                progressBar.visibility = View.GONE   //Hide the Progress bar

                Toast.makeText(
                    this@RegistrationActivity,
                    "Registration Failed with provided Email and Password",
                    Toast.LENGTH_LONG
                ).show()
            }

        }
    }

}