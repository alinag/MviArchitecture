package com.epam.mentoring.mviarchitecture.ui

import com.epam.mentoring.mviarchitecture.data.model.Task

sealed class MainIntent {
    class FetchTask(val id: Int): MainIntent()
    class SaveTask(val task: Task?): MainIntent()
    object FetchTasks: MainIntent()
}