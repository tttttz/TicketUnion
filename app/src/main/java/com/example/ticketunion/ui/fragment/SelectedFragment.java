package com.example.ticketunion.ui.fragment;

import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ticketunion.R;
import com.example.ticketunion.base.BaseFragment;
import com.example.ticketunion.base.IBaseInfo;
import com.example.ticketunion.model.domain.SelectedContent;
import com.example.ticketunion.model.domain.SelectedPageCategory;
import com.example.ticketunion.presenter.ISelectedPagePresenter;
import com.example.ticketunion.ui.adapter.SelectedPageContentAdapter;
import com.example.ticketunion.ui.adapter.SelectedPageLeftAdapter;
import com.example.ticketunion.utils.LogUtil;
import com.example.ticketunion.utils.PresenterManager;
import com.example.ticketunion.utils.SizeUtils;
import com.example.ticketunion.utils.TickUtils;
import com.example.ticketunion.view.ISelectedPageCallback;

import java.util.List;

import butterknife.BindView;

public class SelectedFragment extends BaseFragment implements ISelectedPageCallback, SelectedPageLeftAdapter.OnLeftItemClickListener, SelectedPageContentAdapter.OnSelectedPageContentItemClickListener {

    private ISelectedPagePresenter mSelectedPagePresenter = null;

    @BindView(R.id.left_category_list)
    public RecyclerView leftCategoryList;

    @BindView(R.id.right_content_list)
    public RecyclerView rightContentList;
    //fragment bar的标题
    @BindView(R.id.fragment_bat_title_tv)
    public TextView barTitleTv;


    private SelectedPageLeftAdapter mLeftAdapter;
    private SelectedPageContentAdapter mRightAdapter;

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_selected;
    }
    @Override
    protected void initView(View rootView) {
        setupState(State.SUCCESS);
        barTitleTv.setText("精选宝贝");
        //左侧RecyclerView初始化
        leftCategoryList.setLayoutManager(new LinearLayoutManager(getContext()));
        mLeftAdapter = new SelectedPageLeftAdapter();
        leftCategoryList.setAdapter(mLeftAdapter);
        //右侧RecyclerView初始化
        rightContentList.setLayoutManager(new LinearLayoutManager(getContext()));
        mRightAdapter = new SelectedPageContentAdapter();
        rightContentList.setAdapter(mRightAdapter);
        rightContentList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                int bottom = SizeUtils.dip2px(getContext(), 4);
                int left = SizeUtils.dip2px(getContext(), 6);
                outRect.bottom = bottom;
                outRect.top = bottom;
                outRect.left = left;
                outRect.right = left;
            }
        });
    }

    @Override
    protected void initListener() {
        mLeftAdapter.setOnLeftItemClickListener(this);
        mRightAdapter.setOnSelectedPageContentItemClickListener(this);
    }

    @Override
    protected void initPresenter() {
        mSelectedPagePresenter = PresenterManager.getInstance().getSelectedPagePresenter();
        mSelectedPagePresenter.registerViewCallback(this);
        mSelectedPagePresenter.getCategories();
    }

    @Override
    protected View loadRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_with_bar_layout, container, false);
    }

    @Override
    protected void release() {
        super.release();
        if (mSelectedPagePresenter != null) {
            mSelectedPagePresenter.unregisterViewCallback(this);
        }
    }

    //========================================================================
    //以下为callback接口方法的实现
    @Override
    public void onCategoriesLoaded(SelectedPageCategory categories) {
        setupState(State.SUCCESS);
        LogUtil.d(this, "category == >" + categories);
        //更新UI,根据当前获取的目录获取数据
        mLeftAdapter.setData(categories);
        List<SelectedPageCategory.DataBean> data = categories.getData();
        mSelectedPagePresenter.getContentByCategory(data.get(0));
    }

    @Override
    public void onContentLoaded(SelectedContent content) {
        mRightAdapter.setData(content);
        rightContentList.scrollToPosition(0);
    }

    @Override
    public void onError() {
        setupState(State.ERROR);
    }

    @Override
    protected void onRetryClick() {
        //网络错误重新点击
        if (mSelectedPagePresenter != null) {
            mSelectedPagePresenter.reloadContent();
        }
    }

    @Override
    public void onLoading() {
        setupState(State.LOADING);
    }

    @Override
    public void onEmpty() {

    }

    //左侧item点击事件
    @Override
    public void onLeftItemClick(SelectedPageCategory.DataBean item) {
        mRightAdapter.clearData();
        String favorites_title = item.getFavorites_title();
        mSelectedPagePresenter.getContentByCategory(item);
        LogUtil.d(this, "item title ==> " + favorites_title);
    }

    //右侧点击事件
    @Override
    public void onContentItemClick(IBaseInfo item) {
        TickUtils.toTickPage(getContext(), item);
    }
}
