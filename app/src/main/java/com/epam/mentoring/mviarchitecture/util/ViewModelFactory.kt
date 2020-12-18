package com.epam.mentoring.mviarchitecture.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.epam.mentoring.mviarchitecture.data.repository.MainRepository
import com.epam.mentoring.mviarchitecture.ui.viewmodel.TaskDetailsViewModel
import com.epam.mentoring.mviarchitecture.ui.viewmodel.TasksListViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

class ViewModelFactory() : ViewModelProvider.Factory {

    @ExperimentalCoroutinesApi
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TasksListViewModel::class.java)) {
            return TasksListViewModel(MainRepository()) as T
        }
        if (modelClass.isAssignableFrom(TaskDetailsViewModel::class.java)) {
            return TaskDetailsViewModel(MainRepository()) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}