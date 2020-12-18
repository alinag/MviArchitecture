package com.epam.mentoring.mviarchitecture.ui.viewstate

import com.epam.mentoring.mviarchitecture.data.model.Task


sealed class MainState {

    object Idle : MainState()
    object Loading : MainState()
    data class Tasks(val tasks: ArrayList<Task>) : MainState()
    data class TaskDetails(val task: Task) : MainState()
    data class EditedTask(val task: Task) : MainState()
    data class Error(val error: String?) : MainState()

}