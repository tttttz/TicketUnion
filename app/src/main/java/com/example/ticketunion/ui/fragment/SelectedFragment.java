package com.example.ticketunion.ui.fragment;

import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ticketunion.R;
import com.example.ticketunion.base.BaseFragment;
import com.example.ticketunion.model.domain.SelectedContent;
import com.example.ticketunion.model.domain.SelectedPageCategory;
import com.example.ticketunion.presenter.ISelectedPagePresenter;
import com.example.ticketunion.ui.adapter.SelectedPageLeftAdapter;
import com.example.ticketunion.utils.LogUtil;
import com.example.ticketunion.utils.PresenterManager;
import com.example.ticketunion.view.ISelectedPageCallback;

import java.util.List;

import butterknife.BindView;

public class SelectedFragment extends BaseFragment implements ISelectedPageCallback, SelectedPageLeftAdapter.OnLeftItemClickListener {

    private ISelectedPagePresenter mSelectedPagePresenter = null;

    @BindView(R.id.left_category_list)
    public RecyclerView leftCategoryList;

    @BindView(R.id.right_content_list)
    public RecyclerView rightContentList;
    private SelectedPageLeftAdapter mLeftAdapter;

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_selected;
    }
    @Override
    protected void initView(View rootView) {
        setupState(State.SUCCESS);
        leftCategoryList.setLayoutManager(new LinearLayoutManager(getContext()));
        mLeftAdapter = new SelectedPageLeftAdapter();
        leftCategoryList.setAdapter(mLeftAdapter);
    }

    @Override
    protected void initListener() {
        mLeftAdapter.setOnLeftItemClickListener(this);
    }

    @Override
    protected void initPresenter() {
        mSelectedPagePresenter = PresenterManager.getInstance().getSelectedPagePresenter();
        mSelectedPagePresenter.registerViewCallback(this);
        mSelectedPagePresenter.getCategories();
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
        LogUtil.d(this, "category == >" + categories);
        //更新UI,根据当前获取的目录获取数据
        mLeftAdapter.setData(categories);
        List<SelectedPageCategory.DataBean> data = categories.getData();
        mSelectedPagePresenter.getContentByCategory(data.get(0));
    }

    @Override
    public void onContentLoaded(SelectedContent content) {

    }

    @Override
    public void onError() {

    }

    @Override
    public void onLoading() {

    }

    @Override
    public void onEmpty() {

    }

    //左侧item点击事件
    @Override
    public void onLeftItemClick(SelectedPageCategory.DataBean item) {
        String favorites_title = item.getFavorites_title();
        LogUtil.d(this, "item title ==> " + favorites_title);
    }
}
