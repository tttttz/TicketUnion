package com.example.ticketunion.presenter.impl;

import com.example.ticketunion.model.Api;
import com.example.ticketunion.model.domain.SelectedContent;
import com.example.ticketunion.model.domain.SelectedPageCategory;
import com.example.ticketunion.presenter.ISelectedPagePresenter;
import com.example.ticketunion.utils.LogUtil;
import com.example.ticketunion.utils.RetrofitManager;
import com.example.ticketunion.utils.UrlUtils;
import com.example.ticketunion.view.ISelectedPageCallback;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * @ProjectName: TicketUnion
 * @Author: Tz
 * @CreateDate: 2020/5/28 17:21
 * God bless my code!
 */
public class SelectedPagePresenterImpl implements ISelectedPagePresenter {


    private ISelectedPageCallback mCallback = null;
    private final Api mApi;
    private SelectedPageCategory.DataBean mCurrentCategoryItem = null;

    public SelectedPagePresenterImpl(){
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        mApi = retrofit.create(Api.class);
    }

    @Override
    public void getCategories() {
        if (mCallback != null) {
            mCallback.onLoading();
        }
        Call<SelectedPageCategory> task = mApi.getSelectedCategory();
        task.enqueue(new Callback<SelectedPageCategory>() {
            @Override
            public void onResponse(Call<SelectedPageCategory> call, Response<SelectedPageCategory> response) {
                int resultCode = response.code();
                LogUtil.d(SelectedPagePresenterImpl.this, "result code ==> " + resultCode);
                if (resultCode == HttpURLConnection.HTTP_OK) {
                    SelectedPageCategory result = response.body();
                    //TODO:通知UI更新
                    if (mCallback != null) {
                        mCallback.onCategoriesLoaded(result);
                    }
                } else {
                    onLoadError();
                }
            }

            @Override
            public void onFailure(Call<SelectedPageCategory> call, Throwable t) {
                onLoadError();
            }
        });
    }

    /**
     * 加载失败
     */
    private void onLoadError() {
        if (mCallback != null) {
            mCallback.onError();
        }
    }

    @Override
    public void getContentByCategory(SelectedPageCategory.DataBean item) {
        this.mCurrentCategoryItem = item;
        int categoryId = item.getFavorites_id();
        LogUtil.d(this, "categoryId ==> " + categoryId);
        String targetUrl = UrlUtils.getSelectedPageContentUrl(categoryId);

        Call<SelectedContent> task = mApi.getSelectedPageContent(targetUrl);
        task.enqueue(new Callback<SelectedContent>() {
            @Override
            public void onResponse(Call<SelectedContent> call, Response<SelectedContent> response) {
                int resultCode = response.code();
                LogUtil.d(SelectedPagePresenterImpl.this, "result code ==> " + resultCode);
                if (resultCode == HttpURLConnection.HTTP_OK) {
                    SelectedContent result = response.body();
                    if (mCallback != null) {
                        mCallback.onContentLoaded(result);
                    }
                } else {
                    onLoadError();
                }
            }

            @Override
            public void onFailure(Call<SelectedContent> call, Throwable t) {
                onLoadError();
            }
        });
    }

    @Override
    public void reloadContent() {
        this.getCategories();
    }

    @Override
    public void registerViewCallback(ISelectedPageCallback callback) {
        this.mCallback = callback;
    }

    @Override
    public void unregisterViewCallback(ISelectedPageCallback callback) {
        this.mCallback = null;
    }

}
