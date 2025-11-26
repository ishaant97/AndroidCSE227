package com.example.androidcse227.EndTermPractical

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidcse227.R
import com.example.androidcse227.Unit1.Firebase.User
import com.example.androidcse227.Unit1.Firebase.UserAdapter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class TaskApp : AppCompatActivity() {
    private lateinit var taskTitleEditText: EditText
    private lateinit var addTaskButton: Button
    private lateinit var updateTaskButton: Button
    private lateinit var deleteTaskButton: Button
    private lateinit var database: DatabaseReference

    private lateinit var taskList: ArrayList<User>
    private lateinit var adapter: UserAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContentView(R.layout.activity_task_app)
        database = FirebaseDatabase.getInstance("https://newandroidcse227-default-rtdb.firebaseio.com").getReference("tasks")
        taskTitleEditText = findViewById(R.id.taskTitleEditText)
        addTaskButton = findViewById(R.id.addTaskButton)
        updateTaskButton = findViewById(R.id.updateTaskButton)
        deleteTaskButton = findViewById(R.id.deleteTaskButton)
        recyclerView = findViewById(R.id.recyclerView)

        taskList = ArrayList()
        adapter = UserAdapter(taskList)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        addTaskButton.setOnClickListener { addTask() }
        updateTaskButton.setOnClickListener { updateTask() }
        deleteTaskButton.setOnClickListener { deleteTask() }
        getTasks()
    }

    private fun addTask(){
        addTaskButton.setOnClickListener {
            val taskTitle = taskTitleEditText.text.toString()
            if (taskTitle.isNotEmpty()) {
                val data = Task(taskTitle)
                database.child(data.toString()).setValue(data)
                    .addOnSuccessListener {
                        taskTitleEditText.text.clear()
                        Toast.makeText(this, "TaskCreated Successfully", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        taskTitleEditText.text.clear()
                        Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_LONG).show()
                    }
            }
        }
    }

    private fun updateTask(){
        updateTaskButton.setOnClickListener {
            val taskTitle = taskTitleEditText.text.toString()
            val updates = mutableMapOf<String, Any>()
            if (taskTitle.isNotEmpty()) {
                updates["title"] = taskTitle
                database.child(taskTitle).updateChildren(updates)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Task Updated Successfully", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_LONG).show()
                    }
            }
            else{
                Toast.makeText(this, "Please enter a task title", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun deleteTask(){
        deleteTaskButton.setOnClickListener {
            val taskTitle = taskTitleEditText.text.toString()
            if (taskTitle.isNotEmpty()) {
                database.child(taskTitle).removeValue()
                    .addOnSuccessListener {
                        Toast.makeText(this, "Task Deleted Successfully", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_LONG).show()
                    }
            }
            else{
                Toast.makeText(this, "Please enter a task title", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getTasks(){
        database.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                taskList.clear()
                for (dataSnap in snapshot.children) {
                    val data = dataSnap.getValue(User::class.java)
                    if (data != null) taskList.add(data)
                }
                adapter.notifyDataSetChanged()
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }
}