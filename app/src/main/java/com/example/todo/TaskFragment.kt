package com.example.todo

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import androidx.fragment.app.Fragment

class TaskFragment : Fragment() {

    private lateinit var task: Task
    private lateinit var titleField: EditText
    private lateinit var dateBTN:Button
    private lateinit var completeCheckBox: CheckBox


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        task = Task()
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

 }