package com.example.ticketunion.ui.fragment;

import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ticketunion.R;
import com.example.ticketunion.base.BaseFragment;
import com.example.ticketunion.base.IBaseInfo;
import com.example.ticketunion.model.domain.OnSellContent;
import com.example.ticketunion.presenter.impl.OnSellPagePresenterImpl;
import com.example.ticketunion.ui.adapter.OnSellContentAdapter;
import com.example.ticketunion.utils.PresenterManager;
import com.example.ticketunion.utils.SizeUtils;
import com.example.ticketunion.utils.TickUtils;
import com.example.ticketunion.utils.ToastUtil;
import com.example.ticketunion.view.IOnSellCallback;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import butterknife.BindView;

public class OnSellFragment extends BaseFragment implements IOnSellCallback, OnSellContentAdapter.OnSellPageItemClickListener {

    private OnSellPagePresenterImpl mOnSellPagePresenter;
    public static final int DEFAULT_SPAN_COUNT = 2;

    @BindView(R.id.on_sell_content_list)
    RecyclerView mContentRv;

    @BindView(R.id.on_sell_refresh_layout)
    TwinklingRefreshLayout  mTwinklingRefreshLayout;

    //fragment bar的标题
    @BindView(R.id.fragment_bat_title_tv)
    public TextView barTitleTv;

    private OnSellContentAdapter mOnSellContentAdapter;

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_on_sell;
    }

    @Override
    protected void initView(View rootView) {
        setupState(State.SUCCESS);
        mOnSellContentAdapter = new OnSellContentAdapter();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), DEFAULT_SPAN_COUNT);
        mContentRv.setLayoutManager(gridLayoutManager);
        mContentRv.setAdapter(mOnSellContentAdapter);
        mContentRv.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = SizeUtils.dip2px(getContext(), 2.5f);
                outRect.bottom = SizeUtils.dip2px(getContext(), 2.5f);
                outRect.left = SizeUtils.dip2px(getContext(), 2.5f);
                outRect.right = SizeUtils.dip2px(getContext(), 2.5f);
            }
        });
        mTwinklingRefreshLayout.setEnableLoadmore(true);
        mTwinklingRefreshLayout.setEnableRefresh(false);
        mTwinklingRefreshLayout.setEnableOverScroll(true);
        barTitleTv.setText("特惠宝贝");
    }

    @Override
    protected void initListener() {
        mTwinklingRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                //加载更多
                if (mOnSellPagePresenter != null) {
                    mOnSellPagePresenter.loadMore();
                }
            }
        });
        mOnSellContentAdapter.setOnSellPageItemClickListener(this);
    }

    @Override
    protected void initPresenter() {
        mOnSellPagePresenter = PresenterManager.getInstance().getOnSellPagePresenter();
        mOnSellPagePresenter.registerViewCallback(this);
        mOnSellPagePresenter.getSellContent();
    }

    @Override
    protected View loadRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_with_bar_layout, container, false);
    }

    @Override
    protected void onRetryClick() {
        if (mOnSellPagePresenter != null) {
            mOnSellPagePresenter.reload();
        }
    }

    @Override
    protected void release() {
        mOnSellPagePresenter.unregisterViewCallback(this);
    }

    //=====================================================================
    //以下为callback接口方法的实现
    @Override
    public void onContentLoadSuccess(OnSellContent result) {
        setupState(State.SUCCESS);
        //数据回来
        mOnSellContentAdapter.setData(result);
    }


    @Override
    public void onLoadMoreLoaded(OnSellContent result) {
        //加载更多的回调
        mTwinklingRefreshLayout.finishLoadmore();
        //添加内容到适配器
        mOnSellContentAdapter.loadMore(result);
        int size = result.getData().getTbk_dg_optimus_material_response().getResult_list().getMap_data().size();
        ToastUtil.showToast("加载了" + size + "条数据...");
    }

    @Override
    public void onLoadMoreError() {
        mTwinklingRefreshLayout.finishLoadmore();
        ToastUtil.showToast("网络异常，请稍后重试");
    }

    @Override
    public void onLoadMoreEmpty() {
        mTwinklingRefreshLayout.finishLoadmore();
        ToastUtil.showToast("没有更多内容...");
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
    public void onSellItemClick(IBaseInfo item) {
        TickUtils.toTickPage(getContext(), item);
    }
}
