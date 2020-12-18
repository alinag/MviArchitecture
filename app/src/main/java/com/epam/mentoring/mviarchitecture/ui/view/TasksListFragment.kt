package com.epam.mentoring.mviarchitecture.ui.view

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.epam.mentoring.mviarchitecture.R
import com.epam.mentoring.mviarchitecture.data.model.Task
import com.epam.mentoring.mviarchitecture.ui.MainAdapter
import com.epam.mentoring.mviarchitecture.ui.MainIntent
import com.epam.mentoring.mviarchitecture.ui.viewmodel.TasksListViewModel
import com.epam.mentoring.mviarchitecture.ui.viewstate.MainState
import com.epam.mentoring.mviarchitecture.util.ViewModelFactory
import kotlinx.android.synthetic.main.tasks_list_fragment.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class TasksListFragment : Fragment(), MainAdapter.ListItemClickListener {
    private val tasks: ArrayList<Task> = arrayListOf<Task>()
    private var adapter = MainAdapter(tasks, this)

    companion object {
        fun newInstance() =
            TasksListFragment()
    }

    private lateinit var viewModel: TasksListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.tasks_list_fragment, container, false)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupUI()
        setupViewModel()
        observeViewModel()
        setupData()
    }

    private fun setupUI() {
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.run {
            addItemDecoration(
                DividerItemDecoration(
                    recyclerView.context,
                    (recyclerView.layoutManager as LinearLayoutManager).orientation
                )
            )
        }
        recyclerView.adapter = adapter
    }

    private fun setupData() {
        lifecycleScope.launch {
            viewModel.taskIntent.send(MainIntent.FetchTasks)
        }
    }


    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(
            this,
            ViewModelFactory(
            )
        ).get(TasksListViewModel::class.java)
    }

    private fun observeViewModel() {
        lifecycleScope.launch {
            viewModel.state.collect {
                when (it) {

                    is MainState.Tasks -> {
                        tasks.clear()
                        tasks.addAll(it.tasks)
                        adapter.notifyDataSetChanged()
                    }

                    is MainState.TaskDetails -> {
                        if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
                            (requireActivity() as MainActivity).show(it.task)
                        }
                    }

                    is MainState.Error -> {
                        Toast.makeText(context, it.error, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }


//    private fun renderList(tasks: ArrayList<Task>) {
//        adapter.notifyDataSetChanged()
//    }


    override fun onListItemClick(position: Int) {
        lifecycleScope.launch {
            viewModel.taskIntent.send(MainIntent.FetchTask(position))
        }

    }

}