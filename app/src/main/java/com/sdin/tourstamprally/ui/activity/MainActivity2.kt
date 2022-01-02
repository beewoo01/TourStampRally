package com.sdin.tourstamprally.ui.activity

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
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
import com.sdin.tourstamprally.databinding.ActivityMain2Binding
import com.sdin.tourstamprally.model.Location_four
import com.sdin.tourstamprally.model.RallyMapDTO
import com.sdin.tourstamprally.model.ReviewWriter
import com.sdin.tourstamprally.model.TouristSpotPoint
import com.sdin.tourstamprally.utill.ItemOnClick
import com.sdin.tourstamprally.utill.NFCListener
import com.sdin.tourstamprally.utill.Quadruple
import com.sdin.tourstamprally.utill.navutil.DialogNavigator
import java.io.*
import java.util.*
import kotlin.collections.HashMap

class MainActivity2 : AppCompatActivity()/*, NavigationBarView.OnItemSelectedListener,
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

    private var currentBottomMenu: Int = R.id.page_home

    private val toolbarItem: Map<Int, Quadruple<Int, Int, Int, Int>> =
        hashMapOf(

            //1번 Normal
            1 to Quadruple(
                background = R.color.White,
                title_color = R.color.Black,
                backBtn_ic = R.drawable.back_ic_resize,
                tab_ic = R.drawable.ic_menu_black
            ),

            //2번 Scan
            2 to Quadruple(
                background = R.color.popup_buttonColor,
                title_color = R.color.White,
                backBtn_ic = R.drawable.ic_backspace_white_24,
                tab_ic = R.drawable.ic_menu_white
            ),

            //3번 Menu
            3 to Quadruple(
                background = R.color.mainColor,
                title_color = R.color.White,
                backBtn_ic = R.drawable.ic_backspace_white_24,
                tab_ic = R.drawable.ic_menu_white
            ),

            4 to Quadruple(
                background = R.color.comment_mainColor,
                title_color = R.color.Black,
                backBtn_ic = R.drawable.back_ic_resize,
                tab_ic = R.drawable.ic_menu_black
            )
        )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@MainActivity2, R.layout.activity_main2)
        binding.activity = this@MainActivity2
        binding.toolbarLayout.activity = this
        binding.drawaLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.account_admin,
                R.id.notice/*, R.id.coupon_list*/,
                R.id.notify_setting,
                R.id.bascet_list
            ),
            binding.drawaLayout
        )

        initView()

    }

    override fun onBackPressed() {
        super.onBackPressed()
        updateBottomMenu()
    }

    private fun updateBottomMenu() {
        binding.bottomNavigationView.menu.findItem(currentBottomMenu).isChecked = true
    }

    fun backBtnClick() {
        navController.navigateUp()
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
                navigate = R.id.ready_dialog
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

    fun openDrawa() {
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
            readfromIntent(intent)
            pendingIntent = PendingIntent.getActivity(
                this@MainActivity2, 0,
                Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0
            )
            val tagDetected = IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED)
            writingTagFilters = arrayOf(tagDetected)

        }
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
            readfromIntent(it)
        }
    }

    private fun readfromIntent(intent: Intent) {
        val action = intent.action
        if (NfcAdapter.ACTION_TAG_DISCOVERED == action
            || NfcAdapter.ACTION_TECH_DISCOVERED == action
            || NfcAdapter.ACTION_NDEF_DISCOVERED == action
        ) {

            val rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
            rawMsgs?.size?.let {
                val msgs = arrayOfNulls<NdefMessage>(it)
                for (i in 0..it) {
                    msgs[i] = rawMsgs[i] as NdefMessage
                }

                val name = supportFragmentManager.getBackStackEntryAt(
                    supportFragmentManager.backStackEntryCount - 1
                ).name

                name?.let {
                    if (name == "NFC") {
                        nfcListener?.onReadTag(msgs)
                    } else {
                        nfcListener?.onReadTag(msgs)
                    }
                }
            }


        }
    }

    fun setOnListener(nfcListener: NFCListener) {
        this.nfcListener = nfcListener
    }

    private fun initView() {

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        navController = navHostFragment.navController

        navController.addOnDestinationChangedListener(navListener)

        supportFragmentManager.addOnBackStackChangedListener {
            val backStackEntryCount = supportFragmentManager.backStackEntryCount
            val fragments = supportFragmentManager.fragments
            val fragmentCount = fragments.size
        }


        //setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navigationview.setupWithNavController(navController)
        binding.bottomNavigationView.setupWithCustomNavController(navController)
        //binding.bottomNavigationView.setOnItemSelectedListener(this@MainActivity2)
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
            /*Log.wtf("navListener", "navListener")
            Log.wtf("navListener destination.id", destination.id.toString())
            Log.wtf("navigatorName", destination.navigatorName)
            //Log.wtf("label", destination.label.toString())
            Log.wtf("arguments model", destination.arguments["model"].toString())
            Log.wtf("arguments", destination.arguments.values.toString())
            Log.wtf("real arguments", arguments?.get("model").toString())*/

            var title: String
            val locate: Int
            testMenuId(destination.id)
            if (destination.label == "홈") {
                binding.webViewLayout.visibility = View.VISIBLE
                binding.toolbarLayout.logoMainToolbar.visibility = View.VISIBLE
                binding.toolbarLayout.titleTxv.visibility = View.INVISIBLE
                binding.toolbarLayout.backBtn.visibility = View.INVISIBLE
            } else {
                binding.webViewLayout.visibility = View.GONE
                binding.toolbarLayout.logoMainToolbar.visibility = View.INVISIBLE
                binding.toolbarLayout.titleTxv.visibility = View.VISIBLE
                binding.toolbarLayout.backBtn.visibility = View.VISIBLE
            }

            if (arguments?.getString("title") != null
                && arguments.getString("title") != "null"
            ) {
                //binding.toolbarLayout.titleTxv.text = arguments.getString("title")
                title = arguments.getString("title", "")
                locate = arguments.getInt("state", 0)
                /*val isMenu: Boolean = arguments.getBoolean("menu", false)

                locate = if (isMenu) {
                    3
                } else {
                    1
                }*/

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
            Log.wtf("title~!!", arguments?.getString("title"))

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
            Glide.with(this@MainActivity2)
                .load(ContextCompat.getDrawable(this@MainActivity2, item.tab_ic))
                .into(binding.toolbarLayout.tapImb)
        }


    }


    /*private fun setupBottomNavigationBar() {
        val controller = binding.bottomNavigationView.setupWithNavController(
            findNavController(binding.navHost.id)
        )


    }*/

    /*override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {

            R.id.mainfragment -> {
                findNavController(binding.navHost.id).navigate(R.id.page_home)
            }

            R.id.page_store -> {

            }

            R.id.page_report -> {
                findNavController(binding.navHost.id).navigate(R.id.page_report)
            }

            R.id.camera -> {
                dispatchTakePictureIntent()
            }

            R.id.fragment_qr_scan -> {
                findNavController(binding.navHost.id).navigate(R.id.page_stamp)
            }


        }

        return true
    }*/

    /*private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            var photoFile: File? = null
            photoFile = createImageFile()
            if (photoFile != null) {
                val photoURI = FileProvider.getUriForFile(
                    this,
                    "com.sdin.tourstamprally.fileprovider",
                    photoFile
                )
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                resultLauncher.launch(takePictureIntent)
            }
        }
    }

    private val resultLauncher = registerForActivityResult(
        StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == RESULT_OK) {
            val file: File = File(currentPhotoPath)
            val bitmap = BitmapFactory.decodeFile(file.absolutePath)
            saveMediaToStorage(bitmap)
        }
    }


    @SuppressLint("SimpleDateFormat")
    private fun saveMediaToStorage(bitmap: Bitmap) {
        var exif: ExifInterface? = null
        try {
            exif = ExifInterface(currentPhotoPath!!)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val orientation = exif!!.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_UNDEFINED
        )
        val bmRotated: Bitmap? = rotateBitmap(bitmap, orientation)
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "jpeg_" + timeStamp + "_"
        var outputStream: OutputStream? = null
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val contentValues = ContentValues()
            contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, imageFileName)
            contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            contentValues.put(MediaStore.Images.Media.WIDTH, bmRotated?.width)
            contentValues.put(MediaStore.Images.Media.HEIGHT, bmRotated?.height)
            val imageUri =
                contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            if (imageUri != null) {
                try {
                    outputStream = contentResolver.openOutputStream(imageUri)
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }
            }
        } else {
            val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val image = File(storageDir, imageFileName)
            try {
                outputStream = FileOutputStream(image)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
        }
        if (outputStream != null) {
            bmRotated?.compress(Bitmap.CompressFormat.JPEG, 95, outputStream)
            try {
                outputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            //bitmap.recycle();
            Toast.makeText(this, "저장 성공", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "저장에 실패하였습니다.", Toast.LENGTH_SHORT).show()
        }
    }


    private fun rotateBitmap(bitmap: Bitmap, orientation: Int): Bitmap? {
        val matrix = Matrix()
        when (orientation) {
            ExifInterface.ORIENTATION_NORMAL -> return bitmap
            ExifInterface.ORIENTATION_FLIP_HORIZONTAL -> matrix.setScale(-1f, 1f)
            ExifInterface.ORIENTATION_ROTATE_180 -> matrix.setRotate(180f)
            ExifInterface.ORIENTATION_FLIP_VERTICAL -> {
                matrix.setRotate(180f)
                matrix.postScale(-1f, 1f)
            }
            ExifInterface.ORIENTATION_TRANSPOSE -> {
                matrix.setRotate(90f)
                matrix.postScale(-1f, 1f)
            }
            ExifInterface.ORIENTATION_ROTATE_90 -> matrix.setRotate(90f)
            ExifInterface.ORIENTATION_TRANSVERSE -> {
                matrix.setRotate(-90f)
                matrix.postScale(-1f, 1f)
            }
            ExifInterface.ORIENTATION_ROTATE_270 -> matrix.setRotate(-90f)
            else -> return bitmap
        }
        return try {
            val bmRotated =
                Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
            bitmap.recycle()
            bmRotated
        } catch (e: OutOfMemoryError) {
            e.printStackTrace()
            null
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun createImageFile(): File? {
        return try {
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
            val imageFileName = "JPEG_" + timeStamp + "_"
            val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val image = File.createTempFile(
                imageFileName,  *//* prefix *//*
                ".jpg",  *//* suffix *//*
                storageDir *//* directory *//*
            )
            currentPhotoPath = image.absolutePath
            Log.wtf("currentPhotoPath", currentPhotoPath)
            image
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }


        // Save a file: path for use with ACTION_VIEW intents
    }*/

    /*override fun onClick(position: Int) {

    }

    override fun ItemGuid(position: Int) {
    }

    override fun ItemGuidForPoint(model: RallyMapDTO?) {

    }

    override fun ItemGuidForDetail(model: TouristSpotPoint?) {

    }

    override fun SetFragment(location_fours: ArrayList<Location_four>?) {

    }

    override fun onItemClick(location_four: Location_four?) {

    }

    override fun onWriteRewviewClick(reviewWriter: ReviewWriter?) {

    }

    override fun onWriteReviewSuccess() {

    }

    override fun reviewMoreClick() {

    }

    override fun reviewItemClick(review_idx: Int, spot_name: String?) {

    }*/


}