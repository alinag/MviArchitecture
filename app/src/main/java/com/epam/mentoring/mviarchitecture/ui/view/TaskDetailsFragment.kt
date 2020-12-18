package com.epam.mentoring.mviarchitecture.ui.view

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.epam.mentoring.mviarchitecture.R
import com.epam.mentoring.mviarchitecture.data.model.Task
import com.epam.mentoring.mviarchitecture.ui.MainIntent
import com.epam.mentoring.mviarchitecture.ui.viewmodel.TaskDetailsViewModel
import com.epam.mentoring.mviarchitecture.ui.viewstate.MainState
import com.epam.mentoring.mviarchitecture.util.ViewModelFactory
import kotlinx.android.synthetic.main.task_details_fragment.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class TaskDetailsFragment : Fragment() {

    companion object {
        val TAG: String = "TASK_DETAILS"
        val KEY_TASK_ID: String = "TASK_ID"
        fun newInstance() =
            TaskDetailsFragment()
    }

    private lateinit var viewModel: TaskDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.task_details_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupViewModel()
        setupData()
        observeViewModel()
        observeTextChange()
    }

    private fun setupData() {
        lifecycleScope.launch {
            viewModel.taskIntent.send(MainIntent.FetchTask(arguments?.get(KEY_TASK_ID) as Int))
        }
        viewModel.currentTask.observe(viewLifecycleOwner, Observer<Task> {
            task_details_name.setText(it.name)
            task_details_description.setText(it.description)
        })
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(
            this,
            ViewModelFactory()
        ).get(TaskDetailsViewModel::class.java)
    }

    private fun observeTextChange() {

        task_details_name.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                lifecycleScope.launch {
                    viewModel.taskIntent.send(MainIntent.SaveTask(viewModel.currentTask.value?.copy(name = p0.toString())))
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
        task_details_description.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                lifecycleScope.launch {
                    viewModel.taskIntent.send(MainIntent.SaveTask(viewModel.currentTask.value?.copy(description = p0.toString())))
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.state.collect {
                when (it) {


                    is MainState.TaskDetails -> {
                        viewModel.currentTask.value = it.task
                        task_details_name.setText(it.task.name)
                        task_details_description.setText(it.task.description)
                    }

                    is MainState.Error -> {
                        Toast.makeText(context, it.error, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }


}

