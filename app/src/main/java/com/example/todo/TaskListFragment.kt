package com.example.todo

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.ParsePosition
import java.util.*

class TaskListFragment : Fragment() {

    interface Callbacks {
        fun onTaskSelected(taskid: UUID)
    }

    private var callbacks: Callbacks? = null

    private lateinit var taskRecyclerView: RecyclerView
    private var adapter: TaskAdapter? = TaskAdapter(emptyList())

    private val taskListViewModel: TaskListViewModel by lazy {
        ViewModelProviders.of(this).get(TaskListViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
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
        taskRecyclerView.adapter = adapter
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        taskListViewModel.taskListLiveData.observe(
            viewLifecycleOwner,
            Observer { tasks ->
                tasks?.let {
                    updateUI(tasks)
                }
            }
        )
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    private fun updateUI(tasks: List<Task>) {
        adapter = TaskAdapter(tasks)
        taskRecyclerView.adapter = adapter
    }

    private inner class TaskHolder(view:View)
        :RecyclerView.ViewHolder(view) , View.OnClickListener {

        private lateinit var task: Task

           private val titleTextView: TextView = itemView.findViewById(R.id.task_title)
           private val descriptionTextView:TextView = itemView.findViewById(R.id.listDetailsLabel)
           private val dateTextView: TextView = itemView.findViewById(R.id.dueDate)
           private val completeImageView: ImageView = itemView.findViewById(R.id.task_complete)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {

            callbacks?.onTaskSelected(task.id)

        }

        fun bind(task: Task) {
            this.task = task
            titleTextView.text = this.task.title
            descriptionTextView.text = this.task.description
            dateTextView.text = this.task.dueDate.toString()
            completeImageView.visibility = if (task.isComplete) {
                View.VISIBLE
            }else {
                View.GONE
            }
            }

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
            holder.bind(task)
        }


        }

    companion object{
        fun newInstance(): TaskListFragment{
            return TaskListFragment()
        }
    }
}