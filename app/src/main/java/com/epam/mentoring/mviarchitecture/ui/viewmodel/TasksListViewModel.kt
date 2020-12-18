package com.epam.mentoring.mviarchitecture.ui.viewmodel


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.epam.mentoring.mviarchitecture.data.repository.MainRepository
import com.epam.mentoring.mviarchitecture.ui.MainIntent
import com.epam.mentoring.mviarchitecture.ui.viewstate.MainState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class TasksListViewModel(
    private val repository: MainRepository
) : ViewModel() {
    val taskIntent = Channel<MainIntent>(Channel.UNLIMITED)
    private val _state = MutableStateFlow<MainState>(MainState.Idle)
    val state: StateFlow<MainState>
        get() = _state

    init {
        handleIntent()
    }

    private fun handleIntent() {
        viewModelScope.launch {
            taskIntent.consumeAsFlow().collect {
                when (it) {
                    is MainIntent.FetchTask -> fetchTask(it.id)
                    is MainIntent.FetchTasks -> fetchTasks()
                }
            }
        }
    }


    private fun fetchTask(id: Int) {
        viewModelScope.launch {
            _state.value = MainState.Loading
            _state.value = try {
                MainState.TaskDetails(repository.getTask(id))
            } catch (e: Exception) {
                MainState.Error(e.localizedMessage)
            }
        }
    }

    private fun fetchTasks() {
        viewModelScope.launch {
            _state.value = MainState.Loading
            _state.value = try {
                MainState.Tasks(repository.getTasks())
            } catch (e: Exception) {
                MainState.Error(e.localizedMessage)
            }
        }
    }
}