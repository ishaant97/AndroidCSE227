package com.example.androidcse227.Unit1.Firebase

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.androidcse227.MainActivity
import com.example.androidcse227.R
import com.google.firebase.auth.FirebaseAuth

// THESE ARE THE IMPORTS YOU WERE MISSING
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse

class FireBaseUIActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fire_base_uiactivity)

        auth = FirebaseAuth.getInstance()

        if (auth.currentUser != null) {
            goToHome()
        } else {
            launchFirebaseUI()
        }
    }

    private fun launchFirebaseUI() {
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build()
        )

        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setLogo(R.drawable.ic_launcher_foreground)
            .build()

        // Note: startActivityForResult is deprecated, but it will still work for now.
        startActivityForResult(signInIntent, 1001)
    }

    private fun goToHome() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1001) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
                val user = FirebaseAuth.getInstance().currentUser
                Toast.makeText(this, "Welcome ${user?.email}", Toast.LENGTH_SHORT).show()
                goToHome()
            } else {
                // Sign in failed
                if (response == null) {
                    Toast.makeText(this, "Sign in cancelled", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Error: ${response.error?.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}