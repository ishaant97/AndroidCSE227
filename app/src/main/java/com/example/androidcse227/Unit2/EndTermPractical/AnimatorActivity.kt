package com.example.androidcse227.Unit2.EndTermPractical

import android.animation.ValueAnimator
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.androidcse227.R

class AnimatorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_animator)


        // value animator
        val textView = findViewById<TextView>(R.id.textView)
        val startAnimBtn = findViewById<Button>(R.id.startAnimBtn)

        startAnimBtn.setOnClickListener {

            // ValueAnimator: change text size from 14sp to 40sp
            val animator = ValueAnimator.ofFloat(14f, 40f)
            animator.duration = 1000

            animator.addUpdateListener { animation ->
                val animatedValue = animation.animatedValue as Float
                textView.textSize = animatedValue
            }
            animator.start()
        }

    }
}