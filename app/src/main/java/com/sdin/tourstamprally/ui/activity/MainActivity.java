package com.sdin.tourstamprally.ui.activity;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
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
import com.sdin.tourstamprally.ui.fragment.DirectionGuidFragment;
import com.sdin.tourstamprally.ui.fragment.LocationFragment;
import com.sdin.tourstamprally.ui.fragment.MainFragment;
import com.sdin.tourstamprally.ui.fragment.NFCFragment;
import com.sdin.tourstamprally.ui.fragment.QRscanFragment;
import com.sdin.tourstamprally.ui.fragment.StoreListFragment;
import com.sdin.tourstamprally.ui.fragment.TourRecordFragment;
import com.sdin.tourstamprally.utill.ItemOnClick;
import com.sdin.tourstamprally.utill.NFCListener;


public class MainActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener, ItemOnClick {

    private ActivityMainBinding binding;
    public final String TAG = "MainActivity";
    private NfcAdapter nfcAdapter;
    private boolean NfcMode = false;
    private PendingIntent pendingIntent;
    private IntentFilter writingTagFilters[];
    public static boolean ISNfcInable;
    private FragmentManager fragmentManager;
    private String NOWFRAGMENT = "Main";
    private Fragment fragment;
    private NFCFragment nfcFragment = new NFCFragment();
    private QRscanFragment QRscanFragment = new QRscanFragment();
    private DirectionGuidFragment directionGuidFragment;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MainActivity", "onCreate ");
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        initView();
        setNFC();

    }

    private void setToolbar(int pos) {
        int gg = getSupportFragmentManager().getBackStackEntryCount();
        Log.d("gg =", String.valueOf(gg));
        for (int i = 0; i <= gg; i++){
            String name = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount()- 1).getName();
            Log.wtf("name!!!!", name);
        }
        String name = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount()- 1).getName();
        Log.d("name", name);
        if (name.equals("NFC") || name.equals("QR")){
            String title = name.equals("QR")? name + "코드" : name;
            binding.toolbarLayout.toolbarLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.popup_buttonColor));
            binding.toolbarLayout.backBtn.setVisibility(View.VISIBLE);
            binding.toolbarLayout.titleTxv.setVisibility(View.VISIBLE);
            binding.toolbarLayout.titleTxv.setText(title+ " 스캔");
            binding.toolbarLayout.logoMainToolbar.setVisibility(View.GONE);
            binding.toolbarLayout.tapImb.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_hamburger_white_24));
        }else if (name.equals("Main")){
            binding.toolbarLayout.toolbarLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.White));
            binding.toolbarLayout.backBtn.setVisibility(View.GONE);
            binding.toolbarLayout.titleTxv.setVisibility(View.GONE);
            binding.toolbarLayout.logoMainToolbar.setVisibility(View.VISIBLE);
            binding.toolbarLayout.tapImb.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.hamberger_menu_resize));
        }else {
            if (name.equals("direction_guid")){
                name = "길안내 관광지";
            }else if (name.equals("location_fragment")){

            }
            Log.wtf("setToolbar","else");
            binding.toolbarLayout.toolbarLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.White));
            binding.toolbarLayout.backBtn.setVisibility(View.VISIBLE);
            Glide.with(this).load(ContextCompat.getDrawable(this, R.drawable.back_ic_resize)).into(binding.toolbarLayout.backBtn);
            binding.toolbarLayout.titleTxv.setVisibility(View.VISIBLE);
            binding.toolbarLayout.titleTxv.setTextColor(ContextCompat.getColor(this, R.color.Black));
            binding.toolbarLayout.titleTxv.setText(name);
            binding.toolbarLayout.logoMainToolbar.setVisibility(View.GONE);
            binding.toolbarLayout.tapImb.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.hamberger_menu_resize));
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
        fragmentManager = getSupportFragmentManager();


        binding.toolbarLayout.backBtn.setOnClickListener(v -> {
            Log.wtf("MainAct FCount", String.valueOf(fragmentManager.getBackStackEntryCount()));
            if (fragmentManager.getBackStackEntryCount() > 0) {
                Log.wtf("MainAct", "if");
                fragmentManager.beginTransaction().remove(fragment).commit();
                fragmentManager.popBackStack();
            } else {
                Log.wtf("MainAct", String.valueOf(fragmentManager.getBackStackEntryCount()));
            }
            setToolbar(2);
        });
        binding.bottomNavigationView.setOnNavigationItemSelectedListener(this);
        setFragment("Main", new MainFragment().newInstance("",""));
    }

    public void openDrawa() {
        binding.drawaLayout.openDrawer(GravityCompat.END);
    }


    public void logout() {
        Log.d(TAG, "logout");
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.page_home:
                setFragment("Main", new MainFragment().newInstance("", ""));
                break;

            case R.id.page_store:
                setFragment("Store", new StoreListFragment());
                //setFragment("NFC", nfcFragment);
                break;

            case R.id.page_report:
                setFragment("Recode", new TourRecordFragment());
                break;

            case R.id.page_navi:
                setKaKaoNavi();
                //setFragment("Navi", new TourSpotPointFragment());
                // TODO: 7/7/21 navi popup
                //setFragment("Tour", new TourRecordFragment());
                break;

        }
        return true;
    }

    private void setFragment(String tag, Fragment fragment) {
        this.fragment = fragment;
        if (tag.equals("Main")){
            binding.webViewLayout.setVisibility(View.VISIBLE);
        }else {
            binding.webViewLayout.setVisibility(View.GONE);
        }
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(binding.framelayout.getId(), this.fragment, tag)
                .addToBackStack(tag)
                .commit();
        /*ft = fragmentManager.beginTransaction();
        ft.replace(binding.framelayout.getId(), fragment, tag).addToBackStack(null).commit();*/
        fragmentManager.executePendingTransactions();
        setToolbar(1);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("MainActivity", "onResume ");
        if (TextUtils.isEmpty(Utils.UserPhone) || TextUtils.isEmpty(Utils.UserPassword)){
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        }else {
            NfcModeOn();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        NfcModeOff();
        Log.d("MainActivity", "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("MainActivity", "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("MainActivity", "onDestroy");
    }



    private void NfcModeOn() {
        NfcMode = true;
        if (nfcAdapter == null){
            nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        }
        if (ISNfcInable){
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

            String name = getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount()-1).getName();
            if (name.equals("NFC")){
                if (listener != null){
                    listener.onReadTag(msgs);
                }
            }else {
                setFragment("NFC", nfcFragment);
                listener.onReadTag(msgs);
            }

            //setFragment("NFC", nfcFragment.newInstance(msgs, listener));
        }
    }

    private NFCListener listener;

    public void setOnListener(NFCListener listener){
        this.listener = listener;
    }


    @Override
    public void onClick(int position) {
        //ItemOnClcik Listener
        Log.d("onClick", String.valueOf(position));
        switch (position){

        }
    }

    @Override
    public void ItemGuid(int position) {
        Log.d("ItemGuid_MainAct", String.valueOf(position));
        switch (position){
            case 0 :

                break;

            case 1 :
                //NFC
                if (ISNfcInable) {
                    setFragment("NFC", nfcFragment);
                }else {
                    showToast("NFC를 지원하지 않는 단말기 입니다.");
                }
                break;

            case 2 :
                //QR
                setFragment("QR", QRscanFragment);
                break;

            case 3 :

                break;
        }
    }

    @Override
    public void SetFragment(String tag) {
        if (tag.equals("direction_guid")){
            directionGuidFragment = new DirectionGuidFragment().newInstance();
            setFragment("direction_guid", directionGuidFragment);
        }
    }

    @Override
    public void onItemClick(Tour_Spot tour_spot) {
        Log.wtf("onItemClick", tour_spot.toString());
        setFragment("location_fragment", new LocationFragment().newInstance(tour_spot));
        setToolbar(3);
    }

    private void setKaKaoNavi(){
        if (NaviClient.getInstance().isKakaoNaviInstalled(this)){
            Log.wtf(TAG, "카카오내비 앱으로 길안내 가능");
            startActivity(NaviClient.getInstance().navigateIntent(
                    new Location("카카오 판교오피스", "127.108640", "37.402111"),
                    new NaviOption(CoordType.WGS84)
                    )
            );
        }else {
            Log.wtf(TAG, "카카오내비 미설치 : 웹 길안내 사용 권장");
            Uri uri = NaviClient.getInstance().navigateWebUrl(
                    new Location("카카오 판교오피스", "127.108640", "37.402111"),
                    new NaviOption(CoordType.WGS84)
            );

            KakaoCustomTabsClient.INSTANCE.openWithDefault(this, uri);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        Log.d("onActivityResult Main", "onActivityResult Main: .");
        if (resultCode == Activity.RESULT_OK) {
            IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
            String re = scanResult.getContents();
            String message = re;
            Log.d("onActivityResult", "onActivityResult: ." + re);
            Toast.makeText(this, re, Toast.LENGTH_LONG).show();
        }else {
            super.onActivityResult(requestCode, resultCode, intent);
        }
    }
}