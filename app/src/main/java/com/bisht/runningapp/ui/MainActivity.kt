package com.bisht.runningapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bisht.runningapp.R
import com.bisht.runningapp.db.RunDAO
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var dao:RunDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }
}