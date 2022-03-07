package com.sdin.tourstamprally.ui.activity;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.exifinterface.media.ExifInterface;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationBarView;
import com.sdin.tourstamprally.R;
import com.sdin.tourstamprally.Utils;
import com.sdin.tourstamprally.adapter.DrawaRecyclerViewAdapter;
import com.sdin.tourstamprally.databinding.ActivityMainBinding;
import com.sdin.tourstamprally.model.Location_four;
import com.sdin.tourstamprally.model.RallyMapDTO;
import com.sdin.tourstamprally.model.ReviewWriter;
import com.sdin.tourstamprally.model.TouristSpotPoint;
import com.sdin.tourstamprally.model.UserModel;
import com.sdin.tourstamprally.ui.dialog.ReadyDialog;
import com.sdin.tourstamprally.ui.fragment.AccountFragment;
import com.sdin.tourstamprally.ui.fragment.CouponMainFragment;
import com.sdin.tourstamprally.ui.fragment.DeabsFragment;
import com.sdin.tourstamprally.ui.fragment.DirectionGuidFragment;
import com.sdin.tourstamprally.ui.fragment.LocationFragment;
import com.sdin.tourstamprally.ui.fragment.MainFragment;
import com.sdin.tourstamprally.ui.fragment.MoreReviewFragment;
import com.sdin.tourstamprally.ui.fragment.NFCFragment;
import com.sdin.tourstamprally.ui.fragment.NoticeFragment;
import com.sdin.tourstamprally.ui.fragment.QRscanFragment;
import com.sdin.tourstamprally.ui.fragment.ReviewComentsFragment;
import com.sdin.tourstamprally.ui.fragment.SetAlarmFragment;
import com.sdin.tourstamprally.ui.fragment.TourDetailFragment;
import com.sdin.tourstamprally.ui.fragment.TourSpotPointFragment;
import com.sdin.tourstamprally.ui.fragment.VisitHistoryFragment;
import com.sdin.tourstamprally.ui.fragment.WriteReviewFragment;
import com.sdin.tourstamprally.utill.listener.ItemOnClick;
import com.sdin.tourstamprally.utill.listener.NFCListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import gun0912.tedkeyboardobserver.TedKeyboardObserver;


public class MainActivity extends BaseActivity implements NavigationBarView.OnItemSelectedListener, ItemOnClick {

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
    private final CouponMainFragment couponMainFragment = new CouponMainFragment();
    private final DeabsFragment deabsFragment = new DeabsFragment();
    private final SetAlarmFragment setAlarmFragment = new SetAlarmFragment();
    private long backKeyPressedTime = 0;

    public static boolean keyboardState = false;

    private String currentPhotoPath = null;


    private final Map<Integer, String> hashMap = new HashMap<>();
    private int fragmentcount = 0;

    private boolean isDrawerOpen = false;


    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String gew =  "2021-11-02 16:20:42";
        Calendar calendar = Calendar.getInstance();
        try {
            Date to = sdf.parse(gew);
            calendar.setTime(to);
            System.out.println("YEAR = " + calendar.get(Calendar.YEAR));
        } catch (ParseException e) {
            e.printStackTrace();
        }


        /*calendar.setTime(date);

        Calendar.getInstance();*/
        //Log.wtf("", );

