package com.example.ticketunion.ui.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ticketunion.R;
import com.example.ticketunion.base.BaseFragment;
import com.example.ticketunion.model.domain.SearchRecommend;
import com.example.ticketunion.model.domain.SearchResult;
import com.example.ticketunion.presenter.impl.SearchPresenterImpl;
import com.example.ticketunion.utils.LogUtil;
import com.example.ticketunion.utils.PresenterManager;
import com.example.ticketunion.view.ISearchCallback;

import java.util.List;

public class SearchFragment extends BaseFragment implements ISearchCallback {

    private SearchPresenterImpl mSearchPresenter;

    @Override
    protected View loadRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_search_layout, container, false);
    }

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_search;
    }
    @Override
    protected void initView(View rootView) {
        setupState(State.SUCCESS);
    }

    @Override
    protected void initPresenter() {
        mSearchPresenter = PresenterManager.getInstance().getSearchPresenter();
        mSearchPresenter.registerViewCallback(this);
        mSearchPresenter.getRecommendWords();
        mSearchPresenter.doSearch("键盘");
        mSearchPresenter.getHistory();
    }

    @Override
    protected void release() {
        if (mSearchPresenter != null) {
            mSearchPresenter.unregisterViewCallback(this);
        }
    }

    //===========================================================
    //以下为callback回调方法
    @Override
    public void onHistoriesLoaded(List<String> histories) {
        LogUtil.d(this, "history size --> " + histories.size());
    }

    @Override
    public void onHistoriesDeleted() {

    }

    @Override
    public void onSearchSuccess(SearchResult result) {
        int size = result.getData().getTbk_dg_material_optional_response().getResult_list().getMap_data().size();
        LogUtil.d(this, "result size ==> " + size);
    }

    @Override
    public void onMoreLoaded(SearchResult result) {

    }

    @Override
    public void onMoreLoadedError() {

    }

    @Override
    public void onMoreLoadedEmpty() {

    }

    @Override
    public void onRecommendWordsLoaded(List<SearchRecommend.DataBean> recommendWords) {
        LogUtil.d(this, "size == > " + recommendWords.size());
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
}
