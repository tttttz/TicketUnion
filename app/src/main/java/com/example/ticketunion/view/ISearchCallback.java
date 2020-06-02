package com.example.ticketunion.view;

import com.example.ticketunion.base.IBaseCallback;
import com.example.ticketunion.model.domain.SearchRecommend;
import com.example.ticketunion.model.domain.SearchResult;

import java.util.List;

/**
 * @ProjectName: TicketUnion
 * @Author: Tz
 * @CreateDate: 2020/6/2 16:06
 * God bless my code!
 */
public interface ISearchCallback extends IBaseCallback {

    /**
     * 获取历史记录
     * @param histories
     */
    void onHistoriesLoaded(List<String> histories);

    /**
     * 删除历史记录
     */
    void onHistoriesDeleted();

    /**
     * 搜索成功
     * @param result
     */
    void onSearchSuccess(SearchResult result);

    /**
     * 加载了更多内容
     * @param result
     */
    void onMoreLoaded(SearchResult result);

    /**
     * 加载更多失败
     */
    void onMoreLoadedError();

    /**
     * 加载更多内容
     */
    void onMoreLoadedEmpty();

    /**
     * 推荐词获取结果
     * @param recommendWords
     */
    void onRecommendWordsLoaded(List<SearchRecommend.DataBean> recommendWords);
}
