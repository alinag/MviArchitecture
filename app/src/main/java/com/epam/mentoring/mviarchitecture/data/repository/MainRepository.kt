package com.epam.mentoring.mviarchitecture.data.repository

import android.util.Log
import com.epam.mentoring.mviarchitecture.data.model.Task

class MainRepository {
    companion object {
        val stubRepoList: ArrayList<Task> = arrayListOf<Task>(
            Task(0, "work", "prepare to demo"),
            Task(1, "mentoring", "complete all tasks"),
            Task(2, "life", "meet up with friends"),
            Task(3, "study", "english homework")
        )
    }


    suspend fun getTasks() = stubRepoList
    suspend fun getTask(id: Int) = stubRepoList[id]
    suspend fun updateTask(task: Task?) {
        task?.let { stubRepoList[task.id] = task }
    }

}