package com.example.androidcse227.Unit3

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidcse227.R
import kotlin.jvm.java


class RetroActivity : AppCompatActivity() {
    private lateinit var viewModel: PostViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_retro)
//
//        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
//        recyclerView.layoutManager = LinearLayoutManager(this)
//
//        viewModel = ViewModelProvider(this).get(PostViewModel::class.java)
//
//        FirebaseMessaging.getInstance().token
//            .addOnSuccessListener { Log.d("TOKEN", it) }
//
//        viewModel.posts.observe(this) {
//            recyclerView.adapter = PostAdapter(it)
//        }
    }
}