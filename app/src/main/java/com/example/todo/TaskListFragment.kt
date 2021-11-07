package com.example.todo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.ParsePosition

private const val TAG = "TaskListFragment"

class TaskListFragment : Fragment() {

    private lateinit var taskRecyclerView: RecyclerView
    private var adapter: TaskAdapter? = null

    private val taskListViewModel: TaskListViewModel by lazy {
        ViewModelProviders.of(this).get(TaskListViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG , "Total tasks: ${taskListViewModel.tasks.size}")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_task_list , container, false)
        taskRecyclerView =
            view.findViewById(R.id.task_recycler_view) as RecyclerView
        taskRecyclerView.layoutManager = LinearLayoutManager(context)
        updateUI()
        return view
    }

    private fun updateUI() {
        val tasks = taskListViewModel.tasks
        adapter = TaskAdapter(tasks)
        taskRecyclerView.adapter = adapter
    }

    private inner class TaskHolder(view:View)
        :RecyclerView.ViewHolder(view){
            val titleTextView: TextView = itemView.findViewById(R.id.task_title)
            val descriptionTextView:TextView = itemView.findViewById(R.id.listDetailsLabel)
            val dateTextView: TextView = itemView.findViewById(R.id.dueDate)
        }

    private inner class TaskAdapter(var tasks:List<Task>)
        :RecyclerView.Adapter<TaskHolder>(){

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
        : TaskHolder {
            val view = layoutInflater.inflate(R.layout.list_item_task , parent , false)
            return TaskHolder(view)

        }

        override fun getItemCount() = tasks.size

        override fun onBindViewHolder(holder: TaskHolder, position:Int) {
            val task = tasks[position]
            holder.apply {
                titleTextView.text = task.title
                descriptionTextView.text = task.description
                dateTextView.text = task.dueDate.toString()
            }
        }



        }

    companion object{
        fun newInstance(): TaskListFragment{
            return TaskListFragment()
        }
    }
}