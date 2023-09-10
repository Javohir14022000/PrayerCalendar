package com.mobiler.namozcalendar.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.google.android.gms.location.FusedLocationProviderClient

import com.mobiler.namozcalendar.R
import com.mobiler.namozcalendar.database.AppDatabase
import com.mobiler.namozcalendar.databinding.FragmentSplashBinding
import com.mobiler.namozcalendar.ui.adapter.ViewPagerAdapter
import com.mobiler.namozcalendar.utils.SharedPreference


class SplashFragment : Fragment(R.layout.fragment_splash) {

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding
    lateinit var appDatabase: AppDatabase
    lateinit var viewPagerAdapter: ViewPagerAdapter
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
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        val root: View = binding!!.root
        val location = sharedPreference.getLocation()

        if (location?.first == null) {
            findNavController().navigate(R.id.homeFragment)
        }
        setUpViewPager()

        binding?.apply {
            bottomNavigation.onItemSelected = {
                when (it) {
                    0 -> {
                        viewPager.currentItem = 0
                    }

                    1 -> {
                        viewPager.currentItem = 1
                    }

                    2 -> {
                        viewPager.currentItem = 2
                    }
                }
            }
        }


        return root

    }

    private fun setUpViewPager() {
        viewPagerAdapter = ViewPagerAdapter(childFragmentManager)
        binding?.apply {
            viewPager.adapter = viewPagerAdapter
            viewPager.addOnPageChangeListener(object : OnPageChangeListener {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {

                }

                override fun onPageSelected(position: Int) {
                    when (position) {
                        0 -> {
                            bottomNavigation.itemActiveIndex = 0
                        }

                        1 -> {
                            bottomNavigation.itemActiveIndex = 1
                        }

                        2 -> {
                            bottomNavigation.itemActiveIndex = 2
                        }
                    }
                }

                override fun onPageScrollStateChanged(state: Int) {

                }

            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /*    private fun checkGps() {
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

        private fun checkPermission() {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {


            } else {

                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_PERMISSION_REQUEST_CODE
                )

            }
        }

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
                        Toast.makeText(requireContext(), "$latitude : $longitude", Toast.LENGTH_SHORT)
                            .show()
                        appDatabase.prayerLicDao()
                            .addPrayerLoc(PrayerLocEntity(0, latitude.toString(), location.toString()))
                    }
                }
                .addOnFailureListener { e ->
                    Log.d("TAG", "Xatolik ")
                    e.printStackTrace()
                }
        }

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
                    // Foydalanuvchi ruxsat bermadi, joylashuvni olishni bekor qiling
                }
            }
        }*/

}