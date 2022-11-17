package com.example.todo

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.*
import androidx.lifecycle.Observer
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.example.todo.TaskList.KEY
import com.example.todo.TaskList.TaskListFragment
import com.example.todo.TaskList.TaskListViewModel
import com.example.todo.TaskList.time
import com.example.todo.database.Task
import kotlinx.android.synthetic.main.fragment_list.view.*
import java.util.*

private const val ARG_TASK_ID = "task_id"
private const val DIALOG_DATE = "dialogDate"
private const val REQUEST_DATE = 0

class TaskFragment : Fragment() , DateFragment.callbacks {

    private var callbacks : TaskListFragment.Callbacks? = null
    private lateinit var task: Task
    private lateinit var titleField: EditText
    private lateinit var detailsField: EditText
    private lateinit var dueDateBTN:Button
    private lateinit var creationDateBTN:TextView
    private lateinit var completeCheckBox: CheckBox
    private lateinit var addImageBTN: ImageButton

    private val taskDetailViewModel: TaskDetailViewModel by lazy {
        ViewModelProviders.of(this).get(TaskDetailViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        task = Task()
        val taskId= arguments?.getSerializable(KEY) as UUID
        taskDetailViewModel.loadTask(taskId)
        setHasOptionsMenu(true)
    }
//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        callbacks = context as TaskListFragment.Callbacks?
//    }
//
//    override fun onDetach() {
//        super.onDetach()
//        callbacks = null
//    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_list, container , false)
        titleField = view.findViewById(R.id.listTitle) as EditText
        detailsField = view.findViewById(R.id.listDetailsLabel) as EditText
        dueDateBTN = view.findViewById(R.id.dueDate) as Button
        creationDateBTN = view.findViewById(R.id.creationDateBTN) as TextView
        creationDateBTN.apply {
            isEnabled=false }
        completeCheckBox = view.findViewById(R.id.isComplete) as CheckBox
        addImageBTN = view.findViewById(R.id.AddImageButton) as ImageButton

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        taskDetailViewModel.taskLiveData.observe(
            viewLifecycleOwner,
            Observer { task ->
                task?.let {
                    this.task = task
                    updateUI()
                }
            })
    }

    override fun onStart() {
        super.onStart()

        val titleWatcher = object : TextWatcher {

            override fun beforeTextChanged(
                sequence: CharSequence?,
                start:Int,
                count:Int,
                after:Int
            ) {

            }

            override fun onTextChanged(
                sequence: CharSequence?,
                start:Int,
                before:Int,
                count:Int
            ) {
                task.title = sequence.toString()

            }

            override fun afterTextChanged(sequence: Editable?) {

            }

        }

        val detailsWatcher = object : TextWatcher {

            override fun beforeTextChanged(
                sequence: CharSequence?,
                start:Int,
                count:Int,
                after:Int
            ) {

            }

            override fun onTextChanged(
                sequence: CharSequence?,
                start:Int,
                before:Int,
                count:Int
            ) {
                task.description = sequence.toString()

            }

            override fun afterTextChanged(sequence: Editable?) {

            }

        }



        titleField.addTextChangedListener(titleWatcher)
        detailsField.addTextChangedListener(detailsWatcher)

        completeCheckBox.apply {
            setOnCheckedChangeListener { _, isChecked ->
                task.isComplete = isChecked
            }
        }


        dueDateBTN.setOnClickListener {
            DateFragment.newInstance(task.dueDate).apply {
                setTargetFragment(this@TaskFragment , REQUEST_DATE)
                show(this@TaskFragment.requireFragmentManager() , DIALOG_DATE)
            }
        }


        creationDateBTN.setOnClickListener {
            DateFragment.newInstance(task.creationDate).apply {
                setTargetFragment(this@TaskFragment , REQUEST_DATE)
                show(this@TaskFragment.requireFragmentManager() , DIALOG_DATE)
            }
        }

        addImageBTN.setOnClickListener {

//            taskDetailViewModel.addTask(task)
            if (task.title.isEmpty()) {
                taskDetailViewModel.deletTask(task)
//                taskDetailViewModel.saveTask(task)
            }else{
                taskDetailViewModel.saveTask(task)}

            val fragment = TaskListFragment()
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.fragment_container, fragment)?.addToBackStack(null)?.commit()

            if (task.title.isEmpty()){

            }else {
                val toast = Toast(context)
                val view = ImageView(context)
                view.setImageResource(R.drawable.completed_task)
                toast.setView(view)
                toast.show()
            }
        }

    }

    private fun updateUI(){
        titleField.setText(task.title)
        detailsField.setText(task.description)
        dueDateBTN.text = android.text.format.DateFormat.format(time , task.dueDate)
        creationDateBTN.text = task.creationDate.toString()
        creationDateBTN.setText("Creation Date: \n${android.text.format.DateFormat.format(time , task.creationDate)}")

        completeCheckBox.apply {
            isChecked = task.isComplete
            jumpDrawablesToCurrentState()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete_task -> {
                taskDetailViewModel.deletTask(task)
                val fragment = TaskListFragment()
                activity?.let { it.supportFragmentManager
                    .popBackStack()}

//                activity?.supportFragmentManager?.beginTransaction()
//                    ?.replace(R.id.fragment_container, fragment)?.addToBackStack(null)?.commit()
                val toast = Toast(context)
                val view = ImageView(context)
                view.setImageResource(R.drawable.delete128)
                toast.setView(view)
                toast.show()
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_list , menu)
    }

    companion object{

        fun newInstance(taskId: UUID):TaskFragment {
            val args = Bundle().apply {
                putSerializable(ARG_TASK_ID , taskId)
            }
            return TaskFragment().apply {
                arguments = args
            }
        }
    }

    override fun onDateSelected(date: Date) {
        task.dueDate = date
        updateUI()
    }

    override fun onStop() {
        if (task.title.isEmpty()){
            taskDetailViewModel.deletTask(task)
        }else{
            taskDetailViewModel.saveTask(task)
    }
        super.onStop()
}
}