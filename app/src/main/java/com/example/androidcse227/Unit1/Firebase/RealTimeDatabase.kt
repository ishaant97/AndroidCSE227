package com.example.androidcse227.Unit1.Firebase

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidcse227.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.jvm.java

class RealTimeDatabase : AppCompatActivity() {

    private lateinit var nameInput: EditText
    private lateinit var ageInput: EditText
    private lateinit var saveBtn: Button
    private lateinit var recyclerView: RecyclerView

    private lateinit var database: DatabaseReference
    private lateinit var userList: ArrayList<User>
    private lateinit var adapter: UserAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_real_time_database)



        nameInput = findViewById(R.id.nameInput)
        ageInput = findViewById(R.id.ageInput)
        saveBtn = findViewById(R.id.saveBtn)
        recyclerView = findViewById(R.id.recyclerView)

        database = FirebaseDatabase.getInstance("https://newandroidcse227-default-rtdb.firebaseio.com").getReference("Users")
        Log.d("DB_URL", database.toString())


        userList = ArrayList()
        adapter = UserAdapter(userList)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        saveBtn.setOnClickListener {
            Toast.makeText(this, "Button clicked", Toast.LENGTH_SHORT).show()
            saveUser()
        }

        getUsers()
    }

    private fun saveUser() {
        val name = nameInput.text.toString()
        val age = ageInput.text.toString()

        if (name.isNotEmpty() && age.isNotEmpty()) {

            val userId = database.push().key!!
            val user = User(name, age.toInt())

            database.child(userId).setValue(user)
                .addOnSuccessListener {
                    Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_LONG).show()
                    it.printStackTrace()
                }

        }
    }

    private fun getUsers() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()

                for (dataSnap in snapshot.children) {
                    val user = dataSnap.getValue(User::class.java)
                    if (user != null) userList.add(user)
                }

                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }
}