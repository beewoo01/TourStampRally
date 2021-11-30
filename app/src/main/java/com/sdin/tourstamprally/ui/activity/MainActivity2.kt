package com.sdin.tourstamprally.ui.activity

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.nfc.tech.Ndef
import android.os.Bundle
import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.sdin.tourstamprally.R
import com.sdin.tourstamprally.adapter.DrawaRecyclerViewAdapter
import com.sdin.tourstamprally.databinding.ActivityMain2Binding
import com.sdin.tourstamprally.model.Location_four
import com.sdin.tourstamprally.model.RallyMapDTO
import com.sdin.tourstamprally.model.ReviewWriter
import com.sdin.tourstamprally.model.TouristSpotPoint
import com.sdin.tourstamprally.utill.ItemOnClick
import com.sdin.tourstamprally.utill.NFCListener
import java.util.ArrayList

class MainActivity2 : AppCompatActivity(), ItemOnClick {

    private lateinit var binding: ActivityMain2Binding
    private var currentnavController: LiveData<NavController>? = null
    private var nfcAdapter: NfcAdapter? = null
    private var ISNfcInable = false
    private var pendingIntent: PendingIntent? = null
    private lateinit var writingTagFilters: Array<IntentFilter>
    private var nfcListener: NFCListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@MainActivity2, R.layout.activity_main2)
        binding.activity = this@MainActivity2
        binding.toolbarLayout.activity = this
        binding.navigationLayout.drawaRecyclerview.apply {
            setHasFixedSize(true)
            adapter = DrawaRecyclerViewAdapter(this@MainActivity2)
        }
        binding.drawaLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)


        initView()

        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        }

    }

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
        }else {
            nfcAdapter!!.enableForegroundDispatch(this@MainActivity2, pendingIntent, writingTagFilters, null)
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

                        //setFragment("NFC", nfcFragment)
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
        supportFragmentManager.addOnBackStackChangedListener {
            val backStackEntryCount = supportFragmentManager.backStackEntryCount
            val fragments = supportFragmentManager.fragments
            val fragmentCount = fragments.size
        }
    }

    private fun setupBottomNavigationBar() {
        val controller = binding.bottomNavigationView.setupWithNavController(
            findNavController(binding.navHostFragmnet.id)
        )


    }

    override fun onClick(position: Int) {

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

    }

}