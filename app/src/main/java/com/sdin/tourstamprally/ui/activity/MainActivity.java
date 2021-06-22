package com.sdin.tourstamprally.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.sdin.tourstamprally.R;
import com.sdin.tourstamprally.databinding.ActivityMainBinding;
import com.sdin.tourstamprally.databinding.DrawaMenuBinding;
import com.sdin.tourstamprally.ui.fragment.MainFragment;
import com.sdin.tourstamprally.utill.ItemOnClick;


public class MainActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private ActivityMainBinding binding;
    public final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        initView();

    }

    @Override
    protected void initView() {
        DrawaRecyclerViewAdapter adapter = new DrawaRecyclerViewAdapter();
        binding.setActivity(this);
        binding.navigationLayout.setActivity(this);
        binding.navigationLayout.drawaRecyclerview.setHasFixedSize(true);
        binding.navigationLayout.drawaRecyclerview.setAdapter(adapter);
        adapter.setListener(position -> {
            switch (position){
                case 0 : Log.d("AdapterItemClick", "0");
                    break;

                case 1 : Log.d("AdapterItemClick", "1");
                    break;

                case 2 : Log.d("AdapterItemClick", "2");
                    break;

                case 3 : Log.d("AdapterItemClick", "3");
                    break;

                case 4 : Log.d("AdapterItemClick", "4");
                    break;
            }
        });
        binding.bottomNavigationView.setOnNavigationItemSelectedListener(this);
        getSupportFragmentManager().beginTransaction().replace(binding.framelayout.getId(), new MainFragment()).commit();
    }

    public void openDrawa(){
        binding.drawaLayout.openDrawer(GravityCompat.END);
    }



    public void logout(){
        Log.d(TAG, "logout");
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (item.getItemId()){
            case R.id.page_home :
                transaction.replace(binding.framelayout.getId(), new MainFragment()).commit();
                break;

            case R.id.page_store :
                transaction.replace(binding.framelayout.getId(), new MainFragment()).commit();
                break;

            case R.id.page_report :
                transaction.replace(binding.framelayout.getId(), new MainFragment()).commit();
                break;

            case R.id.page_navi :
                transaction.replace(binding.framelayout.getId(), new MainFragment()).commit();
                break;

        }
        return true;
    }

    public class DrawaRecyclerViewAdapter extends RecyclerView.Adapter<DrawaRecyclerViewAdapter.ViewHolder>{

        private ItemOnClick listener = null;

        public void setListener(ItemOnClick listener){
            this.listener = listener;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ViewHolder(DrawaMenuBinding.inflate(
                    LayoutInflater.from(parent.getContext())
                    , parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            switch (position){
                case 0 :
                    setItem(R.drawable.setting_icon, "계정관리", false, holder);
                    break;
                case 1 :
                    setItem(R.drawable.notice_icon, "공지사항", true, holder);
                    holder.binding.newImv.setVisibility(View.VISIBLE);
                    break;
                case 2 :
                    setItem(R.drawable.coupon_status_icon, "쿠폰현황",false, holder);
                    break;
                case 3 :
                    setItem(R.drawable.alarm_settings_icon, "알림설정",false, holder);
                    break;
                case 4 :
                    setItem(R.drawable.steamed_list_icon, "찜한목록", false, holder);
                    break;
            }
        }

        private void setItem(int iconId, String title, boolean isNew,ViewHolder holder){
            holder.binding.menuIcon.setImageResource(iconId);
            holder.binding.menuTxv.setText(title);
            if (isNew) holder.binding.newImv.setVisibility(View.VISIBLE);
            else holder.binding.newImv.setVisibility(View.INVISIBLE);
        }

        @Override
        public int getItemCount() {
            return 5;
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            DrawaMenuBinding binding;
            public ViewHolder(@NonNull DrawaMenuBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
                binding.drawaMenuLayout.setOnClickListener( v -> {
                    if (getAdapterPosition() != RecyclerView.NO_POSITION)
                        if (listener != null) listener.onClick(getAdapterPosition());
                });

            }
        }
    }
}