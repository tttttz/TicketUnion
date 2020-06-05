package com.example.ticketunion.presenter;

import com.example.ticketunion.base.IBasePresenter;
import com.example.ticketunion.view.ISearchCallback;

/**
 * @ProjectName: TicketUnion
 * @Author: Tz
 * @CreateDate: 2020/6/2 16:05
 * God bless my code!
 */
public interface ISearchPresenter extends IBasePresenter<ISearchCallback> {
    /**
     * 获取搜索历史
     */
    void getHistory();

    /**
     * 删除搜索历史
     */
    void delHistory();

    /**
     * 搜索
     * @param key
     */
    void doSearch(String key);

    /**
     * 重新搜索
     */
    void reSearch();

    /**
     * 获取更多搜索结果
     */
    void loaderMore();

    /**
     * 获取推荐词
     */
    void getRecommendWords();


}
