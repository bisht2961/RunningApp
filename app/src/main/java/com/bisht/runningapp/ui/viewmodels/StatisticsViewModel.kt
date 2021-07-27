package com.bisht.runningapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.bisht.runningapp.repositories.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    val repository: MainRepository
) : ViewModel(){

    val totalTimeRun = repository.getTotalTimeInMillis()
    val totalDistance = repository.getTotalDistance()
    val totalCaloriesBurned = repository.getTotalCaloriesBurned()
    val totalAverageSpeed = repository.getTotalAvgSpeed()
    val runsSortedByDate  = repository.getAllRunsSortedByDate()

}