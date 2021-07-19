package com.bisht.runningapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bisht.runningapp.R
import com.bisht.runningapp.db.Run
import com.bisht.runningapp.other.TrackingUtility
import com.bumptech.glide.Glide
import com.google.android.material.textview.MaterialTextView
import java.text.SimpleDateFormat
import java.util.*

class RunAdapter(): RecyclerView.Adapter<RunAdapter.RunViewHolder>(){

    inner class RunViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val ivImage = itemView.findViewById<ImageView>(R.id.ivRunImage)
        val tvDate = itemView.findViewById<MaterialTextView>(R.id.tvDate)
        val tvTime = itemView.findViewById<MaterialTextView>(R.id.tvTime)
        val tvDistance = itemView.findViewById<MaterialTextView>(R.id.tvDistance)
        val tvAvgSpeed = itemView.findViewById<MaterialTextView>(R.id.tvAvgSpeed)
        val tvCalories = itemView.findViewById<MaterialTextView>(R.id.tvCalories)
    }

    val diffCallback = object: DiffUtil.ItemCallback<Run>(){
        override fun areItemsTheSame(oldItem: Run, newItem: Run): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: Run, newItem: Run): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    val differ = AsyncListDiffer(this,diffCallback)
    fun submitList(list:List<Run>) = differ.submitList(list)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RunViewHolder {
        val viewHolder =RunViewHolder(LayoutInflater.from(parent.context).inflate(
            R.layout.item_run,parent,false
        ))

        return viewHolder
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: RunViewHolder, position: Int) {
        val run = differ.currentList[position]
        Glide.with(holder.ivImage.context).load(run.img).into(holder.ivImage)
        val calendar = Calendar.getInstance().apply {
            timeInMillis = run.timeStamp
        }
        val dateFormat = SimpleDateFormat("dd.MM.yy",Locale.getDefault())
        holder.tvDate.text = dateFormat.format(calendar.time)
        val avgSpeed = "${run.avgSpeedInKMH}km/hr"
        holder.tvAvgSpeed.text = avgSpeed
        val distance = "${run.distanceInMeter/100f}km"
        holder.tvDistance.text = distance
        holder.tvTime.text = TrackingUtility.getFormattedStopWatchTime(run.timeInMillis)
        val caloriesBurned = "${run.caloriesBurned}kcal"
        holder.tvCalories.text = caloriesBurned

    }

}