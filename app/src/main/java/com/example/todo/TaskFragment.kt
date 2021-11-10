package com.example.todo

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.*
import androidx.lifecycle.Observer
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_list.view.*
import java.util.UUID

private const val ARG_TASK_ID = "task_id"
private const val DIALOG_DATE = "dialogDate"

class TaskFragment : Fragment() {

    private var callbacks : TaskListFragment.Callbacks? = null
    private lateinit var task: Task
    private lateinit var titleField: EditText
    private lateinit var detailsField: EditText
    private lateinit var dueDateBTN:Button
    private lateinit var creationDateBTN:Button
    private lateinit var completeCheckBox: CheckBox
    private lateinit var addImageBTN: ImageButton

    private val taskDetailViewModel: TaskDetailViewModel by lazy {
        ViewModelProviders.of(this).get(TaskDetailViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        task = Task()
        val taskId: UUID = arguments?.getSerializable(ARG_TASK_ID) as UUID
        taskDetailViewModel.loadTask(taskId)
        setHasOptionsMenu(true)
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as TaskListFragment.Callbacks?
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
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
            DateFragment().apply {
                show(this@TaskFragment.requireFragmentManager() , DIALOG_DATE)
            }
        }


        creationDateBTN.setOnClickListener {
            DateFragment().apply {
                show(this@TaskFragment.requireFragmentManager() , DIALOG_DATE)
            }
        }

        addImageBTN.setOnClickListener {
            val fragment = TaskListFragment()
            activity?.supportFragmentManager?.beginTransaction()
                ?.replace(R.id.fragment_container, fragment)?.addToBackStack(null)?.commit()
            val toast = Toast(context)
            val view = ImageView(context)
            view.setImageResource(R.drawable.addcomplete128)
            toast.setView(view)
            toast.show()
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete_task -> {
                taskDetailViewModel.deletTask(task)
                val fragment = TaskListFragment()
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(R.id.fragment_container, fragment)?.addToBackStack(null)?.commit()
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

 }