package com.mobiler.namozcalendar.ui

import android.Manifest
import android.content.IntentSender
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.mobiler.namozcalendar.R
import com.mobiler.namozcalendar.database.AppDatabase
import com.mobiler.namozcalendar.databinding.FragmentHomeBinding
import com.mobiler.namozcalendar.utils.SharedPreference

class HomeFragment : Fragment(R.layout.fragment_home) {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding
    lateinit var appDatabase: AppDatabase
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val LOCATION_PERMISSION_REQUEST_CODE = 100
    lateinit var sharedPreference: SharedPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appDatabase = AppDatabase.getInstance(requireContext())
        sharedPreference = SharedPreference(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding!!.root
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        binding?.apply {
            btnNext.setOnClickListener {
               checkGps()
            }

            btnPlace.setOnClickListener {
                Toast.makeText(requireContext(), "Xozircha ushbu qism qo`shilamadi!!!", Toast.LENGTH_SHORT).show()

            }
        }


        return root
    }

    //permission
    private fun checkPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )

        } else {
            checkGps()

        }
    }

    //gpsDialog
    private fun checkGps() {
        val create = LocationRequest.create()
        val builder = LocationSettingsRequest.Builder().addLocationRequest(create)
        val task = LocationServices.getSettingsClient(requireContext())
            .checkLocationSettings(builder.build())

        task.addOnSuccessListener {
            val locationSettingsStates = it.locationSettingsStates
            if (locationSettingsStates!!.isLocationPresent) {
                getLastLocation()
            }
        }.addOnFailureListener {
            val statusCode = (it as ResolvableApiException).statusCode
            if (statusCode == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                try {
                    it.startResolutionForResult(requireActivity(), 100)
                } catch (e: IntentSender.SendIntentException) {
                    Log.d("TAG Exception", "checkGps: ${e.message} ")
                }
            }
        }
    }

    //getLocation
    private fun getLastLocation() {
        Log.d("TAG", "getLastLocation: ")
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("TAG", "getLastLocation: return ")
            return
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    Log.d("TAG", "getLastLocation: long lat ")
                    val latitude = location.latitude
                    val longitude = location.longitude
                    sharedPreference.saveLocation(latitude, longitude)
                    findNavController().popBackStack()

                }
            }
            .addOnFailureListener { e ->
                Log.d("TAG", "Xatolik ")
                e.printStackTrace()
            }
    }

    @Deprecated("Deprecated in Java")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
               checkGps()
            } else {

                Toast.makeText(requireContext(), "Joylashuvni olish uchun ruxsat bermadingiz!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        checkPermission()
    }
}