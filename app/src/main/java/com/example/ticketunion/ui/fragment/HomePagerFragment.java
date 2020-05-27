package com.example.ticketunion.ui.fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.ticketunion.R;
import com.example.ticketunion.base.BaseFragment;
import com.example.ticketunion.model.domain.Categories;
import com.example.ticketunion.model.domain.HomePagerContent;
import com.example.ticketunion.presenter.ICategoryPagerPresenter;
import com.example.ticketunion.presenter.impl.TicketPresentImpl;
import com.example.ticketunion.ui.activity.TicketActivity;
import com.example.ticketunion.ui.adapter.HomePagerContentAdapter;
import com.example.ticketunion.ui.adapter.LooperPagerAdapter;
import com.example.ticketunion.ui.custom.AutoLooperViewPager;
import com.example.ticketunion.utils.Constants;
import com.example.ticketunion.utils.LogUtil;
import com.example.ticketunion.utils.PresenterManager;
import com.example.ticketunion.utils.SizeUtils;
import com.example.ticketunion.utils.ToastUtil;
import com.example.ticketunion.view.ICategoryPagerCallback;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.views.TbNestedScrollView;

import java.util.List;

import butterknife.BindView;

/**
 * @ProjectName: TicketUnion
 * @Author: Tz
 * @CreateDate: 2020/5/18 19:46
 * God bless my code!
 */
public class HomePagerFragment extends BaseFragment implements ICategoryPagerCallback, HomePagerContentAdapter.OnListItemClickListener, LooperPagerAdapter.OnLooperPagerClickListener {

    private ICategoryPagerPresenter mPagerPresenter;
    //当前物料id
    private int mMaterialId;

    @BindView(R.id.home_pager_content_list)
    public RecyclerView mContentList;

    @BindView(R.id.looper_pager)
    public AutoLooperViewPager looperPager;

    @BindView(R.id.home_pager_title)
    public TextView currentCategoryTitleTv;

    @BindView(R.id.looper_point_container)
    public LinearLayout looperPointContainer;

    @BindView(R.id.home_pager_refresh)
    public TwinklingRefreshLayout twinklingRefreshLayout;

    @BindView(R.id.home_pager_nested_scroller)
    public TbNestedScrollView homePagerNestedView;

    @BindView(R.id.home_pager_parent)
    public LinearLayout homePagerParent;

    @BindView(R.id.home_pager_header_container)
    public LinearLayout homeHeaderContainer;

    private HomePagerContentAdapter mContentAdapter;
    private LooperPagerAdapter mLooperPagerAdapter;

