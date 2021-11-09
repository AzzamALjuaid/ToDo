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
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_list.view.*
import java.util.UUID

private const val ARG_TASK_ID = "task_id"
private const val DIALOG_DATE = "dialogDate"

class TaskFragment : Fragment() {

    private lateinit var task: Task
    private lateinit var titleField: EditText
    private lateinit var detailsField: EditText
    private lateinit var dueDateBTN:Button
    private lateinit var creationDateBTN:Button
    private lateinit var completeCheckBox: CheckBox
    private val taskDetailViewModel: TaskDetailViewModel by lazy {
        ViewModelProviders.of(this).get(TaskDetailViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        task = Task()
        val taskId: UUID = arguments?.getSerializable(ARG_TASK_ID) as UUID
        taskDetailViewModel.loadTask(taskId)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_list, container , false)
        titleField = view.findViewById(R.id.listTitle) as EditText
        detailsField = view.findViewById(R.id.listDetailsLabel) as EditText
        dueDateBTN = view.findViewById(R.id.dueDate) as Button
        creationDateBTN = view.findViewById(R.id.creationDateBTN) as Button
        completeCheckBox = view.findViewById(R.id.isComplete) as CheckBox

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
            DateFragment().apply {
                show(this@TaskFragment.requireFragmentManager() , DIALOG_DATE)
            }
        }

        creationDateBTN.setOnClickListener {
            DateFragment().apply {
                show(this@TaskFragment.requireFragmentManager() , DIALOG_DATE)
            }
        }

    }

    override fun onStop() {
        super.onStop()
        taskDetailViewModel.saveTask(task)
    }

    private fun updateUI(){
        titleField.setText(task.title)
        detailsField.setText(task.description)
        dueDateBTN.text = task.dueDate.toString()
        creationDateBTN.text = task.creationDate.toString()

        completeCheckBox.apply {
            isChecked = task.isComplete
            jumpDrawablesToCurrentState()
        }

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

 }