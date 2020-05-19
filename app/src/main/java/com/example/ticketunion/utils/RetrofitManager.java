package com.example.ticketunion.utils;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @ProjectName: TicketUnion
 * @Author: Tz
 * @CreateDate: 2020/5/18 17:10
 * God bless my code!
 */

/**
 * Retrofit帮助类
 */
public class RetrofitManager {

    private static volatile RetrofitManager instance = null;
    private final Retrofit mRetrofit;

    public static RetrofitManager getInstance(){
        LogUtil.d(RetrofitManager.class, "ok1");
        if (instance == null) {
            synchronized (RetrofitManager.class) {
                if (instance == null) {
                    instance = new RetrofitManager();
                }
            }
        }
        return instance;
    }

    private RetrofitManager(){
        //创建Retrofit
        mRetrofit = new Retrofit.Builder()
                .baseUrl("https://api.sunofbeach.net/shop/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
    public Retrofit getRetrofit(){
        return mRetrofit;
    }
}
