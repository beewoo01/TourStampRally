package com.sdin.tourstamprally.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.kakao.sdk.common.util.KakaoCustomTabsClient;
import com.kakao.sdk.navi.NaviClient;
import com.kakao.sdk.navi.model.CoordType;
import com.kakao.sdk.navi.model.Location;
import com.kakao.sdk.navi.model.NaviOption;
import com.sdin.tourstamprally.R;
import com.sdin.tourstamprally.Utils;
import com.sdin.tourstamprally.adapter.DrawaRecyclerViewAdapter;
import com.sdin.tourstamprally.databinding.ActivityMainBinding;
import com.sdin.tourstamprally.model.Tour_Spot;
import com.sdin.tourstamprally.model.TouristSpotPoint;
import com.sdin.tourstamprally.model.UserModel;
import com.sdin.tourstamprally.ui.fragment.AccountFragment;
import com.sdin.tourstamprally.ui.fragment.CouponMainFragment;
import com.sdin.tourstamprally.ui.fragment.DeabsFragment;
import com.sdin.tourstamprally.ui.fragment.DirectionGuidFragment;
import com.sdin.tourstamprally.ui.fragment.LocationFragment;
import com.sdin.tourstamprally.ui.fragment.MainFragment;
import com.sdin.tourstamprally.ui.fragment.NFCFragment;
import com.sdin.tourstamprally.ui.fragment.NoticeFragment;
import com.sdin.tourstamprally.ui.fragment.QRscanFragment;
import com.sdin.tourstamprally.ui.fragment.SetAlarmFragment;
import com.sdin.tourstamprally.ui.fragment.StoreListFragment;
import com.sdin.tourstamprally.ui.fragment.TourDetailFragment;
import com.sdin.tourstamprally.ui.fragment.TourSpotPointFragment;
import com.sdin.tourstamprally.ui.fragment.VisitHistoryFragment;
import com.sdin.tourstamprally.ui.fragment.WriteReviewFragment;
import com.sdin.tourstamprally.utill.ItemOnClick;
import com.sdin.tourstamprally.utill.NFCListener;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;


public class MainActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener, ItemOnClick {

    private ActivityMainBinding binding;
    public final String TAG = "MainActivity";
    private NfcAdapter nfcAdapter;
    private boolean NfcMode = false;
    private PendingIntent pendingIntent;
    private IntentFilter[] writingTagFilters;
    public static boolean ISNfcInable;
    private FragmentManager fragmentManager;
    private final String NOWFRAGMENT = "Main";
    private Fragment fragment;
    private final NFCFragment nfcFragment = new NFCFragment();
    private final QRscanFragment QRscanFragment = new QRscanFragment();
    private final CouponMainFragment couponMainFragment = new CouponMainFragment();
    private final DeabsFragment deabsFragment = new DeabsFragment();
    private final SetAlarmFragment setAlarmFragment = new SetAlarmFragment();
    private long backKeyPressedTime = 0;


