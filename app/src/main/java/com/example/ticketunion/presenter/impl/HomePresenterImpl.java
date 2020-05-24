package com.example.ticketunion.presenter.impl;

import com.example.ticketunion.model.Api;
import com.example.ticketunion.model.domain.Categories;
import com.example.ticketunion.presenter.IHomePresenter;
import com.example.ticketunion.utils.LogUtil;
import com.example.ticketunion.utils.RetrofitManager;
import com.example.ticketunion.view.IHomeCallback;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * @ProjectName: TicketUnion
 * @Author: Tz
 * @CreateDate: 2020/5/18 16:42
 * God bless my code!
 */
public class HomePresenterImpl implements IHomePresenter {

    private IHomeCallback mCallback = null;

    @Override
    public void getCategories() {
        //调用loading界面
        if (mCallback != null) {
            mCallback.onLoading();
        }

        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        Call<Categories> task = api.getCategories();

        task.enqueue(new Callback<Categories>() {
            @Override
            public void onResponse(Call<Categories> call, Response<Categories> response) {
                //数据结果
                int code = response.code();
                LogUtil.d(HomePresenterImpl.this, "result code ==> " + code);
                if (code == HttpURLConnection.HTTP_OK) {
                    Categories categories = response.body();
                    //LogUtil.d(HomePresenterImpl.this, categories.toString());
                    if (mCallback != null) {
                        if (categories == null || categories.getData().size() == 0) {
                            mCallback.onEmpty();
                        } else {
                            //在该回调方法中调用setupState将成功页面显示出来
                            mCallback.onCategoriesLoaded(categories);
                        }
                    }
                } else {
                    LogUtil.d(HomePresenterImpl.this, "请求失败......");
                    if (mCallback != null) {
                        mCallback.onError();
                    }
                }
            }

            @Override
            public void onFailure(Call<Categories> call, Throwable t) {
                //加载失败
                LogUtil.d(HomePresenterImpl.this, "请求错误......" + t);
                if (mCallback != null) {
                    mCallback.onError();
                }
            }
        });
    }

    @Override
    public void registerViewCallback(IHomeCallback callback) {
        this.mCallback = callback;
    }

    @Override
    public void unregisterViewCallback(IHomeCallback callback) {
        mCallback = null;
    }
}
