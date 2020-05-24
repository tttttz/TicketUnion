package com.example.ticketunion.presenter.impl;

import com.example.ticketunion.model.Api;
import com.example.ticketunion.model.domain.HomePagerContent;
import com.example.ticketunion.presenter.ICategoryPagerPresenter;
import com.example.ticketunion.utils.LogUtil;
import com.example.ticketunion.utils.RetrofitManager;
import com.example.ticketunion.utils.UrlUtils;
import com.example.ticketunion.view.ICategoryPagerCallback;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * @ProjectName: TicketUnion
 * @Author: Tz
 * @CreateDate: 2020/5/19 19:51
 * God bless my code!
 */
public class CategoryPagePresenterImpl implements ICategoryPagerPresenter {


    //key是categoryId，value是page
    private Map<Integer, Integer> pagesInfo = new HashMap<>();
    //默认页数
    private static final Integer DEFAULT_PAGE = 1;

    private List<ICategoryPagerCallback> mCallbacks = new ArrayList<>();

    private static CategoryPagePresenterImpl sInstance = null;
    private Integer mCurrentPage;

    /**
     * 单例
     *
     * @return
     */
    public static CategoryPagePresenterImpl getInstance() {
        if (sInstance == null) {
            sInstance = new CategoryPagePresenterImpl();
        }
        return sInstance;
    }

    private CategoryPagePresenterImpl() {

    }

    /**
     * 根据id获取内容
     *
     * @param categoryId
     */
    @Override
    public void getContentByCategoryId(int categoryId) {
        //显示loading
        handleLoading(categoryId);

        Integer targetPage = pagesInfo.get(categoryId);
        if (targetPage == null) {
            targetPage = DEFAULT_PAGE;
            pagesInfo.put(categoryId, targetPage);
        }
        Call<HomePagerContent> task = createTask(categoryId, targetPage);
        task.enqueue(new Callback<HomePagerContent>() {
            @Override
            public void onResponse(Call<HomePagerContent> call, Response<HomePagerContent> response) {
                int code = response.code();
                LogUtil.d(CategoryPagePresenterImpl.this, "content code ==> " + code);
                if (code == HttpURLConnection.HTTP_OK) {
                    HomePagerContent pagerContent = response.body();
                    LogUtil.d(CategoryPagePresenterImpl.this, "pageContent == >" + pagerContent);
                    handleHomePagerContentResult(pagerContent, categoryId);
                } else {
                    handleNetworkError(categoryId);
                }
            }

            @Override
            public void onFailure(Call<HomePagerContent> call, Throwable t) {
                LogUtil.d(CategoryPagePresenterImpl.this, "onFailure ==> " + t.toString());
                handleNetworkError(categoryId);
            }
        });
    }

    private Call<HomePagerContent> createTask(int categoryId, Integer targetPage) {
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        String homePagerUrl = UrlUtils.createHomePagerUrl(categoryId, targetPage);
        return api.getHomePagerContent(homePagerUrl);
    }

    /**
     * 处理loading页面
     *
     * @param categoryId
     */
    private void handleLoading(int categoryId) {
        for (ICategoryPagerCallback callback : mCallbacks) {
            if (callback.getCategoryId() == categoryId) {
                callback.onLoading();
            }
        }
    }

    private void handleNetworkError(int categoryId) {
        for (ICategoryPagerCallback callback : mCallbacks) {
            if (callback.getCategoryId() == categoryId) {
                callback.onError();
            }
        }
    }

    /**
     * 处理获取到的数据给UI页面
     *
     * @param pagerContent
     * @param categoryId
     */
    private void handleHomePagerContentResult(HomePagerContent pagerContent, int categoryId) {
        List<HomePagerContent.DataBean> data = pagerContent.getData();
        for (ICategoryPagerCallback callback : mCallbacks) {
            if (callback.getCategoryId() == categoryId) {
                if (pagerContent == null || data.size() == 0) {
                    callback.onEmpty();
                } else {
                    List<HomePagerContent.DataBean> looperData = data.subList(data.size() - 5, data.size());
                    callback.onLooperListLoaded(looperData);
                    callback.onContentLoaded(data);
                }
            }
        }
    }

    @Override
    public void loadMore(int categoryId) {
        //加载更多数据
        //1.拿到当前页面
        mCurrentPage = pagesInfo.get(categoryId);
        if (mCurrentPage == null) {
            mCurrentPage = 1;
        }
        //2.页码++
        mCurrentPage++;
        //3.加载数据
        Call<HomePagerContent> task = createTask(categoryId, mCurrentPage);
        //4.处理数据结果
        task.enqueue(new Callback<HomePagerContent>() {
            @Override
            public void onResponse(Call<HomePagerContent> call, Response<HomePagerContent> response) {
                int code = response.code();
                LogUtil.d(CategoryPagePresenterImpl.this, "code ==> " + code);
                if (code == HttpURLConnection.HTTP_OK) {
                    HomePagerContent result = response.body();
                    LogUtil.d(CategoryPagePresenterImpl.this, "load more result ==> " + result.toString());
                    handleLoaderResult(result, categoryId);
                } else {
                    handleNetworkError(categoryId);
                }
            }

            @Override
            public void onFailure(Call<HomePagerContent> call, Throwable t) {
                LogUtil.d(CategoryPagePresenterImpl.this, t.toString());
                handleLoadMoreError(categoryId);
            }
        });
    }

    /**
     * 处理加载数据结果
     * @param result
     * @param categoryId
     */
    private void handleLoaderResult(HomePagerContent result, int categoryId) {
        for (ICategoryPagerCallback callback : mCallbacks) {
            if (callback.getCategoryId() == categoryId) {
                if (result == null || result.getData().size() == 0) {
                    callback.onLoadMoreEmpty();
                } else {
                    callback.onLoadMoreLoaded(result.getData());
                    pagesInfo.put(categoryId, mCurrentPage);
                }
            }
        }
    }

    private void handleLoadMoreError(int categoryId) {
//        mCurrentPage--;
//        pagesInfo.put(categoryId, mCurrentPage);
        for (ICategoryPagerCallback callback : mCallbacks) {
            if (callback.getCategoryId() == categoryId) {
                callback.onLoadMoreError();
            }
        }
    }

    @Override
    public void reload(int categoryId) {

    }


    @Override
    public void registerViewCallback(ICategoryPagerCallback callback) {
        if (!mCallbacks.contains(callback)) {
            mCallbacks.add(callback);
        }
    }

    @Override
    public void unregisterViewCallback(ICategoryPagerCallback callback) {
        mCallbacks.remove(callback);
    }
}
