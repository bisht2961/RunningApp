package com.bisht.runningapp.other

import android.content.Context
import android.view.LayoutInflater
import com.bisht.runningapp.databinding.MarkerViewBinding
import com.bisht.runningapp.db.Run
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import java.text.SimpleDateFormat
import java.util.*

class CustomMarkerView (val runs: List<Run>,c:Context,layoutId: Int): MarkerView(c,layoutId){

    private var binding: MarkerViewBinding
    init {
        val inflator:LayoutInflater = c.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = MarkerViewBinding.inflate(inflator)
    }

    override fun getOffset(): MPPointF {
        return MPPointF(-width/2f,-height.toFloat())
    }
    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        super.refreshContent(e, highlight)
        if(e == null ){
            return
        }
        val curRunId = e.x.toInt()
        val run = runs[curRunId]
        val calendar = Calendar.getInstance().apply {
            timeInMillis = run.timeStamp
        }
        val dateFormat = SimpleDateFormat("dd.MM.yy",Locale.getDefault())
        binding.tvDate.text = dateFormat.format(calendar.time)
        val avgSpeed = "${run.avgSpeedInKMH}km/hr"
        binding.tvAvgSpeed.text = avgSpeed
        val distance = "${run.distanceInMeter/100f}km"
        binding.tvDistance.text = distance
        binding.tvDuration.text = TrackingUtility.getFormattedStopWatchTime(run.timeInMillis)
        val caloriesBurned = "${run.caloriesBurned}kcal"
        binding.tvCaloriesBurned.text = caloriesBurned

    }

}