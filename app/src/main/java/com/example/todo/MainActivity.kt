package com.example.todo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.todo.TaskList.TaskListFragment
import java.util.*


private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() , TaskListFragment.Callbacks {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val currentFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container)

        if (currentFragment == null) {
            val fragment = TaskListFragment()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container , fragment)
                .commit()
        }
    }

    override fun onTaskSelected(taskid: UUID) {
        val fragment = TaskFragment.newInstance(taskid)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container , fragment)
            .addToBackStack(null)
            .commit()
    }
}