    public static HomePagerFragment newInstance(Categories.DataBean category) {
        HomePagerFragment homePagerFragment = new HomePagerFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.KEY_HOME_PAGER_TITLE, category.getTitle());
        bundle.putInt(Constants.KEY_HOME_PAGER_MATERIAL_ID, category.getId());
        homePagerFragment.setArguments(bundle);
        return homePagerFragment;
    }

    @Override
    protected void initPresenter() {
        mPagerPresenter = PresenterManager.getInstance().getCategoryPagePresenter();
        mPagerPresenter.registerViewCallback(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        //可见时开始轮播图的自动轮播
        looperPager.startLoop();
    }

    @Override
    public void onPause() {
        super.onPause();
        //不可见时停止轮播
        looperPager.stopLoop();
    }

    @Override
    protected void loadData() {
        Bundle arguments = getArguments();
        String title = arguments.getString(Constants.KEY_HOME_PAGER_TITLE, "");
        mMaterialId = arguments.getInt(Constants.KEY_HOME_PAGER_MATERIAL_ID, 0);
        //LogUtil.d(this, "title ==> " + title + " id ==> " + mMaterialId);
        if (mPagerPresenter != null) {
            mPagerPresenter.getContentByCategoryId(mMaterialId);
        }
        if (currentCategoryTitleTv != null) {
            currentCategoryTitleTv.setText(title);
        }
    }

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_home_pager;
    }

    @Override
    protected void initView(View rootView) {
        //设置布局管理器
        mContentList.setLayoutManager(new LinearLayoutManager(getContext()));
        mContentList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = 8;
                outRect.bottom = 8;
            }
        });
        //mContentList.setNestedScrollingEnabled(false);
        //创建适配器
        mContentAdapter = new HomePagerContentAdapter();
        //设置适配器
        mContentList.setAdapter(mContentAdapter);

        //轮播图相关设置
        //创建适配器
        mLooperPagerAdapter = new LooperPagerAdapter();
        //设置适配器
        looperPager.setAdapter(mLooperPagerAdapter);
        //设置refresh相关内容
        twinklingRefreshLayout.setEnableRefresh(false);
        twinklingRefreshLayout.setEnableLoadmore(true);
    }

    @Override
    protected void initListener() {

        mContentAdapter.setOnListItemClickListener(this);
        mLooperPagerAdapter.setOnLooperPagerClickListener(this);

        looperPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //切换指示器
                LogUtil.d(this, "looper size bug  == > " + mLooperPagerAdapter.getDataSize());
                //以下操作是为了避免a mod 0 出现bug导致程序崩溃
                int targetPosition = 0;
                if (mLooperPagerAdapter.getDataSize() != 0) {
                    targetPosition = position % mLooperPagerAdapter.getDataSize();
                }
                updateLooperIndicator(targetPosition);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //
        homePagerParent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (homeHeaderContainer == null) {
                    return;
                }
                int headerHeight = homeHeaderContainer.getMeasuredHeight();
                LogUtil.d(HomePagerFragment.this, "headerHeight ==> " + headerHeight);
                homePagerNestedView.setHeaderHeight(headerHeight);
                int measuredHeight = homePagerParent.getMeasuredHeight();
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mContentList.getLayoutParams();
                layoutParams.height = measuredHeight;
                mContentList.setLayoutParams(layoutParams);
                if (measuredHeight != 0) {
                    homePagerParent.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            }
        });

        //设置加载更多组件的监听
        twinklingRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                if (mPagerPresenter != null) {
                    mPagerPresenter.loadMore(mMaterialId);
                }
            }
        });
    }

    //切换指示器
    private void updateLooperIndicator(int targetPosition) {
        for (int i = 0; i < looperPointContainer.getChildCount(); i++) {
            View point = looperPointContainer.getChildAt(i);
            if (i == targetPosition) {
                point.setBackgroundResource(R.drawable.shape_indicator_point_selected);
            } else {
                point.setBackgroundResource(R.drawable.shape_indicator_point_normal);
            }
        }
    }

    @Override
    protected void release() {
        if (mPagerPresenter != null) {
            mPagerPresenter.unregisterViewCallback(this);
        }
    }

    //==================================================================
    //以下为callback的回调方法
    @Override
    public void onContentLoaded(List<HomePagerContent.DataBean> contents) {
        //数据列表加载
        mContentAdapter.setData(contents);
        setupState(State.SUCCESS);
    }



    @Override
    public int getCategoryId() {
        return mMaterialId;
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
    public void onLoadMoreError() {
        ToastUtil.showToast("网络异常，请稍后重试...");
        if (twinklingRefreshLayout != null) {
            twinklingRefreshLayout.finishLoadmore();;
        }
    }

    @Override
    public void onLoadMoreEmpty() {
        ToastUtil.showToast("没有更多的数据...");
        if (twinklingRefreshLayout != null) {
            twinklingRefreshLayout.finishLoadmore();
        }
    }

    @Override
    public void onLoadMoreLoaded(List<HomePagerContent.DataBean> contents) {
        //将数据添加到适配器的底部
        mContentAdapter.addData(contents);
        //结束刷新
        if (twinklingRefreshLayout != null) {
            twinklingRefreshLayout.finishLoadmore();
        }
        ToastUtil.showToast("加载了" + contents.size() + "条数据");
    }

    @Override
    public void onLooperListLoaded(List<HomePagerContent.DataBean> contents) {
        //LogUtil.d(this, "looper size ==> " + contents.size());
        mLooperPagerAdapter.setData(contents);
        looperPointContainer.removeAllViews();

        //设置当前的item为Integer.MAX_VALUE的中间点,可以实现伪无限循环
        //注意要先处理中间点的数，保证是从第0个开始
        int dx = (Integer.MAX_VALUE / 2) % contents.size();
        int targetCenterPosition = Integer.MAX_VALUE / 2 - dx;
        looperPager.setCurrentItem(targetCenterPosition);


        LogUtil.d(this, "url == >" + contents.get(0).getPict_url());
        //添加点
        for (int i = 0; i < contents.size(); i++) {
            View point = new View(getContext());
            int size = SizeUtils.dip2px(getContext(), 8);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(size, size);
            layoutParams.leftMargin = SizeUtils.dip2px(getContext(), 5);
            layoutParams.rightMargin = SizeUtils.dip2px(getContext(), 5);
            point.setLayoutParams(layoutParams);
            if (i == 0) {
                point.setBackgroundResource(R.drawable.shape_indicator_point_selected);
            } else {
                point.setBackgroundResource(R.drawable.shape_indicator_point_normal);
            }
            looperPointContainer.addView(point);
        }
    }


    /**
     * 列表内容的点击回调
     */
    @Override
    public void onItemClick(HomePagerContent.DataBean item) {
        LogUtil.d(this, "item click ==> " + item.getTitle());
        handleItem(item);
    }

    private void handleItem(HomePagerContent.DataBean item) {
        //拿到presenter加载数据
        String title = item.getTitle();
        String url = item.getClick_url();
        String cover = item.getPict_url();
        TicketPresentImpl ticketPresent = PresenterManager.getInstance().getTicketPresent();
        ticketPresent.getTicket(title, url, cover);
        startActivity(new Intent(getContext(), TicketActivity.class));
    }

    /**
     * 轮播图内容的点击回调
     */
    @Override
    public void onLooperItemClick(HomePagerContent.DataBean item) {
        LogUtil.d(this, "pager click ==> " + item.getTitle());
        handleItem(item);
    }
}
