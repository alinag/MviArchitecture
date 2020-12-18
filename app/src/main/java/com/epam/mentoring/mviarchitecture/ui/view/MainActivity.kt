package com.epam.mentoring.mviarchitecture.ui.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.epam.mentoring.mviarchitecture.R
import com.epam.mentoring.mviarchitecture.data.model.Task
import com.epam.mentoring.mviarchitecture.ui.view.TaskDetailsFragment.Companion.KEY_TASK_ID
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            val fragment =
                TasksListFragment()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.main_content, fragment)
                .commit()
        }
    }


    fun show(task: Task) {
        val fragment =
            TaskDetailsFragment()
        val args = Bundle().apply {
            putInt(KEY_TASK_ID, task.id)
        }
        fragment.arguments = args
        supportFragmentManager.beginTransaction()
            .addToBackStack(TaskDetailsFragment.TAG)
            .replace(R.id.main_content, fragment, TaskDetailsFragment.TAG)
            .commit()
    }
}