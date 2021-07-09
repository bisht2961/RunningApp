package com.bisht.runningapp.repositories

import com.bisht.runningapp.db.Run
import com.bisht.runningapp.db.RunDAO
import javax.inject.Inject

class MainRepository @Inject constructor(
 val runDao:RunDAO
){
    suspend fun insertRun(run: Run) = runDao.insert(run)

    suspend fun deleteRun(run: Run) = runDao.delete(run)

    fun getAllRunsSortedByDate() = runDao.getAllRunsSortedByDate()

    fun getAllRunsSortedByDistance() = runDao.getAllRunsSortedByDistanceInMeter()

    fun getAllRunsSortedByTimeInMillis() = runDao.getAllRunsSortedByTime()

    fun getAllRunsSortedByCaloriesBurned() = runDao.getTotalCaloriesBurned()

    fun getAllRunsSortedByAvgSpeed() = runDao.getAllRunsSortedByAvgSpeed()

    fun getTotalAvgSpeed() = runDao.getTotalAverageSpeed()

    fun getTotalCaloriesBurned() = runDao.getTotalCaloriesBurned()

    fun getTotalDistance() = runDao.getTotalDistance()

    fun getTotalTimeInMillis() = runDao.getTotalTimeInMillis()

}
