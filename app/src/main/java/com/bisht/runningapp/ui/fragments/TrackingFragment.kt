package com.bisht.runningapp.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bisht.runningapp.R
import com.bisht.runningapp.databinding.FragmentSetupBinding
import com.bisht.runningapp.databinding.FragmentTrackingBinding
import com.bisht.runningapp.other.Constants.ACTION_PAUSE_SERVICE
import com.bisht.runningapp.other.Constants.ACTION_START_OR_RESUME_SERVICE
import com.bisht.runningapp.other.Constants.MAP_ZOOM
import com.bisht.runningapp.other.Constants.POLYLINE_COLOR
import com.bisht.runningapp.other.Constants.POLYLINE_WIDTH
import com.bisht.runningapp.other.TrackingUtility
import com.bisht.runningapp.services.PolyLine
import com.bisht.runningapp.services.TrackingService
import com.bisht.runningapp.ui.viewmodels.MainViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.PolylineOptions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TrackingFragment : Fragment(R.layout.fragment_tracking){

    private var _binding: FragmentTrackingBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by viewModels()
    private var isTracking = false
    private var pathPoints = mutableListOf<PolyLine>()
    private var curTimeMillis = 0L
    private var map: GoogleMap? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTrackingBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mapView.onCreate(savedInstanceState)
        binding.btnToggleRun.setOnClickListener{
            toggleRun()
        }
        binding.mapView.getMapAsync{
            map = it
            addAllPolyLine()
        }
        subscribeToObserver()
        
    }

    private fun subscribeToObserver(){
        TrackingService.isTracking.observe(viewLifecycleOwner, Observer {
            updateTracking(it)
        })
        TrackingService.pathPoints.observe(viewLifecycleOwner, Observer {
            pathPoints = it
            addLatestPolyLine()
            moveCameraToUser()
        })
        TrackingService.timeRunInMillis.observe(viewLifecycleOwner, Observer {
            curTimeMillis = it
            val formattedTime = TrackingUtility.getFormattedStopWatchTime(curTimeMillis,true)
            binding.tvTimer.text = formattedTime
        })
    }

    private fun toggleRun(){
        if(isTracking){
            sendCommandService(ACTION_PAUSE_SERVICE)
        }else{
            sendCommandService(ACTION_START_OR_RESUME_SERVICE)
        }
    }



    private fun updateTracking(isTracking: Boolean ){
        this.isTracking = isTracking
        if(!isTracking){
            binding.btnToggleRun.text = "Start"
            binding.btnFinishRun.visibility = View.VISIBLE
        }else{
            binding.btnToggleRun.text = "Stop"
            binding.btnFinishRun.visibility = View.GONE
        }
    }

    private fun moveCameraToUser(){
        if(pathPoints.isNotEmpty() && pathPoints.last().isNotEmpty() ){
            map?.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    pathPoints.last().last(),
                    MAP_ZOOM
                )
            )
        }
    }


    private fun addAllPolyLine(){
        for (polyline in pathPoints){
            val polylineOptions = PolylineOptions()
                .color(POLYLINE_COLOR)
                .width(POLYLINE_WIDTH)
                .addAll(polyline)
            map?.addPolyline(polylineOptions)
        }
    }
    private fun addLatestPolyLine(){
        if(pathPoints.isNotEmpty() && pathPoints.last().size > 1 ){
            val preLastLatLng = pathPoints.last()[pathPoints.last().size -2 ]
            val lastLatLng = pathPoints.last().last()
            val polylinOptions = PolylineOptions()
                .color(POLYLINE_COLOR)
                .width(POLYLINE_WIDTH)
                .add(preLastLatLng)
                .add(lastLatLng)
            map?.addPolyline(polylinOptions)
        }

    }

    private fun sendCommandService(action: String) = Intent(requireContext(), TrackingService::class.java).also {
        it.action = action
        requireContext().startService(it)
    }
    override fun onResume() {
        super.onResume()
        binding.mapView?.onResume()
    }

    override fun onStart() {
        super.onStart()
        binding.mapView?.onStart()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView?.onStop()
    }

    override fun onPause() {
        super.onPause()
        binding.mapView?.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView?.onLowMemory()
    }

}