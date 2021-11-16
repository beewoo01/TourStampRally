package com.sdin.tourstamprally.ui.activity

import android.os.Bundle
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
import java.util.ArrayList

class MainActivity2 : AppCompatActivity(), ItemOnClick {

    private lateinit var binding : ActivityMain2Binding
    private var currentnavController : LiveData<NavController>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this@MainActivity2, R.layout.activity_main2)
        binding.activity = this@MainActivity2
        binding.toolbarLayout.activity = this
        binding.toolbarLayout.activity = this
        //binding.navigationLayout.setActivity(this);
        val adapter = DrawaRecyclerViewAdapter(this)
        binding.navigationLayout.drawaRecyclerview.setHasFixedSize(true)
        binding.navigationLayout.drawaRecyclerview.adapter = adapter
        binding.drawaLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)


        initView()

        if (savedInstanceState == null){
            setupBottomNavigationBar()
        }

    }

    private fun initView() {
        supportFragmentManager.addOnBackStackChangedListener {
            val backStackEntryCount = supportFragmentManager.backStackEntryCount
            val fragments = supportFragmentManager.fragments
            val fragmentCount = fragments.size
        }
    }

    private fun setupBottomNavigationBar(){
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