    private final Map<Integer, String> hashMap = new HashMap<>();
    private int fragmentcount = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MainActivity", "onCreate ");
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        initView();
        setNFC();
        //getHashKey();

    }

    /*private void getHashKey(){
        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageInfo == null)
            Log.e("KeyHash", "KeyHash:null");

        for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash!! ", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            } catch (NoSuchAlgorithmException e) {
                Log.e("KeyHash", "Unable to get MessageDigest. signature=" + signature, e);
            }
        }


        String keyHash = com.kakao.util.maps.helper.Utility.getKeyHash(this);
        Log.wtf("keyHash!2222", keyHash);
    }*/

    @SuppressLint("SetTextI18n")
    private void setToolbar(int pos) {

        /*Log.wtf("ggggghash", hashMap.get(fragmentcount));
        Log.wtf("getBackStackEntryCount", String.valueOf(fragmentManager.getBackStackEntryCount()));
        Log.wtf("getBackStackEntryCount", String.valueOf(fragmentManager.getBackStackEntryCount()));
        Log.wtf("getBackStackEntryCount", String.valueOf(fragmentManager.getBackStackEntryCount()));*/
        /*int gg = getSupportFragmentManager().getBackStackEntryCount();
        Log.d("gg =", String.valueOf(gg));
        for (int i = 0; i <= gg; i++){
            String name = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount()- 1).getName();
            //Log.wtf("name!!!!", name);
        }*/
        //String name = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName();
        String name = hashMap.get(fragmentcount);

        /*Log.d("name", name);
        Log.wtf("name11111111111111111", name);*/
        if (name != null) {
            switch (name) {
                case "NFC":
                case "QR":
                    String title = name.equals("QR") ? name + "코드" : name;
                    binding.toolbarLayout.toolbarLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.popup_buttonColor));
                    binding.toolbarLayout.backBtn.setVisibility(View.VISIBLE);
                    binding.toolbarLayout.titleTxv.setVisibility(View.VISIBLE);
                    binding.toolbarLayout.titleTxv.setText(title + " 스캔");
                    binding.toolbarLayout.titleTxv.setTextColor(ContextCompat.getColor(this, R.color.White));
                    binding.toolbarLayout.logoMainToolbar.setVisibility(View.GONE);
                    Glide.with(this).load(ContextCompat.getDrawable(this, R.drawable.ic_backspace_white_24)).into(binding.toolbarLayout.backBtn);
                    binding.toolbarLayout.tapImb.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_hamburger_white_24));
                    break;
                case "Main":
                    binding.toolbarLayout.toolbarLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.White));
                    binding.toolbarLayout.backBtn.setVisibility(View.GONE);
                    binding.toolbarLayout.titleTxv.setVisibility(View.GONE);
                    binding.toolbarLayout.logoMainToolbar.setVisibility(View.VISIBLE);
                    binding.toolbarLayout.tapImb.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.hamberger_menu_resize));
                    break;
                case "계정수정":
                case "공지사항":
                case "쿠폰현황":
                case "찜한목록":
                case "알림설정":

                    binding.toolbarLayout.toolbarLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.mainColor));
                    binding.toolbarLayout.backBtn.setVisibility(View.VISIBLE);
                    Glide.with(this).load(ContextCompat.getDrawable(this, R.drawable.ic_backspace_white_24)).into(binding.toolbarLayout.backBtn);
                    binding.toolbarLayout.titleTxv.setVisibility(View.VISIBLE);
                    binding.toolbarLayout.titleTxv.setText(name);
                    binding.toolbarLayout.titleTxv.setTextColor(ContextCompat.getColor(this, R.color.White));
                    binding.toolbarLayout.logoMainToolbar.setVisibility(View.GONE);
                    binding.toolbarLayout.tapImb.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_hamburger_white_24));

                    break;
                default:
                    if (name.equals("direction_guid")) {
                        name = "길안내 관광지";
                    } else if (name.equals("location_fragment")) {

                    }
                    //Log.wtf("setToolbar","else");
                    binding.toolbarLayout.toolbarLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.White));
                    binding.toolbarLayout.backBtn.setVisibility(View.VISIBLE);
                    Glide.with(this).load(ContextCompat.getDrawable(this, R.drawable.back_ic_resize)).into(binding.toolbarLayout.backBtn);
                    binding.toolbarLayout.titleTxv.setVisibility(View.VISIBLE);
                    binding.toolbarLayout.titleTxv.setTextColor(ContextCompat.getColor(this, R.color.Black));
                    binding.toolbarLayout.titleTxv.setText(name);
                    binding.toolbarLayout.logoMainToolbar.setVisibility(View.GONE);
                    binding.toolbarLayout.tapImb.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.hamberger_menu_resize));
                    break;
            }
        }

    }

    private void setNFC() {
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (nfcAdapter != null) {
            ISNfcInable = true;
            readfromIntent(getIntent());
            pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
            IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
            tagDetected.addCategory(Intent.CATEGORY_DEFAULT);
            writingTagFilters = new IntentFilter[]{tagDetected};
        } else ISNfcInable = false;
    }

    @Override
    protected void initView() {

        DrawaRecyclerViewAdapter adapter = new DrawaRecyclerViewAdapter(this);
        binding.setActivity(this);
        binding.toolbarLayout.setActivity(this);
        binding.navigationLayout.setActivity(this);
        binding.navigationLayout.drawaRecyclerview.setHasFixedSize(true);
        binding.navigationLayout.drawaRecyclerview.setAdapter(adapter);
        binding.drawaLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        fragmentManager = getSupportFragmentManager();

        Glide.with(this).load("http://coratest.kr/imagefile/bsr/" + Utils.User_Profile)
                .error(ContextCompat.getDrawable(this, R.drawable.sample_profile_image)).circleCrop()
                .into(binding.navigationLayout.profileIcon);


        binding.navigationLayout.userNameTxv.setText(Utils.User_Name);
        binding.bottomNavigationView.setOnNavigationItemSelectedListener(this);
        setFragment("Main", MainFragment.newInstance("", ""));
    }

    public void backClick() {
        //Log.wtf("MainAct FCount", String.valueOf(fragmentManager.getBackStackEntryCount()));
        if (fragmentManager.getBackStackEntryCount() > 0) {
            //Log.wtf("MainAct", "if");
            fragmentManager.popBackStack();
            fragmentManager.beginTransaction().remove(fragment).commit();
                /*for(Iterator<Map.Entry<Integer, String>> it = hashMap.entrySet().iterator(); it.hasNext(); ) {
                    Map.Entry<Integer, String> entry = it.next();
                    if(entry.getKey().equals("test")) {
                        it.remove();
                    }
                }*/
            fragmentcount--;


            if (hashMap.get(fragmentcount) != null){
                if (Objects.equals(hashMap.get(fragmentcount), "NFC") || Objects.equals(hashMap.get(fragmentcount), "QR")){
                    fragmentManager.popBackStack();
                    fragmentManager.beginTransaction().remove(fragment).commit();
                    fragmentcount--;
                }
            }
            /*if (Objects.requireNonNull(hashMap.get(fragmentcount)).equals("NFC") || hashMap.get(fragmentcount).equals("QR")) {

            }*/

            //Log.wtf("hash!!!", hashMap.get(fragmentcount));


            //Log.wtf("Id,@@ ", String.valueOf(getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount()-1).getId()));
            //Log.wtf("name!@#@!#@!#!@", getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount()-1).getName());


        } else {
            //Log.wtf("MainAct", String.valueOf(fragmentManager.getBackStackEntryCount()));
        }
        setToolbar(2);
    }

    public void openDrawa() {
        binding.drawaLayout.openDrawer(GravityCompat.END);
        Glide.with(this).load("http://coratest.kr/imagefile/bsr/" + Utils.User_Profile)
                .error(ContextCompat.getDrawable(this, R.drawable.sample_profile_image)).circleCrop()
                .into(binding.navigationLayout.profileIcon);

        binding.navigationLayout.userNameTxv.setText(Utils.User_Name);

    }


    public void logout() {
        Log.d(TAG, "logout");
        SharedPreferences pref = getSharedPreferences("rebuild_preference", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.remove("phone");
        editor.remove("password");
        editor.apply();

        startActivity(new Intent(this, LoginActivity.class));
        finish();


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        Fragment fragment = null;
        String tag = null;

        if (item.getItemId() == R.id.page_home) {
            fragment = MainFragment.newInstance("", "");
            tag = "Main";
            //setFragment("Main", MainFragment.newInstance("", ""));
        } else if (item.getItemId() == R.id.page_store) {
            fragment = new StoreListFragment();
            tag = "매장 리스트";
        } else if (item.getItemId() == R.id.page_report) {
            fragment = new VisitHistoryFragment();
            tag = "방문기록";
        } else if (item.getItemId() == R.id.page_navi) {
            setKaKaoNavi();
        }

        if (fragment != null){
            setFragment(tag, fragment);
        }



        /*switch (item.getItemId()) {
            case R.id.page_home:

                setFragment("Main", MainFragment.newInstance("", ""));
                break;

            case R.id.page_store:
                setFragment("매장 리스트", new StoreListFragment());
                //setFragment("NFC", nfcFragment);
                break;

            case R.id.page_report:
                //setFragment("관광지 기록", new TourRecordFragment());
                setFragment("방문기록", new VisitHistoryFragment());
                break;

            case R.id.page_navi:
                setKaKaoNavi();
                //setFragment("Navi", new TourSpotPointFragment());
                // TODO: 7/7/21 navi popup
                //setFragment("Tour", new TourRecordFragment());
                break;

        }*/
        return true;
    }


    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            Toast.makeText(this, "'뒤로' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            finish();
        }
    }

    /*private void testSetFragment(String tag, Fragment fragment){
        fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction().replace(binding.framelayout.getId(), fragment, tag).commit();
        setToolbar(1);
    }*/

    private void setFragment(String tag, Fragment fragment) {
        this.fragment = fragment;


        //hashMap.put();
        if (tag != null){
            if (tag.equals("Main")) {
                binding.webViewLayout.setVisibility(View.VISIBLE);
            } else {
                binding.webViewLayout.setVisibility(View.GONE);
            }
            fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction().replace(binding.framelayout.getId(), this.fragment, tag)
                    .addToBackStack(tag)
                    .commit();


        /*ft = fragmentManager.beginTransaction();
        ft.replace(binding.framelayout.getId(), fragment, tag).addToBackStack(null).commit();*/
            fragmentManager.executePendingTransactions();
            fragmentcount++;
            hashMap.put(fragmentcount, getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName());


            //Log.wtf("Id,@@ ", String.valueOf(getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount()-1).getId()));
            //Log.wtf("name!@#@!#@!#!@", getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount()-1).getName());

            setToolbar(1);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        //Log.d("MainActivity", "onResume ");
        if (TextUtils.isEmpty(Utils.UserPhone) || TextUtils.isEmpty(Utils.UserPassword)) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        } else {
            NfcModeOn();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        NfcModeOff();
        //Log.d("MainActivity", "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        //Log.d("MainActivity", "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Log.d("MainActivity", "onDestroy");
    }


    private void NfcModeOn() {
        NfcMode = true;
        if (nfcAdapter == null) {
            nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        }
        if (ISNfcInable) {
            nfcAdapter.enableForegroundDispatch(this, pendingIntent, writingTagFilters, null);
        }

    }

    private void NfcModeOff() {
        NfcMode = false;
        nfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        Log.d("MainActivity", "onNewIntent ");

        readfromIntent(intent);
    }


    private void readfromIntent(Intent intent) {
        // intent action을 리드 한다.

        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action) // NFC TAG가 발견되었을때
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action) //번역상으로 NFC action 기술이 발견된게 intent에서 받아온 값이랑 같을때
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {

            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage[] msgs = null;

            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }
            }

            String name = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName();
            if (name != null) {
                if (name.equals("NFC")) {
                    if (listener != null) {
                        listener.onReadTag(msgs);
                    }
                } else {

                    /*testSetFragment("NFC", nfcFragment);*/
                    //testSetFragment("NFC", nfcFragment);
                    setFragment("NFC", nfcFragment);
                    listener.onReadTag(msgs);
                }
            }

            //setFragment("NFC", nfcFragment.newInstance(msgs, listener));
        }
    }

    private NFCListener listener;

    public void setOnListener(NFCListener listener) {
        this.listener = listener;
    }


    @Override
    public void onClick(int position) {
        //ItemOnClcik Listener
        Log.d("onClick", String.valueOf(position));
        binding.drawaLayout.closeDrawer(GravityCompat.END);

        switch (position) {
            case 0:
                UserModel userModel = new UserModel(Utils.User_Idx, Utils.UserPhone, Utils.User_Name, Utils.User_Email, Utils.User_Location, Utils.User_Profile);
                setFragment("계정수정", AccountFragment.newInstance(userModel));
                break;

            case 1:
                setFragment("공지사항", new NoticeFragment());
                //공지
                break;

            case 2:
                setFragment("쿠폰현황", couponMainFragment);
                //쿠폰현황
                break;

            case 3:
                setFragment("알림설정", setAlarmFragment);
                //알림설정
                break;

            case 4:
                setFragment("찜한목록", deabsFragment);
                //찜한목록
                break;


        }
    }

    @Override
    public void ItemGuid(int position) {
        Log.d("ItemGuid_MainAct111", String.valueOf(position));
        if (position == 1) {
            if (ISNfcInable) {
                //testSetFragment("NFC", nfcFragment);
                setFragment("NFC", nfcFragment);
            } else {
                showToast("NFC를 지원하지 않는 단말기 입니다.");
            }
        } else if (position == 2) {
            //testSetFragment("NFC", QRscanFragment);
            setFragment("QR", QRscanFragment);
        }

    }

    @Override
    public void ItemGuidForPoint(Tour_Spot model) {

        //관광지 포인트 화면 이동
        //Log.wtf("model ToSTRING", model.toString());
        setFragment(model.getTouristspot_name(), TourSpotPointFragment.newInstance(model));

        /*if (position == 0){
            //관광지 자세히 보기
            setFragment(model.getTouristspot_name(), new TourDetailFragment().newInstance(model));
        }else if (position == 3){
            setFragment(model.getTouristspot_name(), new TourSpotPointFragment().newInstance(model));
        }*/

    }

    @Override
    public void ItemGuidForDetail(TouristSpotPoint model) {

        setFragment(model.getTouristspotpoint_name(), TourDetailFragment.newInstance(model));
    }

    @Override
    public void SetFragment(String tag) {
        if (tag.equals("direction_guid")) {

            DirectionGuidFragment directionGuidFragment = DirectionGuidFragment.newInstance();
            setFragment("direction_guid", directionGuidFragment);

        } else if (tag.equals("notice")) {


        }
    }

    @Override
    public void onItemClick(Tour_Spot tour_spot) {
        //Log.wtf("onItemClick", tour_spot.toString());
        setFragment(tour_spot.getLocation_name() + " 랠리 맵", LocationFragment.newInstance(tour_spot));
        setToolbar(3);
    }

    @Override
    public void onWriteRewviewClick(int spotIdx, String spotName) {
        //Log.wtf("onWriteRewviewClick", "mainActivity");
        setFragment("리뷰작성", WriteReviewFragment.newInstance(spotIdx, spotName));
    }

    @Override
    public void onWriteReviewSuccess() {
        backClick();
    }

    private void setKaKaoNavi() {
        if (NaviClient.getInstance().isKakaoNaviInstalled(this)) {
            //Log.wtf(TAG, "카카오내비 앱으로 길안내 가능");
            startActivity(NaviClient.getInstance().navigateIntent(
                    new Location("카카오 판교오피스", "127.108640", "37.402111"),
                    new NaviOption(CoordType.WGS84)
                    )
            );
        } else {
            //Log.wtf(TAG, "카카오내비 미설치 : 웹 길안내 사용 권장");
            Uri uri = NaviClient.getInstance().navigateWebUrl(
                    new Location("카카오 판교오피스", "127.108640", "37.402111"),
                    new NaviOption(CoordType.WGS84)
            );

            KakaoCustomTabsClient.INSTANCE.openWithDefault(this, uri);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        Log.d("onActivityResult Main", "onActivityResult Main: .");
        /*if (resultCode == Activity.RESULT_OK) {
            IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
            String re = scanResult.getContents();
            String message = re;
            Log.d("onActivityResult", "onActivityResult: ." + re);
            Toast.makeText(this, re, Toast.LENGTH_LONG).show();
        }else {
            super.onActivityResult(requestCode, resultCode, intent);
        }*/
    }
}