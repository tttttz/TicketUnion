package com.example.ticketunion.presenter.impl;

import com.example.ticketunion.model.Api;
import com.example.ticketunion.model.domain.OnSellContent;
import com.example.ticketunion.presenter.IOnSellPresenter;
import com.example.ticketunion.utils.LogUtil;
import com.example.ticketunion.utils.RetrofitManager;
import com.example.ticketunion.utils.UrlUtils;
import com.example.ticketunion.view.IOnSellCallback;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * @ProjectName: TicketUnion
 * @Author: Tz
 * @CreateDate: 2020/6/1 15:48
 * God bless my code!
 */
public class OnSellPagePresenterImpl implements IOnSellPresenter {

    public static final int DEFAULT_PAGE = 1;
    private int mCurrentPage = DEFAULT_PAGE;
    private IOnSellCallback mCallback = null;
    private final Api mApi;

    private boolean mIsLoading = false;


    public OnSellPagePresenterImpl() {
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        mApi = retrofit.create(Api.class);
    }

    @Override
    public void getSellContent() {
        if (mIsLoading) {
            return;
        }
        mIsLoading = true;
        //通知UI状态为加载
        if (mCallback != null) {
            mCallback.onLoading();
        }

        String targetUrl = UrlUtils.getOnSellPage(mCurrentPage);
        Call<OnSellContent> task = mApi.getOnSellContent(targetUrl);
        task.enqueue(new Callback<OnSellContent>() {
            @Override
            public void onResponse(Call<OnSellContent> call, Response<OnSellContent> response) {
                mIsLoading = false;
                int code = response.code();
                LogUtil.d(this, "on sell code == > " + code);
                if (code == HttpURLConnection.HTTP_OK) {
                    OnSellContent result = response.body();
                    LogUtil.d(this, "on sell result ==> " + result);
                    onSuccess(result);
                } else {
                    onError();
                }
            }

            @Override
            public void onFailure(Call<OnSellContent> call, Throwable t) {
                onError();
            }
        });
    }
    //请求失败

    private void onError() {
        mIsLoading = false;
        if (mCallback != null) {
            mCallback.onError();
        }
    }
    //请求成功

    private void onSuccess(OnSellContent result) {
        if (mCallback != null) {
            try {
                if (isEmpty(result)) {
                    onEmpty();
                } else {
                    mCallback.onContentLoadSuccess(result);
                }
            } catch (Exception e) {
                e.printStackTrace();
                onEmpty();
            }
        }
    }

    private boolean isEmpty(OnSellContent content) {
        int size = 0;
        try {
            size = content.getData().getTbk_dg_optimus_material_response().getResult_list().getMap_data().size();
        } catch (Exception e) {
            LogUtil.d(this, "has null point");
            e.printStackTrace();
        }
        return size == 0;
    }

    private void onEmpty() {
        if (mCallback != null) {
            mCallback.onEmpty();
        }
    }

    @Override
    public void reload() {
        //重新加载
        this.getSellContent();
    }

    @Override
    public void loadMore() {
        if (mIsLoading) {
            return;
        }
        mIsLoading = true;
        //加载更多
        mCurrentPage++;
        //根据页码加载更多
        String targetUrl = UrlUtils.getOnSellPage(mCurrentPage);
        Call<OnSellContent> task = mApi.getOnSellContent(targetUrl);
        task.enqueue(new Callback<OnSellContent>() {
            @Override
            public void onResponse(Call<OnSellContent> call, Response<OnSellContent> response) {
                mIsLoading = false;
                int code = response.code();
                LogUtil.d(this, "on sell code == > " + code);
                if (code == HttpURLConnection.HTTP_OK) {
                    OnSellContent result = response.body();
                    LogUtil.d(this, "on sell result ==> " + result);
                    onLoadMore(result);
                } else {
                    onLoadMoreError();
                }
            }

            @Override
            public void onFailure(Call<OnSellContent> call, Throwable t) {
                onLoadMoreError();
            }
        });

    }

    //加载更多失败
    private void onLoadMoreError() {
        mIsLoading = false;
        mCurrentPage--;
        if (mCallback != null) {
            mCallback.onLoadMoreError();
        }
    }

    //加载更多
    private void onLoadMore(OnSellContent result) {
        if (mCallback != null) {
            if (isEmpty(result)) {
                mCurrentPage--;
                mCallback.onLoadMoreEmpty();
            } else {
                mCallback.onLoadMoreLoaded(result);
            }
        }
    }

    @Override
    public void registerViewCallback(IOnSellCallback callback) {
        this.mCallback = callback;
    }

    @Override
    public void unregisterViewCallback(IOnSellCallback callback) {
        this.mCallback = null;
    }
}
