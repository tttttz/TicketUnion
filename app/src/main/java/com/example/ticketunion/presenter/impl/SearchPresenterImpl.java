package com.example.ticketunion.presenter.impl;

import com.example.ticketunion.model.Api;
import com.example.ticketunion.model.domain.Histories;
import com.example.ticketunion.model.domain.SearchRecommend;
import com.example.ticketunion.model.domain.SearchResult;
import com.example.ticketunion.presenter.ISearchPresenter;
import com.example.ticketunion.utils.JsonCacheUtil;
import com.example.ticketunion.utils.LogUtil;
import com.example.ticketunion.utils.RetrofitManager;
import com.example.ticketunion.view.ISearchCallback;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * @ProjectName: TicketUnion
 * @Author: Tz
 * @CreateDate: 2020/6/2 18:00
 * God bless my code!
 */
public class SearchPresenterImpl implements ISearchPresenter {

    private static final int DEFAULT_HISTORIES_SIZE = 10;
    private final Api mApi;
    private ISearchCallback mCallback = null;
    public static final int DEFAULT_PAGE = 0;
    private int mCurrentPage = DEFAULT_PAGE;
    private String mCurrentKeyword = null;
    private final JsonCacheUtil mJsonCacheUtil;

    public SearchPresenterImpl() {
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        mApi = retrofit.create(Api.class);
        mJsonCacheUtil = JsonCacheUtil.getInstance();
    }

    @Override
    public void getHistory() {
        Histories histories = mJsonCacheUtil.getValue(KEY_HISTORIES, Histories.class);
        if (mCallback != null && histories != null && histories.getHistories() != null && histories.getHistories().size() != 0) {
            mCallback.onHistoriesLoaded(histories.getHistories());
        }
    }

    @Override
    public void delHistory() {
        mJsonCacheUtil.delCache(KEY_HISTORIES);
    }

    private int mHistoriesMaxSize = DEFAULT_HISTORIES_SIZE;
    public static final String KEY_HISTORIES = "key_histories";

    /**
     * 添加历史记录
     */
    private void saveHistory(String history) {
        Histories histories = mJsonCacheUtil.getValue(KEY_HISTORIES, Histories.class);
        List<String> historiesList = null;
        if (histories != null && histories.getHistories() != null) {
            historiesList = histories.getHistories();
            if (historiesList.contains(history)) {
                historiesList.remove(history);
            }
        }
        if (historiesList == null) {
            historiesList = new ArrayList<>();
        }
        if (histories == null) {
            histories = new Histories();
        }
        if (historiesList.size() > mHistoriesMaxSize) {
            historiesList = historiesList.subList(0, mHistoriesMaxSize);
        }
        historiesList.add(history);
        //此处和原代码不一样
        histories.setHistories(historiesList);
        mJsonCacheUtil.saveCache(KEY_HISTORIES, histories);
    }

    @Override
    public void doSearch(String keyword) {
        if (mCurrentKeyword == null || !mCurrentKeyword.equals(keyword)) {
            this.saveHistory(keyword);
            this.mCurrentKeyword = keyword;
        }
        if (mCallback != null) {
            mCallback.onLoading();
        }
        Call<SearchResult> task = mApi.doSearch(mCurrentPage, keyword);
        task.enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                int code = response.code();
                LogUtil.d(SearchPresenterImpl.class, "search code ==> " + code);
                if (code == HttpURLConnection.HTTP_OK) {
                    handleSearchResult(response.body());
                } else {
                    onError();
                }
            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
                onError();
            }
        });
    }

    private void onError() {
        if (mCallback != null) {
            mCallback.onError();
        }
    }

    private void handleSearchResult(SearchResult result) {
        if (mCallback != null) {
            if (isResultEmpty(result)) {
                //数据为空
                mCallback.onEmpty();
            } else {
                mCallback.onSearchSuccess(result);
            }
        }
    }

    private boolean isResultEmpty(SearchResult result) {
        try {
            return result == null || result.getData().getTbk_dg_material_optional_response().getResult_list().getMap_data().size() == 0;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void research() {
        if (mCurrentKeyword == null || mCurrentKeyword.equals("")) {
            if (mCallback != null) {
                mCallback.onEmpty();
            }
        } else {
            this.doSearch(mCurrentKeyword);
        }
    }


    @Override
    public void loaderMore() {
        mCurrentPage++;
        if (mCurrentKeyword == null) {
            if (mCallback != null) {
                mCallback.onEmpty();
            }
        } else {
            doSearchMore();
        }
    }

    /**
     * 加载更多的数据
     */
    private void doSearchMore() {
        Call<SearchResult> task = mApi.doSearch(mCurrentPage, mCurrentKeyword);
        task.enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                int code = response.code();
                LogUtil.d(SearchPresenterImpl.class, "search more code ==> " + code);
                if (code == HttpURLConnection.HTTP_OK) {
                    handleMoreSearchResult(response.body());
                } else {
                    onLoadMoreError();
                }
            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
                onLoadMoreError();
            }
        });
    }

    /**
     * 处理加载更多结果
     * @param result
     */
    private void handleMoreSearchResult(SearchResult result) {
        if (mCallback != null) {
            if (isResultEmpty(result)) {
                //数据为空
                mCallback.onMoreLoadedEmpty();
            } else {
                mCallback.onMoreLoaded(result);
            }
        }
    }

    private void onLoadMoreError() {
        mCurrentPage--;
        if (mCallback != null) {
            mCallback.onMoreLoadedError();
        }
    }

    @Override
    public void getRecommendWords() {
        Call<SearchRecommend> task = mApi.getRecommend();
        task.enqueue(new Callback<SearchRecommend>() {
            @Override
            public void onResponse(Call<SearchRecommend> call, Response<SearchRecommend> response) {
                int code = response.code();
                LogUtil.d(SearchPresenterImpl.class, "recommend code ==> " + code);
                if (code == HttpURLConnection.HTTP_OK) {
                    SearchRecommend result = response.body();
                    if (mCallback != null) {
                        mCallback.onRecommendWordsLoaded(result.getData());
                    }
                } else {

                }
            }

            @Override
            public void onFailure(Call<SearchRecommend> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void registerViewCallback(ISearchCallback callback) {
        this.mCallback = callback;
    }

    @Override
    public void unregisterViewCallback(ISearchCallback callback) {
        this.mCallback = null;
    }
}
