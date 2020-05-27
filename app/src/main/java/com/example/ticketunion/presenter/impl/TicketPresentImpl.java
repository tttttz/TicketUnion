package com.example.ticketunion.presenter.impl;

import com.example.ticketunion.model.Api;
import com.example.ticketunion.model.domain.TicketParams;
import com.example.ticketunion.model.domain.TicketResult;
import com.example.ticketunion.presenter.ITicketPresenter;
import com.example.ticketunion.utils.LogUtil;
import com.example.ticketunion.utils.RetrofitManager;
import com.example.ticketunion.utils.UrlUtils;
import com.example.ticketunion.view.ITicketPagerCallback;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * @ProjectName: TicketUnion
 * @Author: Tz
 * @CreateDate: 2020/5/26 16:34
 * God bless my code!
 */
public class TicketPresentImpl implements ITicketPresenter {

    private String mCover;
    private TicketResult mResult;

    enum LoadState {
        LOADING, SUCCESS, ERROR, NONE
    }

    private LoadState mCurrentState = LoadState.NONE;

    private ITicketPagerCallback mCallback = null;

    @Override
    public void getTicket(String title, String url, String cover) {
        onTicketLoading();
        mCover = cover;
        LogUtil.d(this, "title ==> " + title);
        LogUtil.d(this, "url ==> " + url);
        LogUtil.d(this, "cover ==> " + cover);
        //获得的url需要前面加上https:
        String targetUrl = UrlUtils.getTicketUrl(url);
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        TicketParams ticketParams = new TicketParams(targetUrl, title);
        Call<TicketResult> task = api.getTicket(ticketParams);
        task.enqueue(new Callback<TicketResult>() {
            @Override
            public void onResponse(Call<TicketResult> call, Response<TicketResult> response) {

                int code = response.code();
                LogUtil.d(TicketPresentImpl.this, "ticket code ==> " + code);
                if (code == HttpURLConnection.HTTP_OK) {
                    mResult = response.body();
                    LogUtil.d(TicketPresentImpl.this, "result ==> " + mResult);

                    onTicketLoadSuccess();
                } else {
                    onLoadTickError();
                }
            }

            @Override
            public void onFailure(Call<TicketResult> call, Throwable t) {
                onLoadTickError();
            }
        });
    }

    private void onTicketLoadSuccess() {
        if (mCallback != null) {
            mCallback.onTicketLoaded(mCover, mResult);
        } else {
            mCurrentState = LoadState.SUCCESS;
        }
    }

    private void onLoadTickError() {
        if (mCallback != null) {
            mCallback.onError();
        } else {
            mCurrentState = LoadState.ERROR;
        }
    }

    @Override
    public void registerViewCallback(ITicketPagerCallback callback) {
        this.mCallback = callback;
        if (mCurrentState != LoadState.NONE) {
            //说明状态已经变了
            //更新UI
            if (mCurrentState == LoadState.SUCCESS) {
                onTicketLoadSuccess();
            } else if (mCurrentState == LoadState.ERROR) {
                onLoadTickError();
            } else if (mCurrentState == LoadState.LOADING) {
                onTicketLoading();
            }
        }
    }

    private void onTicketLoading() {
        if (mCallback != null) {
            mCallback.onLoading();
        } else {
            mCurrentState = LoadState.LOADING;
        }
    }

    @Override
    public void unregisterViewCallback(ITicketPagerCallback callback) {
        this.mCallback = null;
    }
}
