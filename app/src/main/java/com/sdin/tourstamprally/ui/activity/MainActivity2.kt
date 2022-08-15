package com.sdin.tourstamprally.ui.activity

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.sdin.tourstamprally.R
import com.sdin.tourstamprally.Utils
import com.sdin.tourstamprally.data.PreferenceUtil
import com.sdin.tourstamprally.databinding.ActivityMain2Binding
import com.sdin.tourstamprally.ui.dialog.DefaultBSRDialog
import com.sdin.tourstamprally.ui.dialog.DialogFailTimeOver
import com.sdin.tourstamprally.ui.dialog.HomeBottomSheetDialog
import com.sdin.tourstamprally.ui.fragment.auth.NFCListener
import com.sdin.tourstamprally.utill.Quadruple
import com.sdin.tourstamprally.utill.navutil.DialogNavigator
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.observers.DisposableSingleObserver
import io.reactivex.rxjava3.schedulers.Schedulers

class MainActivity2 : BaseActivity()/*, NavigationBarView.OnItemSelectedListener,
    ItemOnClick*/ {

    private lateinit var binding: ActivityMain2Binding
    private var currentnavController: LiveData<NavController>? = null
    private var nfcAdapter: NfcAdapter? = null
    private var ISNfcInable = false
    private var pendingIntent: PendingIntent? = null
    private lateinit var writingTagFilters: Array<IntentFilter>
    private var nfcListener: NFCListener? = null
    private lateinit var navController: NavController

    private lateinit var appBarConfiguration: AppBarConfiguration

    private var currentPhotoPath: String? = null

    private lateinit var prefs : PreferenceUtil
    private var currentBottomMenu: Int = R.id.page_home
    private val _immKeyboard: InputMethodManager
        get() {
            return getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        }

    private val isNotFirstLogin : Boolean by lazy {
        intent.getBooleanExtra("isNotFirstLogin", true)
    }

    private val toolbarItem: Map<Int, Quadruple<Int, Int, Int, Int>> =
        hashMapOf(

            //1번 Normal
            1 to Quadruple(
                background = R.color.white,
                title_color = R.color.black,
                backBtn_ic = R.drawable.back_ic_resize,
                tab_ic = R.drawable.ic_menu_black
            ),

            //2번 Scan
            2 to Quadruple(
                background = R.color.popup_buttonColor,
                title_color = R.color.white,
                backBtn_ic = R.drawable.ic_backspace_white_24,
                tab_ic = R.drawable.ic_menu_white
            ),

            //3번 Menu
            3 to Quadruple(
                background = R.color.mainColor,
                title_color = R.color.white,
                backBtn_ic = R.drawable.ic_backspace_white_24,
                tab_ic = R.drawable.ic_menu_white
            ),

            4 to Quadruple(
                background = R.color.comment_mainColor,
                title_color = R.color.white,
                backBtn_ic = R.drawable.back_ic_resize,
                tab_ic = R.drawable.ic_menu_black
            )
        )



    override fun onCreate(savedInstanceState: Bundle?) {
        prefs = PreferenceUtil(applicationContext)
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@MainActivity2, R.layout.activity_main2)
        binding.activity = this@MainActivity2
        binding.toolbarLayout.activity = this
        binding.drawaLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)



        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.account_admin,
                R.id.notice,
                R.id.coupon_list,
                R.id.notify_setting,
                R.id.bascet_list
            ),
            binding.drawaLayout
        )
        /*setStatusBarTransparent()
        binding.innerContainer.setPadding(0, statusBarHeight(), 0, navigationHeight())*/

        //다시보지않기
        if(prefs.getString(Utils.User_Idx.toString(), "n") == "never"){
            if (!isNotFirstLogin) {
                getData()
            }
        }
        else{
            HomeBottomSheetDialog {
                if (it == 1) {
                    if (!isNotFirstLogin) {
                        getData()
                    }
                }
            }.show(supportFragmentManager, "")
        }

        initView()
        setNFC()
        //binding.bottomNavigationView.menu.findItem(R.id.page_home).setChecked(true)

    }

    /*//actionbar 제거
    fun Activity.setStatusBarTransparent() {

        window.apply {
            setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }
        *//*if(Build.VERSION.SDK_INT >= 30) {	// API 30 에 적용
            WindowCompat.setDecorFitsSystemWindows(window, false)
        }*//*
    }
    fun Context.statusBarHeight(): Int {
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")

        return if (resourceId > 0) resources.getDimensionPixelSize(resourceId)
        else 0
    }

    fun Context.navigationHeight(): Int {
        val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
        return if (resourceId > 0) resources.getDimensionPixelSize(resourceId)
        else 0
    }
    fun Activity.setStatusBarOrigin() {
        window.apply {
            clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        }
        if(Build.VERSION.SDK_INT >= 30) {	// API 30 에 적용
            WindowCompat.setDecorFitsSystemWindows(window, true)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        setStatusBarOrigin()
    }*/


    override fun onBackPressed() {
        super.onBackPressed()
        if (navController.currentDestination?.label == "QR") {
            navController.popBackStack()
        }
        updateBottomMenu()
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (_immKeyboard.isAcceptingText) {
            hideKeyboard()
        }

        return super.dispatchTouchEvent(ev)
    }

    private fun hideKeyboard() {
        var view = currentFocus
        if (view == null) {
            view = View(this@MainActivity2)
        }

        _immKeyboard.hideSoftInputFromWindow(view.windowToken, 0)

    }

    private fun updateBottomMenu() {
        //binding.bottomNavigationView.menu.findItem(currentBottomMenu).isChecked = true
    }

    fun backBtnClick() {
        Log.wtf("main2", "backbtn")
        if (navController.currentDestination?.label == "NFC") {
            navController.navigateUp()
        }
//        navController.navigate(R.layout.fragment_main)
        navController.popBackStack()
    }

    fun drawerItemClick(position: Int) {
        var navigate = 0
        var title = ""
        when (position) {
            0 -> {
                navigate = R.id.account_admin
                title = "계정관리"
                //navController.navigate(R.id.account_admin)
            }

            1 -> {
                navigate = R.id.notice
                title = "공지사항"
                //navController.navigate(R.id.notice)
            }

            2 -> {
                navigate = R.id.coupon_list
                title = "쿠폰현황"
                //navController.navigate(R.id.ready_dialog)
                //navController.navigate(R.id.page_store)
            }

            3 -> {
                navigate = R.id.notify_setting
                title = "알림설정"
                //navController.navigate(R.id.notify_setting)
            }

            4 -> {
                navigate = R.id.bascet_list
                title = "찜한목록"
                //navController.navigate(R.id.bascet_list)
            }
        }

        navController.navigate(navigate, Bundle().apply {
            putInt("state", 3)
            //putBoolean("menu", true)
            putString("title", title)
        })

        binding.drawaLayout.closeDrawer(GravityCompat.END)

    }

    fun openDrawer() {
        binding.drawaLayout.openDrawer(GravityCompat.END)
        Glide.with(this).load("http://coratest.kr/imagefile/bsr/" + Utils.User_Profile)
            .error(ContextCompat.getDrawable(this, R.drawable.sample_profile_image)).circleCrop()
            .into(binding.navigationLayout.profileIcon)

        binding.navigationLayout.userNameTxv.text = Utils.User_Name
        //isDrawerOpen = true
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun setNFC() {
        nfcAdapter = NfcAdapter.getDefaultAdapter(this@MainActivity2)
        nfcAdapter?.let {
            ISNfcInable = true
            readFromIntent(intent)
            pendingIntent = PendingIntent.getActivity(
                this@MainActivity2, 0,
                Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), PendingIntent.FLAG_IMMUTABLE
            )
            val tagDetected = IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED)
            writingTagFilters = arrayOf(tagDetected)

        }
    }

    fun logout() {
        val pref = getSharedPreferences("rebuild_preference", MODE_PRIVATE)
        val editor = pref.edit()
        val phones = pref.getStringSet("phones", null)
        if (phones != null) {
            val list = phones.toMutableList()
            list.remove(Utils.UserPhone)
            editor.putStringSet("phones", list.toMutableSet())
        }
        editor.remove("phone")
        editor.remove("password")

        editor.apply()

        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    override fun onResume() {
        super.onResume()
        if (TextUtils.isEmpty(Utils.UserPhone) || TextUtils.isEmpty(Utils.UserPassword)) {
            startActivity(Intent(this@MainActivity2, LoginActivity::class.java))
            finish()
        }
        nfcModeOn()
    }

    override fun onPause() {
        super.onPause()
        nfcModeOff()
    }

    private fun nfcModeOn() {
        if (nfcAdapter == null) {
            nfcAdapter = NfcAdapter.getDefaultAdapter(this@MainActivity2)
        } else {
            nfcAdapter!!.enableForegroundDispatch(
                this@MainActivity2,
                pendingIntent,
                writingTagFilters,
                null
            )
        }
    }


    private fun nfcModeOff() {
        nfcAdapter?.disableForegroundDispatch(this@MainActivity2)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let {
            setIntent(it)
            readFromIntent(it)
        }
    }

    private fun readFromIntent(intent: Intent) {
        val action = intent.action
        if (NfcAdapter.ACTION_TAG_DISCOVERED == action
            || NfcAdapter.ACTION_TECH_DISCOVERED == action
            || NfcAdapter.ACTION_NDEF_DISCOVERED == action
        ) {

            val rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
            (rawMsgs?.get(0) as NdefMessage).apply {
                nfcListener?.onReadTag(String(records[0].payload))
            }
            /*rawMsgs?.size?.let {
                val msgs: Array<NdefMessage?> = arrayOfNulls(it)
                for ((index, item) in rawMsgs.withIndex()) {
                    msgs[index] = item as NdefMessage
                }

                nfcListener?.onReadTag(msgs)
            }*/


        }
    }

    fun setOnListener(nfcListener: NFCListener) {
        this.nfcListener = nfcListener
    }

    override fun initView() {

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        navController = navHostFragment.navController

        navController.addOnDestinationChangedListener(navListener)

        supportFragmentManager.addOnBackStackChangedListener {
            val backStackEntryCount = supportFragmentManager.backStackEntryCount
            val fragments = supportFragmentManager.fragments
            val fragmentCount = fragments.size
        }


        binding.navigationview.setupWithNavController(navController)
        binding.bottomNavigationView.setupWithCustomNavController(navController)
        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            if (destination.label == "empty") {
                navController.popBackStack()
                openDrawer()
            }

            Log.wtf("navController", "destination.id? " + destination.id)
            Log.wtf("navController", "destination.label? " + destination.label)
            Log.wtf("navController", "destination.navigatorName? " + destination.navigatorName)
        }
        /* binding.bottomNavigationView.setOnItemSelectedListener { item ->

             when(item.itemId) {
                 R.id.drawlay -> {
                     Log.wtf("bottomNavigationView","drawlay")
                     openDrawer()
                 }
                 else -> {
                     Log.wtf("bottomNavigationView","else")
                     binding.bottomNavigationView.setupWithCustomNavController(navController)
                 }
             }

             true
         }*/

    }

    private fun getData() {
        apiService.getCurrentCoState(Utils.User_Idx)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<Int>() {
                override fun onSuccess(integer: Int) {

                    DefaultBSRDialog(
                        context = this@MainActivity2,
                        title = "현재 진행중인 코스가 있습니다.\n계속 진행 하시겠습니까?",
                        content = "취소 시 진행중인 코스는 모두\n인증취소 처리가 됩니다.",
                        isSpecial =  false,
                        isSwitchBtn = true,
                        leftBtnStr = "취소",
                        rightBtnStr = "계속"
                    ){ result ->
                        if (result) {
                            removeMyCourse()
                            DialogFailTimeOver(this@MainActivity2).show()
                        }

                    }.show()
                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                }
            })
    }

    private fun removeMyCourse() {
        apiService.removeMyCourse(Utils.User_Idx)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<Int>() {
                override fun onSuccess(result : Int) {
                    when (result) {
                        0 -> {
                            Log.wtf("removeMyCourse", "정상적으로 삭제됨")
                            //정상적으로 삭제됨
                        }
                        1 -> {
                            Log.wtf("removeMyCourse", "기록 삭제 안됨")
                            //기록 삭제 안됨
                        }
                        2 -> {
                            Log.wtf("removeMyCourse", "코스 삭제 안됨")
                            //코스 삭제 안됨
                        }
                    }
                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                }
            })
    }

    private fun BottomNavigationView.setupWithCustomNavController(navController: NavController) {
        DialogNavigator.setupWithNavController(
            bottomNavigationView = this,
            navController = navController
        )
    }


    private fun testMenuId(id: Int) {
        when (id) {
            R.id.page_home -> {
                currentBottomMenu = R.id.page_home
            }

            R.id.page_report -> {
                currentBottomMenu = R.id.page_report
            }

            R.id.page_stamp -> {
                currentBottomMenu = R.id.page_stamp
            }

        }
    }

    private val navListener =
        NavController.OnDestinationChangedListener { _, destination, arguments ->

            val title: String
            val locate: Int

            Log.wtf("main2" , "navListener")
            //testMenuId(destination.id)
            if (destination.label == "홈") {
                binding.toolbarLayout.conview.visibility = View.GONE
                /* binding.toolbarLayout.logoMainToolbar.visibility = View.VISIBLE
                 binding.toolbarLayout.titleTxv.visibility = View.INVISIBLE
                 binding.toolbarLayout.backBtn.visibility = View.INVISIBLE*/

            } else {
//                binding.webViewLayout.visibility = View.GONE
                binding.toolbarLayout.conview.visibility = View.VISIBLE
                binding.toolbarLayout.logoMainToolbar.visibility = View.INVISIBLE
                binding.toolbarLayout.titleTxv.visibility = View.VISIBLE
                binding.toolbarLayout.backBtn.visibility = View.VISIBLE
            }

            if (arguments?.getString("title") != null
                && arguments.getString("title") != "null"
            ) {
                title = arguments.getString("title", "")
                locate = arguments.getInt("state", 0)

            } else {

                if (destination.label == "QR" || destination.label == "NFC") {
                    title = destination.label.toString() + " 스캔"
                    locate = 2
                } else {
                    title = destination.label.toString()
                    locate = 1
                }
            }



            setScanToolbar(locate = locate)
            binding.toolbarLayout.titleTxv.text = title

        }






    private fun setScanToolbar(locate: Int) {

        val item = toolbarItem[locate]
        item?.let {

            //배경
            binding.toolbarLayout.toolbarLayout.setBackgroundColor(
                ContextCompat.getColor(
                    this@MainActivity2,
                    item.background
                )
            )

            //제목
            binding.toolbarLayout.titleTxv.setTextColor(
                ContextCompat.getColor(
                    this@MainActivity2,
                    item.title_color
                )
            )

            //뒤로가기
            Glide.with(this@MainActivity2)
                .load(ContextCompat.getDrawable(this, item.backBtn_ic))
                .into(binding.toolbarLayout.backBtn)


            //메뉴
            /* Glide.with(this@MainActivity2)
                 .load(ContextCompat.getDrawable(this@MainActivity2, item.tab_ic))
                 .into(binding.toolbarLayout.tapImb)*/
        }


    }


}