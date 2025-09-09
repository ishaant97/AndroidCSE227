package com.example.androidcse227.Unit2

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.androidcse227.R
import com.google.android.material.animation.AnimationUtils

class DuckAnimation : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_duck_animation)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val duckImageView = findViewById<ImageView>(R.id.duckImageView)
        val duckFoodImageView = findViewById<ImageView>(R.id.duckFoodImageView)
        val startAnimationButton = findViewById<Button>(R.id.animationStartButton)
        duckImageView.setOnClickListener {
            duckImageView.setImageResource(R.drawable.duck_food)
            duckFoodImageView.setImageResource(R.drawable.duck)
        }
        duckFoodImageView.setOnClickListener {
            duckImageView.setImageResource(R.drawable.duck)
            duckFoodImageView.setImageResource(R.drawable.duck_food)
        }
        startAnimationButton.setOnClickListener {
            val anim1 = android.view.animation.AnimationUtils.loadAnimation(this, R.anim.duck_moving_towards_food)
            val anim2 =  android.view.animation.AnimationUtils.loadAnimation(this, R.anim.food_running_away_from_duck)
            duckImageView.startAnimation(anim1)
            duckFoodImageView.startAnimation(anim2)
        }
    }
}