package com.example.androidcse227.Unit2

import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.androidcse227.R

class Animation : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_animation)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val imageView = findViewById<ImageView>(R.id.imageView)
        val fadeAnimationButton = findViewById<Button>(R.id.fadeAnimationButton)
        val moveAnimationButton = findViewById<Button>(R.id.moveAnimationButton)

        fadeAnimationButton.setOnClickListener {
            val anim = AnimationUtils.loadAnimation(this, R.anim.fade_in)
            imageView.startAnimation(anim)
        }
        moveAnimationButton.setOnClickListener {
            val anim = AnimationUtils.loadAnimation(this, R.anim.move)
            imageView.startAnimation(anim)
        }
    }
}