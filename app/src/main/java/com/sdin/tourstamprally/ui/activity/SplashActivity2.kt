package com.sdin.tourstamprally.ui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.*
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.sdin.tourstamprally.R
import com.sdin.tourstamprally.Utils
import com.sdin.tourstamprally.data.Constant
import com.sdin.tourstamprally.databinding.ActivitySplashBinding
import com.sdin.tourstamprally.model.UserModel
import com.sdin.tourstamprally.ui.dialog.DefaultBSRDialog
import com.sdin.tourstamprally.ui.dialog.DefaultDialog
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@SuppressLint("CustomSplashScreen")
class SplashActivity2 : BaseActivity() {
    private var binding: ActivitySplashBinding? = null
    private lateinit var fadeOutAnimation: Animation
    private var isErr = false
    private lateinit var handler: Handler
    private lateinit var defaultPopUpBSRDialog: DefaultDialog
    private var CHECKNUM = 0

    companion object {
        //private const var CHECKNUM = 0
        private const val MY_PERMISSIONS_REQUEST_CONTACTS = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@SplashActivity2, R.layout.activity_splash)
        binding?.let {
            Glide.with(it.background.context).load(R.drawable.splash_bgclean).into(object : CustomTarget<Drawable>(){
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?
                ) {
                    it.background.background = resource
                }

                override fun onLoadCleared(placeholder: Drawable?) {

                }

            })
        }


        initView()

    }

    override fun initView() {
        fadeOutAnimation = AnimationUtils.loadAnimation(this@SplashActivity2, R.anim.logo_fade_out)
        fadeOutAnimation.setAnimationListener(fadeOutAnimationListener)
        val fadeInAnimation =
            AnimationUtils.loadAnimation(this@SplashActivity2, R.anim.logo_fade_in)
        fadeInAnimation.setAnimationListener(fadeInAnimationListener)
        binding?.logo?.startAnimation(fadeInAnimation)

        handler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(msg: Message) {
                super.handleMessage(msg)

                if (!isErr) {
                    val preferences = setSharedPref()
                    val phone = preferences.getString("phone", "")
                    val psw = preferences.getString("password", "")


                    if (!phone.isNullOrEmpty() && !psw.isNullOrEmpty()) {

                        login(phone, psw = psw)

                    } else {
                        startActivity(
                            Intent(
                                this@SplashActivity2,
                                LoginActivity::class.java
                            ).apply {
                                putExtra("phone", phone)
                                flags =
                                    Intent.FLAG_ACTIVITY_CLEAR_TASK and Intent.FLAG_ACTIVITY_NEW_TASK
                            })

                        finish()
                    }
                } else {
                    startLoading()
                }
            }
        }
    }

    private fun login(phone: String, psw: String) {
        apiService.userLogin(phone, psw).enqueue(object : Callback<UserModel> {
            override fun onResponse(call: Call<UserModel>, response: Response<UserModel>) {
                try {
                    if (response.isSuccessful) {
                        val result = response.body()

                        if (result == null || result.equals("null")) {
                            moveActivity(LoginActivity::class.java, "로그인에 실패하였습니다.")
                        } else {
                            if (result.enable.equals("0")) {
                                Utils.UserPhone = phone
                                Utils.UserPassword = psw
                                Utils.User_Idx = result.userIdx
                                Utils.User_Name = result.name
                                Utils.User_Email = result.email
                                Utils.User_Location = result.location
                                Utils.User_Profile = result.user_profile

                                moveActivity(MainActivity2::class.java, "로그인에 성공하였습니다.")
                            } else {
                                moveActivity(LoginActivity::class.java, "로그인에 실패하였습니다.")
                            }
                        }


                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    moveActivity(LoginActivity::class.java, "로그인에 실패하였습니다.")
                }


            }

            override fun onFailure(call: Call<UserModel>, t: Throwable) {
                t.printStackTrace()
                moveActivity(LoginActivity::class.java, "로그인에 실패하였습니다.")
            }

        })
    }

    private fun moveActivity(activity: Class<*>, msg: String?) {
        startActivity(Intent(this@SplashActivity2, activity).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            overridePendingTransition(R.anim.activity_fade_out, R.anim.activity_fade_in)
        })

        msg?.let {
            showToast(msg)
        }

        finish()

    }


    private fun setSharedPref() = getSharedPreferences("rebuild_preference", MODE_PRIVATE)

    @SuppressLint("InflateParams", "CutPasteId")
    private fun startLoading() {
        Handler(Looper.getMainLooper()).postDelayed({
            if (!isErr) {
                if (permission(CHECKNUM)) {
                    CHECKNUM++

                    val msg = handler.obtainMessage()
                    msg.data = Bundle().apply {
                        putString("fire", "fire")
                    }
                    isErr = false
                    handler.sendMessage(msg)

                } else {

                    isErr = true

                    val permissionView =
                        layoutInflater.inflate(R.layout.view_permission_meta, null, false)

                    when (CHECKNUM) {
                        0 -> {
                            val permissionGpsErrText =
                                permissionView.findViewById<TextView>(R.id.tv_test)

                            permissionGpsErrText.text = "어플 사용시 필요한 권한을 허용해 주세요."

                            defaultPopUpBSRDialog = DefaultDialog(
                                this@SplashActivity2,
                                permissionErrPopUpCloseButtonListener,
                                "어플 사용시 필요한 권한을 허용해 주세요."
                            )
                        }

                        1 -> {

                            val gpsErrText =
                                permissionView.findViewById<TextView>(R.id.tv_test)

                            gpsErrText.text = "GPS 기능이 꺼져 있습니다. GPS 기능을 켜주세요."

                            defaultPopUpBSRDialog =
                                DefaultDialog(
                                    mContext,
                                    gpsErrPopUpCloseButtonListener,
                                    "GPS 기능이 꺼져 있습니다. GPS 기능을 켜주세요."
                                );
                            defaultPopUpBSRDialog.show();
                        }

                        2 -> {
                            val networkErrText =
                                permissionView.findViewById<TextView>(R.id.tv_test);

                            networkErrText.text = "네트워크 오류가 발생 하였습니다."

                            defaultPopUpBSRDialog = DefaultDialog(
                                mContext,
                                networkErrPopUpCloseButtonListener,
                                "네트워크 오류가 발생 하였습니다."
                            )
                            defaultPopUpBSRDialog.show()
                        }

                    }
                }
            }
        }, 500)
    }

    private val permissionErrPopUpCloseButtonListener = View.OnClickListener {
        defaultPopUpBSRDialog.dismiss();
        ActivityCompat.requestPermissions(
            this@SplashActivity2,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ), MY_PERMISSIONS_REQUEST_CONTACTS
        )
    }

    private val gpsErrPopUpCloseButtonListener = View.OnClickListener {
        defaultPopUpBSRDialog.dismiss()
        CHECKNUM = 0
        isErr = false
        startLoading()
    }

    private val networkErrPopUpCloseButtonListener = View.OnClickListener {
        defaultPopUpBSRDialog.dismiss()
        CHECKNUM = 0
        isErr = false
        startLoading()
    }

    private fun versionCheck(): Array<String> {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        } else {
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE


            )
        }
    }

    private fun permission(checkNum: Int): Boolean =
        when (checkNum) {
            0 -> {
                requestPermissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                )


                Constant.ACCESS_CAMERA == PackageManager.PERMISSION_GRANTED
                        && Constant.ACCESS_LOCATION == PackageManager.PERMISSION_GRANTED
                        && Constant.ACCESS_STOREGE == PackageManager.PERMISSION_GRANTED

            }

            1 -> {
                Utils.getGPSState(this@SplashActivity2)
            }

            2 -> {
                Utils.getNetworkStatus(this@SplashActivity2) < 3
            }

            else -> {
                Log.wtf("permission", "else")
                false
            }
        }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { map ->
            if (map.all { it.value == true }) {
                val preferences = setSharedPref()
                val phone = preferences.getString("phone", "")
                val psw = preferences.getString("password", "")

                if (!phone.isNullOrEmpty() && !psw.isNullOrEmpty()) {
                    Utils.UserPhone = phone
                    Utils.UserPassword = psw
                    login(phone, psw)
                } else {
                    moveActivity(LoginActivity::class.java, null)
                }
            } else {
                AlertDialog.Builder(this@SplashActivity2).apply {
                    title = "앱 권한"
                    setMessage("해당 앱의 원할한 기능을 이용하시려면 애플리케이션 정보> 권한> 에서 모든 권한을 허용해 주십시오")
                    setPositiveButton(
                        "권한설정"
                    ) { dialog, _ ->
                        startActivity(
                            Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).setData(
                                Uri.parse("package:" + applicationContext.packageName)
                            )
                        )
                        dialog.cancel()
                    }
                }.show()
            }
        }


    private val fadeOutAnimationListener = object : Animation.AnimationListener {
        override fun onAnimationStart(animation: Animation?) = Unit

        override fun onAnimationEnd(animation: Animation?) {
            binding?.logo?.visibility = View.GONE
            startLoading()
        }

        override fun onAnimationRepeat(animation: Animation?) = Unit

    }

    private val fadeInAnimationListener = object : Animation.AnimationListener {
        override fun onAnimationStart(animation: Animation?) = Unit

        override fun onAnimationEnd(animation: Animation?) {
            binding?.logo?.startAnimation(fadeOutAnimation)
        }

        override fun onAnimationRepeat(animation: Animation?) = Unit

    }
}