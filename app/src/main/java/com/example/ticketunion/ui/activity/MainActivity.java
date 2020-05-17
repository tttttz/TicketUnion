package com.example.ticketunion.ui.activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.ticketunion.R;
import com.example.ticketunion.base.BaseFragment;
import com.example.ticketunion.ui.fragment.HomeFragment;
import com.example.ticketunion.ui.fragment.RedPacketFragment;
import com.example.ticketunion.ui.fragment.SearchFragment;
import com.example.ticketunion.ui.fragment.SelectedFragment;
import com.example.ticketunion.utils.LogUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    @BindView(R.id.main_navigation_bar)
    public BottomNavigationView mNavigationView;
    private HomeFragment mHomeFragment;
    private SelectedFragment mSelectFragment;
    private RedPacketFragment mRedPacket;
    private SearchFragment mSearch;
    private FragmentManager mFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initFragment();
        initListener();
    }

    private void initFragment() {
        mHomeFragment = new HomeFragment();
        mSelectFragment = new SelectedFragment();
        mRedPacket = new RedPacketFragment();
        mSearch = new SearchFragment();
        //得到FragmentManager
        mFragmentManager = getSupportFragmentManager();
        switchFragment(mHomeFragment);
    }

    /**
     * 初始化监听器
     */
    private void initListener() {
        mNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //Log.d(TAG, "title ==>" + item.getTitle());
                int itemId = item.getItemId();
                switch (itemId) {
                    case R.id.home:
                        LogUtil.d(MainActivity.class, "切换到首页");
                        switchFragment(mHomeFragment);
                        break;
                    case R.id.selected:
                        LogUtil.d(MainActivity.class, "切换到精选");
                        switchFragment(mSelectFragment);
                        break;
                    case R.id.red_packet:
                        LogUtil.d(MainActivity.class, "切换到红包");
                        switchFragment(mRedPacket);
                        break;
                    case R.id.search:
                        LogUtil.d(MainActivity.class, "切换到搜索");
                        switchFragment(mSearch);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    /**
     * 切换Fragment
     * @param fragment
     */
    private void switchFragment(BaseFragment fragment) {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.replace(R.id.main_page_container, fragment);
        transaction.commit();
    }
}
