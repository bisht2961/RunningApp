package com.bisht.runningapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.bisht.runningapp.repositories.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
    class MainViewModels @Inject constructor(
    val repository: MainRepository
) : ViewModel(){

}