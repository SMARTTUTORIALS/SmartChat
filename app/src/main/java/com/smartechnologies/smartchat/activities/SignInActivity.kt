package com.smartechnologies.smartchat.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.smartechnologies.smartchat.R

class SignInActivity : AppCompatActivity() {

    private lateinit var password: EditText
    private lateinit var emailid: EditText
    private lateinit var toolbar: Toolbar
    private lateinit var signInButton: Button

    private lateinit var authenticator: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        signInButton = findViewById(R.id.btnSignInUser)
        password = findViewById(R.id.etUserPassword)
        emailid = findViewById(R.id.etUserEmail)

        toolbar = findViewById(R.id.SignInToolBar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Sign In"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        authenticator = FirebaseAuth.getInstance()

        signInButton.setOnClickListener {
            val txtEmail = emailid.text.toString()
            val txtPassword = password.text.toString()

            if (txtEmail.isEmpty() || txtPassword.isEmpty()) {
                Toast.makeText(this@SignInActivity, "All fields are required", Toast.LENGTH_LONG)
                    .show()
            } else {
                authenticator.signInWithEmailAndPassword(txtEmail, txtPassword)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val intent = Intent(this@SignInActivity, MainActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(
                                this@SignInActivity,
                                "Authentication Failed",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

            }
        }
    }
}