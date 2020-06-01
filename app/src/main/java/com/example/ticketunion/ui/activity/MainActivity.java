package com.example.ticketunion.ui.activity;

import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.ticketunion.R;
import com.example.ticketunion.base.BaseActivity;
import com.example.ticketunion.base.BaseFragment;
import com.example.ticketunion.ui.fragment.HomeFragment;
import com.example.ticketunion.ui.fragment.OnSellFragment;
import com.example.ticketunion.ui.fragment.SearchFragment;
import com.example.ticketunion.ui.fragment.SelectedFragment;
import com.example.ticketunion.utils.LogUtil;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    @BindView(R.id.main_navigation_bar)
    public BottomNavigationView mNavigationView;
    //四个Fragment
    private HomeFragment mHomeFragment;
    private SelectedFragment mSelectFragment;
    private OnSellFragment mRedPacket;
    private SearchFragment mSearch;
    private FragmentManager mFragmentManager;


    @Override
    protected void initView() {
        initFragment();
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected void initEvent() {
        initListener();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    private void initFragment() {
        mHomeFragment = new HomeFragment();
        mSelectFragment = new SelectedFragment();
        mRedPacket = new OnSellFragment();
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
                        LogUtil.d(this, "切换到首页");
                        switchFragment(mHomeFragment);
                        break;
                    case R.id.selected:
                        LogUtil.d(this, "切换到精选");
                        switchFragment(mSelectFragment);
                        break;
                    case R.id.red_packet:
                        LogUtil.d(this, "切换到红包");
                        switchFragment(mRedPacket);
                        break;
                    case R.id.search:
                        LogUtil.d(this, "切换到搜索");
                        switchFragment(mSearch);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    private BaseFragment lastFragment = null;

    /**
     * 切换Fragment
     * @param fragment
     */
    private void switchFragment(BaseFragment fragment) {
        if (lastFragment == fragment) {
            return;
        }
        FragmentTransaction transaction = mFragmentManager.beginTransaction();

        if (!fragment.isAdded()) {
            transaction.add(R.id.main_page_container, fragment);
        } else {
            transaction.show(fragment);
        }
        if (lastFragment != null) {
            transaction.hide(lastFragment);
        }
        lastFragment = fragment;
        transaction.commit();
    }


}