        initView();
        setNFC();
        //getHashKey();

    }


    @SuppressLint("SetTextI18n")
    private void setToolbar(int pos) {


        String name = hashMap.get(fragmentcount);

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
        //binding.navigationLayout.setActivity(this);
        binding.navigationLayout.drawaRecyclerview.setHasFixedSize(true);
        binding.navigationLayout.drawaRecyclerview.setAdapter(adapter);
        binding.drawaLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        fragmentManager = getSupportFragmentManager();


        Glide.with(this).load("http://coratest.kr/imagefile/bsr/" + Utils.User_Profile)
                .error(ContextCompat.getDrawable(this, R.drawable.sample_profile_image)).circleCrop()
                .into(binding.navigationLayout.profileIcon);


        binding.navigationLayout.userNameTxv.setText(Utils.User_Name);
        binding.bottomNavigationView.setOnItemSelectedListener(this);
        setFragment("Main", MainFragment.newInstance("", ""));
    }

    public void backClick() {
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
            fragmentManager.beginTransaction().remove(fragment).commit();


            fragmentcount--;


            if (keyboardState) {
                InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                manager.hideSoftInputFromWindow(binding.drawaLayout.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                keyboardState = false;
            }

            if (hashMap.get(fragmentcount) != null) {
                if (Objects.equals(hashMap.get(fragmentcount), "NFC") || Objects.equals(hashMap.get(fragmentcount), "QR")) {
                    fragmentManager.popBackStack();
                    fragmentManager.beginTransaction().remove(fragment).commit();
                    fragmentcount--;


                }
            }


        } else {

        }
        setToolbar(2);
    }

    public void openDrawa() {
        binding.drawaLayout.openDrawer(GravityCompat.END);
        Glide.with(this).load("http://coratest.kr/imagefile/bsr/" + Utils.User_Profile)
                .error(ContextCompat.getDrawable(this, R.drawable.sample_profile_image)).circleCrop()
                .into(binding.navigationLayout.profileIcon);

        binding.navigationLayout.userNameTxv.setText(Utils.User_Name);
        isDrawerOpen = true;

    }


    public void logout() {
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
            item.setChecked(true);


        } else if (item.getItemId() == R.id.page_store) {
            ReadyDialog readyDialog = new ReadyDialog(this);
            readyDialog.show();
            /*fragment = new StoreListFragment();
            tag = "매장 리스트";*/
        } else if (item.getItemId() == R.id.page_report) {
            fragment = new VisitHistoryFragment();
            tag = "방문기록";
        } else if (item.getItemId() == R.id.camera) {
            /*Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra("CallType", 1);
            resultLauncher.launch(intent);*/

            dispatchTakePictureIntent();


        } else if (item.getItemId() == R.id.page_stamp) {
            //setKaKaoNavi();
            /*StampDialog stampDialog = new StampDialog(this);
            stampDialog.show();
            stampDialog.setClickListener(this);*/
            setFragment("QR", new QRscanFragment());
        }

        if (fragment != null) {
            setFragment(tag, fragment);
        }

        return true;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            photoFile = createImageFile();

            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.sdin.tourstamprally.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                resultLauncher.launch(takePictureIntent);
            }

        }
    }

    /*private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        resultLauncher.launch(takePictureIntent);
    }*/

    private final ActivityResultLauncher<Intent> resultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {

                    File file = new File(currentPhotoPath);
                    Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    saveMediaToStorage(bitmap);
                }
            }
    );


    @SuppressLint("SimpleDateFormat")
    private File createImageFile() {
        try {

            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_";
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );

            currentPhotoPath = image.getAbsolutePath();

            return image;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


        // Save a file: path for use with ACTION_VIEW intents
    }


    @SuppressLint("SimpleDateFormat")
    private void saveMediaToStorage(Bitmap bitmap) {

        ExifInterface exif = null;
        try {
            exif = new ExifInterface(currentPhotoPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);

        Bitmap bmRotated = rotateBitmap(bitmap, orientation);



        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "jpeg_" + timeStamp + "_";
        OutputStream outputStream = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, imageFileName);
            contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
            contentValues.put(MediaStore.Images.Media.WIDTH, bmRotated.getWidth());
            contentValues.put(MediaStore.Images.Media.HEIGHT, bmRotated.getHeight());

            Uri imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            if (imageUri != null) {
                try {
                    outputStream = getContentResolver().openOutputStream(imageUri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }

        } else {

            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File image = new File(storageDir, imageFileName);
            try {
                outputStream = new FileOutputStream(image);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        if (outputStream != null) {
            bmRotated.compress(Bitmap.CompressFormat.JPEG, 95, outputStream);
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //bitmap.recycle();
            Toast.makeText(this, "저장 성공", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "저장에 실패하였습니다.", Toast.LENGTH_SHORT).show();
        }


    }

    private Bitmap rotateBitmap(Bitmap bitmap, int orientation) {
        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_NORMAL:
                return bitmap;
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }
        try {
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return bmRotated;
        }
        catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }


    /*@SuppressLint("SimpleDateFormat")
    private void saveMediaToStorage(Bitmap bitmap) {

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "jpeg_" + timeStamp + "_";
        OutputStream outputStream = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, imageFileName);
            contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
            contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);
            *//*contentValues.put(MediaStore.Images.Media.WIDTH, bitmap.getWidth());
            contentValues.put(MediaStore.Images.Media.HEIGHT, bitmap.getHeight());*//*

            Uri imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            if (imageUri != null) {
                try {
                    outputStream = getContentResolver().openOutputStream(imageUri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }

        } else {

            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File image = new File(storageDir, imageFileName);
            try {
                outputStream = new FileOutputStream(image);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        if (outputStream != null) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 95, outputStream);
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //bitmap.recycle();
            Toast.makeText(this, "저장성공", Toast.LENGTH_SHORT).show();
            Log.wtf("outputStream", "notnull");
        }else {
            Log.wtf("outputStream", "null....");
        }


    }*/

    @Override
    public void onBackPressed() {
        backKeyPressedTime = System.currentTimeMillis();
        //Log.wtf("fragmentTag", fragment.getTag());
        if (isDrawerOpen) {
            binding.drawaLayout.closeDrawer(GravityCompat.END);
            isDrawerOpen = false;
        } else {
            if (!Objects.equals(hashMap.get(fragmentcount), "Main")) {
                backClick();
            } else if (Objects.equals(hashMap.get(fragmentcount), "Main")) {
                finish();
            }
        }



        /*if (System.currentTimeMillis() > backKeyPressedTime + 1000) {
            Toast.makeText(this, "'뒤로' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (System.currentTimeMillis() <= backKeyPressedTime + 1000) {
            finish();
        }*/
    }


    private void setFragment(String tag, Fragment fragment) {
        this.fragment = fragment;


        if (tag != null) {

            new TedKeyboardObserver(this)
                    .listen(isShow -> {
                        keyboardState = isShow;
                    });


            if (tag.equals("Main")) {


                binding.webViewLayout.setVisibility(View.VISIBLE);
            } else {
                binding.webViewLayout.setVisibility(View.GONE);
            }
            fragmentManager = getSupportFragmentManager();


            fragmentManager.beginTransaction().replace(binding.framelayout.getId(), this.fragment, tag)
                    .addToBackStack(tag)
                    .commit();


            fragmentManager.executePendingTransactions();
            fragmentcount++;
            hashMap.put(fragmentcount, getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName());


            setToolbar(1);
        }

        if (keyboardState) {
            InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            manager.hideSoftInputFromWindow(binding.drawaLayout.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            keyboardState = false;
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
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
        binding.drawaLayout.closeDrawer(GravityCompat.END);
        isDrawerOpen = false;

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
                ReadyDialog readyDialog = new ReadyDialog(this);
                readyDialog.show();
                //setFragment("쿠폰현황", couponMainFragment);
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
        if (position == 1) {
            if (ISNfcInable) {
                //testSetFragment("NFC", nfcFragment);
                setFragment("NFC", nfcFragment);
            } else {
                showToast("NFC를 지원하지 않는 단말기 입니다.");
            }
        } else if (position == 2) {
            //testSetFragment("NFC", QRscanFragment);
            setFragment("QR", new QRscanFragment());
        }

    }

    @Override
    public void ItemGuidForPoint(RallyMapDTO model) {

        //관광지 포인트 화면 이동
        setFragment(model.getTouristspot_name(), TourSpotPointFragment.newInstance(model));


    }

    @Override
    public void ItemGuidForDetail(TouristSpotPoint model) {
        Log.w("MainActivity", "ItemGuidForDetail");
        //setFragment(model.getTouristspotpoint_name(), TourDetailFragment.newInstance(model));
    }


    @Override
    public void SetFragment(ArrayList<Location_four> location_four) {
        DirectionGuidFragment directionGuidFragment = DirectionGuidFragment.newInstance(location_four);
        setFragment("direction_guid", directionGuidFragment);
    }

    @Override
    public void onItemClick(Location_four location_four) {
        setFragment(location_four.getLocation_name() + " 랠리 맵", LocationFragment.newInstance(location_four));
        setToolbar(3);
    }


    @Override
    public void onWriteRewviewClick(
            ReviewWriter reviewWriter
            /*int spotIdx, String spotName, float review_score, String review_contents*/) {
        //Log.wtf("onWriteRewviewClick", "mainActivity");
        setFragment("리뷰작성", WriteReviewFragment.newInstance(reviewWriter));
        /*public static            WriteReviewFragment newInstance(int spotIdx, String spotName, int review_idx ,float review_score, String review_contents, boolean isFirst)*/
    }

    @Override
    public void onWriteReviewSuccess() {
        backClick();
    }

    @Override
    public void reviewMoreClick() {
        setFragment("리뷰보기", MoreReviewFragment.newInstance());
    }

    @Override
    public void reviewItemClick(int review_idx, String spot_name) {
        /*setFragment(spot_name, ReviewComentsFragment.newInstance(review_idx));*/
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

    }
}