package com.example.ticketunion.ui.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.ViewPager;

import com.example.ticketunion.R;
import com.example.ticketunion.base.BaseFragment;
import com.example.ticketunion.model.domain.Categories;
import com.example.ticketunion.presenter.IHomePresenter;
import com.example.ticketunion.ui.adapter.HomePagerAdapter;
import com.example.ticketunion.utils.PresenterManager;
import com.example.ticketunion.view.IHomeCallback;
import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;

public class HomeFragment extends BaseFragment implements IHomeCallback {

    private IHomePresenter mHomePresenter;

    @BindView(R.id.home_indicator)
    public TabLayout mTabLayout;

    @BindView(R.id.home_pager)
    public ViewPager homePager;
    private HomePagerAdapter mHomePagerAdapter;

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView(View rootView) {
        mTabLayout.setupWithViewPager(homePager);
        mHomePagerAdapter = new HomePagerAdapter(getChildFragmentManager());
        homePager.setAdapter(mHomePagerAdapter);
    }

    @Override
    protected View loadRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_base_home, container, false);
    }

    @Override
    protected void initPresenter() {
        //创建Presenter
        mHomePresenter = PresenterManager.getInstance().getHomePresenter();
        mHomePresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        //加载数据
        mHomePresenter.getCategories();
    }

    @Override
    protected void onRetryClick() {
        if (mHomePresenter != null) {
            mHomePresenter.getCategories();
        }
    }

    /**
     * Callback的回调方法
     * @param categories
     */
    @Override
    public void onCategoriesLoaded(Categories categories) {
        setupState(State.SUCCESS);
        if (mHomePagerAdapter != null) {
            //这里可以直接将所有页面加载出来
            //homePager.setOffscreenPageLimit(categories.getData().size());
            mHomePagerAdapter.setCategories(categories);
        }
    }

    @Override
    public void onError() {
        setupState(State.ERROR);
    }

    @Override
    public void onLoading() {
        setupState(State.LOADING);
    }

    @Override
    public void onEmpty() {
        setupState(State.EMPTY);
    }

    @Override
    protected void release() {
        //取消注册回调接口
        if (mHomePresenter != null) {
            mHomePresenter.unregisterViewCallback(this);
        }
    }
}
