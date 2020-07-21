package com.smartechnologies.smartchat.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
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

    private lateinit var authenticator: FirebaseAuth
    private lateinit var dbReference: DatabaseReference

    private val passwordRegex: Regex =
        Regex("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#\$%!\\-_?&])(?=\\S+\$).{6,}")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        username = findViewById(R.id.etUserName)
        password = findViewById(R.id.etUserPassword)
        emailid = findViewById(R.id.etUserEmail)
        phonenumber = findViewById(R.id.etUserPhone)
        btnCreateUser = findViewById(R.id.btnCreateUser)

        toolbar = findViewById(R.id.registrationToolBar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Create User"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        authenticator = FirebaseAuth.getInstance()

        btnCreateUser.setOnClickListener {
            val txtUsername = username.text.toString()
            val txtUserEmail = emailid.text.toString()
            val userPhoneNumber = phonenumber.text.toString()
            val txtUserPassword = password.text.toString()

            if (txtUsername.isEmpty() || txtUserEmail.isEmpty() || userPhoneNumber.isEmpty() || txtUserPassword.isEmpty()) {
                Toast.makeText(
                    this@RegistrationActivity,
                    "All Fields are Required",
                    Toast.LENGTH_LONG
                ).show()
            } else if (!txtUserPassword.matches(passwordRegex)) {

                Toast.makeText(
                    this@RegistrationActivity,
                    "Please Enter a Valid Password",
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
        authenticator.createUserWithEmailAndPassword(username, password).addOnCompleteListener {
            if (it.isSuccessful) {
                val firebaseUser = authenticator.currentUser
                val userId: String = firebaseUser!!.uid
                dbReference = FirebaseDatabase.getInstance().getReference("Users").child(userId)

                val hashMap = HashMap<String, String>()
                hashMap["Id"] = userId
                hashMap["Username"] = username
                hashMap["ImageURL"] = "default"
                hashMap["Contact"] = userContact

                dbReference.setValue(hashMap).addOnCompleteListener { task: Task<Void> ->
                    if (task.isSuccessful) {
                        val intent = Intent(this@RegistrationActivity, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        finish()
                    }
                }


            } else {
                Toast.makeText(
                    this@RegistrationActivity,
                    "Registration Failed with provided Email and Password",
                    Toast.LENGTH_LONG
                ).show()
            }

        }
    }
}