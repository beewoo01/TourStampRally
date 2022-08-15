package com.sdin.tourstamprally.ui.fragment.around

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.sdin.tourstamprally.R
import com.sdin.tourstamprally.databinding.FragmentAroundBinding
import com.sdin.tourstamprally.ui.fragment.BaseFragment
import com.sdin.tourstamprally.ui.fragment.MainFragmentDirections
import com.sdin.tourstamprally.ui.fragment.around.course.CourseFragment
import com.sdin.tourstamprally.ui.fragment.around.store.AroundStoreFragment

class AroundFragment : BaseFragment() {

    /**
     * TODO
     * 1. 쿠폰은 발급받은일로 부터 7일 까지 소유할수 있다.
     * 2. 일주일이 지나면 소유자는 소유권을 잃어버리며, 해당 쿠폰은 발급화면에서 보여진다.
     * 3. 쿠폰 사용기간이 발급받은일이로 부터 일주일 전에 소멸된다면 사용가능 기간은 쿠폰 사용기간이 된다.
     * */

    private var binding: FragmentAroundBinding? = null
    private var courseFragment: CourseFragment? = null
    private var aroundStoreFragment: AroundStoreFragment? = null
    private var isRestartFragment = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isRestartFragment = false

        //courseFragment = CourseFragment.newInstance()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.wtf("AroundFragment", "onCreateView")
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_around, container, false)

        binding?.fragment = this@AroundFragment



        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstancdState: Bundle?) {
        super.onViewCreated(view, savedInstancdState)
        Log.wtf("AroundFragment", "onViewCreated")
        initView()
    }

    override fun onPause() {
        super.onPause()
        courseFragment?.onPause()
        aroundStoreFragment?.onPause()
    }

    override fun onStop() {
        super.onStop()
        courseFragment?.onStop()
        aroundStoreFragment?.onStop()
        isRestartFragment = true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.wtf("AroundFragment", "onDestroyView")

        courseFragment?.onDestroyView()
        aroundStoreFragment?.onDestroyView()
        /*courseFragment?.onDestroy()
        aroundStoreFragment?.onDestroy()
        courseFragment = null
        aroundStoreFragment = null*/
        //binding?.aroundPager?.adapter = null
    }

    override fun onDetach() {
        super.onDetach()
        Log.wtf("AroundFragment", "onDetach")
        isRestartFragment = false
        courseFragment?.onDetach()
        aroundStoreFragment?.onDetach()
    }





    override fun onResume() {
        super.onResume()

        Log.wtf("AroundFragment", "onResume")
        /*if (isRestartFragment) {

            when (binding?.aroundPager?.currentItem) {
                0 -> {
                    courseFragment?.onResetFragment()
                }

                else -> {
                    aroundStoreFragment?.onResetFragment()
                }
            }
        }*/

        isRestartFragment = true


    }


    @SuppressLint("ClickableViewAccessibility")
    private fun initView() = with(binding!!) {

        courseFragment = CourseFragment()
        aroundStoreFragment = AroundStoreFragment()

        aroundPager.adapter = AroundPagerAdapter(requireActivity())
        aroundPager.isUserInputEnabled = false
        aroundPager.isSaveEnabled = false
        //isSaveEnabled = Fragment 저장 안함 MapView를 많이 사용하므로 문제 많이 발생함

        btnGetstamp.setOnClickListener {
            val action = AroundFragmentDirections.actionPageStoreToPageStamp()
            findNavController().navigate(action)
        }

        //aroundPager

        TabLayoutMediator(tabLayout, aroundPager) { tab, position ->
            tab.text =
                when (position) {
                    0 -> {
                        "코스"
                    }

                    else -> {
                        "매장"
                    }
                }

        }.attach()

        if (!checkLocationService()) {
            showDialogForLocationServiceSetting()
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }

    }

    inner class AroundPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
        override fun getItemCount(): Int = 2

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> {
                    courseFragment!!
                }

                else -> {
                    aroundStoreFragment!!
                }

            }
        }

    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { map ->
            if (map.all { it.value }) {
                permissionCheck()
            }
        }

    private fun permissionCheck() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )

            return
        }

    }


    private fun showDialogForLocationServiceSetting() {
        AlertDialog.Builder(requireContext()).apply {
            setTitle("위치 서비스 비활성화")
            setMessage("앱을 사용하기 위해 위치 서비스가 필요합니다.")
            setCancelable(true)
            setPositiveButton(
                "설정"
            ) { _, _ ->


                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                locationRequest.launch(intent)
            }
        }.create().show()
    }

    private val locationRequest: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { activityResult ->

    }

    private fun checkLocationService(): Boolean {
        val locationManager: LocationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    override fun onDestroy() {
        courseFragment?.onDestroy()
        aroundStoreFragment?.onDestroy()
        super.onDestroy()
        Log.wtf("AroundFragment", "onDestroy")
        binding = null
    }

}