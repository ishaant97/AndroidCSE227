package com.example.androidcse227.Unit1.Firebase

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.androidcse227.MainActivity
import com.example.androidcse227.R
import com.google.firebase.auth.FirebaseAuth

class AuthActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContentView(R.layout.activity_auth)

        auth = FirebaseAuth.getInstance()

        var emailEditText = findViewById<EditText>(R.id.emailEditText)
        var passwordEditText = findViewById<EditText>(R.id.passwordEditText)

        var loginBtn = findViewById<Button>(R.id.loginBtn)
        var registerBtn = findViewById<Button>(R.id.registerBtn)


        registerBtn.setOnClickListener{
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if(email.isEmpty() || password.isEmpty()){
                Toast.makeText(this,"Fill the details properly",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener{task ->
                    if(task.isSuccessful){
                        Toast.makeText(this, "Account Created", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
                    }
                }
        }

        loginBtn.setOnClickListener{
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if(email.isEmpty() || password.isEmpty()){
                Toast.makeText(this,"Fill the details properly",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Logged In", Toast.LENGTH_SHORT).show()

                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
                    }
                }

        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if(currentUser != null){
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}