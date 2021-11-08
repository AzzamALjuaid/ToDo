package com.example.todo

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.Observer
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import java.util.*

private const val ARG_TASK_ID = "task_id"

class TaskFragment : Fragment() {

    private lateinit var task: Task
    private lateinit var titleField: EditText
    private lateinit var detailsField: EditText
    private lateinit var dateBTN:Button
    private lateinit var completeCheckBox: CheckBox
    private val taskListViewModel: TaskViewModel by lazy {
        ViewModelProvider(this).get(TaskViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        task = Task()
        val taskid: UUID = arguments?.getSerializable(ARG_TASK_ID) as UUID
        taskListViewModel.loadTask(taskid)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_list, container , false)
        titleField = view.findViewById(R.id.listTitle) as EditText
        dateBTN = view.findViewById(R.id.dueDate) as Button
        completeCheckBox = view.findViewById(R.id.isComplete) as CheckBox

        dateBTN.apply {
            text = task.dueDate.toString()
            isEnabled = false
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        taskListViewModel.taskLiveData.observe(
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
        titleField.addTextChangedListener(titleWatcher)

        completeCheckBox.apply {
            setOnCheckedChangeListener { _, isChecked ->
                task.isComplete = isChecked
            }
        }

    }

    private fun updateUI(){
        titleField.setText(task.title)
        detailsField.setText(task.description)
        dateBTN.text = task.dueDate.toString()
        completeCheckBox.isChecked = task.isComplete

    }

    companion object{

        fun newInstance(taskid: UUID):TaskFragment {
            val args = Bundle().apply {
                putSerializable(ARG_TASK_ID , taskid)
            }
            return TaskFragment().apply {
                arguments = args
            }
        }
    }

 }