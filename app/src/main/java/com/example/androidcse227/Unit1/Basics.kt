package com.example.androidcse227.Unit1

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

class Basics : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_basics)
        var intentButton = findViewById<Button>(R.id.intentButton)
        var toastButton = findViewById<Button>(R.id.toastButton)
        var lengthEditText = findViewById<EditText>(R.id.lengthEditText)
        var widthEditText = findViewById<EditText>(R.id.widthEditText)
        var calculateButton = findViewById<Button>(R.id.calculateButton)

        intentButton.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        toastButton.setOnClickListener{
            Toast.makeText(this, "This is a toast message", Toast.LENGTH_SHORT).show()
        }

        calculateButton.setOnClickListener{
            val length = lengthEditText.text.toString().toDouble()
            val width = widthEditText.text.toString().toDouble()
            val area = length * width
            Toast.makeText(this, "The area is $area", Toast.LENGTH_SHORT).show()
        }
    }
}