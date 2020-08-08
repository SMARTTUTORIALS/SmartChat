package com.smartechnologies.smartchat.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.smartechnologies.smartchat.R

class SignInActivity : AppCompatActivity() {

    private lateinit var password: EditText
    private lateinit var emailid: EditText
    private lateinit var toolbar: Toolbar
    private lateinit var signInButton: Button
    private lateinit var forgotPassword: TextView
    private lateinit var createNewAcc: TextView
    private lateinit var progressBar: RelativeLayout

    private lateinit var authenticator: FirebaseAuth

    private lateinit var sharedPreference: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        //initialize the progress bar
        progressBar = findViewById(R.id.ProgressBarContainer)
        progressBar.visibility = View.VISIBLE   //show the Progress bar

        sharedPreference =
            getSharedPreferences(getString(R.string.pref_file_name), Context.MODE_PRIVATE)

        authenticator = FirebaseAuth.getInstance()

        val firebaseUser = authenticator.currentUser

        val isLoggedIn = sharedPreference.getBoolean("isLoggedIn", false)

        if (!isLoggedIn && firebaseUser == null) {

            progressBar.visibility = View.GONE   //hide the Progress bar

            signInButton = findViewById(R.id.btnSignInUser)
            password = findViewById(R.id.etUserPassword)
            emailid = findViewById(R.id.etUserEmail)

            forgotPassword = findViewById(R.id.txtForgotPassword)
            createNewAcc = findViewById(R.id.txtCreateNewAccount)

            toolbar = findViewById(R.id.SignInToolBar)
            setSupportActionBar(toolbar)
            supportActionBar?.title = "Sign In"



            signInButton.setOnClickListener {
                val txtEmail = emailid.text.toString()
                val txtPassword = password.text.toString()

                if (txtEmail.isEmpty() || txtPassword.isEmpty()) {
                    Toast.makeText(
                        this@SignInActivity,
                        "All fields are required",
                        Toast.LENGTH_LONG
                    )
                        .show()
                } else {

                    progressBar.visibility = View.VISIBLE   //show the Progress bar

                    authenticator.signInWithEmailAndPassword(txtEmail, txtPassword)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                progressBar.visibility = View.GONE   //hide the Progress bar

                                sharedPreference.edit().putBoolean("isLoggedIn", true).apply()

                                Toast.makeText(
                                    this@SignInActivity,
                                    "Welcome to SmartChat",
                                    Toast.LENGTH_SHORT
                                ).show()

                                val intent = Intent(this@SignInActivity, MainActivity::class.java)
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(intent)
                                finish()
                            } else {

                                progressBar.visibility = View.GONE   //hide the Progress bar

                                sharedPreference.edit().putBoolean("isLoggedIn", false).apply()

                                Toast.makeText(
                                    this@SignInActivity,
                                    "Authentication Failed",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }
                }
            }

            createNewAcc.setOnClickListener {
                val intent = Intent(this@SignInActivity, RegistrationActivity::class.java)
                startActivity(intent)

            }

            forgotPassword.setOnClickListener {
                Toast.makeText(this@SignInActivity, "Feature under Development!", Toast.LENGTH_LONG)
                    .show()
            }
        } else if (isLoggedIn && firebaseUser != null) {
            progressBar.visibility = View.GONE   //hide the Progress bar

            Toast.makeText(this@SignInActivity, "Welcome Back", Toast.LENGTH_SHORT).show()

            val intent = Intent(this@SignInActivity, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this@SignInActivity, "Unknown Error Occurred", Toast.LENGTH_LONG)
                .show()
            authenticator.signOut()
            sharedPreference.edit().putBoolean("isLoggedIn", false).apply()

            finishAffinity()
        }
    }
}