package com.bisht.runningapp.ui.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bisht.runningapp.db.Run
import com.bisht.runningapp.other.SortType
import com.bisht.runningapp.repositories.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    val repository: MainRepository
) : ViewModel(), LifecycleObserver {

    private val runSortedByDate = repository.getAllRunsSortedByDate()
    private val runsSortedByDistance = repository.getAllRunsSortedByDistance()
    private val runsSortedByCaloriesBurned = repository.getAllRunsSortedByCaloriesBurned()
    private val runsSortedByTimeInMillis = repository.getAllRunsSortedByTimeInMillis()
    private val runsSortedByAvgSpeed = repository.getAllRunsSortedByAvgSpeed()
    val runs = MediatorLiveData<List<Run>>()
    var sortType = SortType.DATE
    init {
        runs.addSource(runSortedByDate){result ->
            if(sortType == SortType.DATE){
                result?.let {runs.value = it }
            }
        }
        runs.addSource(runsSortedByTimeInMillis){result ->
            if(sortType == SortType.RUNNING_TIME){
                result?.let { runs.value = it}
            }
        }
        runs.addSource(runsSortedByDistance){result ->
            if(sortType == SortType.DISTANCE){
                result?.let {runs.value = it}
            }
        }
        runs.addSource(runsSortedByAvgSpeed){result ->
            if(sortType == SortType.AVG_SPEED){
                result?.let {runs.value = it}
            }
        }
        runs.addSource(runsSortedByCaloriesBurned){result ->
            if(sortType == SortType.CALORIES_BURNED){
                result?.let {runs.value = it}
            }
        }
    }

    fun sort(sortType: SortType) = when(sortType){
        SortType.DATE -> runSortedByDate.value?.let { runs.value = it }
        SortType.RUNNING_TIME -> runsSortedByTimeInMillis.value?.let { runs.value = it }
        SortType.AVG_SPEED -> runsSortedByAvgSpeed.value?.let { runs.value = it }
        SortType.DISTANCE -> runsSortedByDistance.value?.let { runs.value = it }
        SortType.CALORIES_BURNED -> runsSortedByCaloriesBurned.value?.let { runs.value = it }
    }.also{
        this.sortType = sortType
    }

    fun insertRun(run: Run) = viewModelScope.launch {
        repository.insertRun(run)
    }

}
