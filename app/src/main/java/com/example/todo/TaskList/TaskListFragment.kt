package com.example.todo.TaskList

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.R
import com.example.todo.database.Task
import com.example.todo.TaskFragment
import java.util.*

const val time="dd/MMMM/yyyy"
const val KEY = "TaskFragment"

class TaskListFragment : Fragment() {
    var sortStep: Int = 1

    interface Callbacks {
        fun onTaskSelected(taskId: UUID)
    }

    private var callbacks: Callbacks? = null

    private lateinit var taskRecyclerView: RecyclerView
    private var adapter: TaskAdapter = TaskAdapter(emptyList())

    private val taskListViewModel: TaskListViewModel by lazy {
        ViewModelProviders.of(this).get(TaskListViewModel::class.java)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_task_list, container, false)
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_task_list, menu)
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {


            R.id.add_task -> {
                val task = Task()
                taskListViewModel.addTask(task)
                val args=Bundle()
                args.putSerializable(KEY , task.id)
                val fragment= TaskFragment()
                fragment.arguments= args
                activity?.let {
                    it.supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit()}
//                callbacks?.onTaskSelected(task.id)
                true
            }
            R.id.sort_tasks -> {

                if (sortStep == 1) {

                    taskListViewModel.dateSortTask().observe(
                        viewLifecycleOwner, Observer {
                            updateUI(it)
                            val toast = Toast(context)
                            val view = ImageView(context)
                            view.setImageResource(R.drawable.sprt_date)
                            toast.setView(view)
                            toast.show()
                        })
                    sortStep++
                } else if (sortStep == 2) {
                    taskListViewModel.titleSortTask().observe(
                        viewLifecycleOwner, Observer {
                            updateUI(it)
                            val toast = Toast(context)
                            val view = ImageView(context)
                            view.setImageResource(R.drawable.filter)
                            toast.setView(view)
                            toast.show()
                        })
                    sortStep++
                }
                  else if (sortStep==3){
                        taskListViewModel.completeSortTask().observe(
                            viewLifecycleOwner, Observer {
                                updateUI(it)
                                val toast = Toast(context)
                                val view = ImageView(context)
                                view.setImageResource(R.drawable.is_complete)
                                toast.setView(view)
                                toast.show()
                    }
                        )
                    sortStep=1
                  }

                true
            }
            else -> return super.onOptionsItemSelected(item)
        }

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
//           private val doneImageView:ImageView = itemView.findViewById(R.id.passdate)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {

            if (v == itemView){

                val args = Bundle()
                val fragment = TaskFragment()

                args.putSerializable(KEY,task.id)
                fragment.arguments = args

                activity?.let {
                    it.supportFragmentManager
                        .beginTransaction()
                        .replace(R.id.fragment_container, fragment)
                        .addToBackStack(null)
                        .commit()
                }
            }

        }

        fun bind(task: Task) {
            this.task = task
            var today:Date = Date()
            titleTextView.text = this.task.title
            descriptionTextView.text = this.task.description
//            dueDate.setText() =DateFormat.format(time,task.dueDate).toString()
            dateTextView.text = this.task.dueDate.toString()

            val date=Date()
            if (task.isComplete) {
                completeImageView.setImageResource(R.drawable.is_complete)
            }else  if (date.after(task.dueDate)){
                completeImageView.setImageResource(R.drawable.passdate)
            }else {
                completeImageView.isVisible=false
            }
            }
    }
    private inner class TaskAdapter(var tasks:List<Task>)
        :RecyclerView.Adapter<TaskHolder>(){

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
        : TaskHolder {
            val layoutInflater = LayoutInflater.from(context)
            val view = layoutInflater.inflate(R.layout.list_item_task, parent , false)
            return TaskHolder(view)

        }
        override fun getItemCount() = tasks.size
        override fun onBindViewHolder(holder: TaskHolder, position:Int) {
            val task = tasks[position]
            holder.bind(task)
        }
        }
}