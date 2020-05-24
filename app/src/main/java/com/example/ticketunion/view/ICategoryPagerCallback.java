package com.example.ticketunion.view;

import com.example.ticketunion.base.IBaseCallback;
import com.example.ticketunion.model.domain.HomePagerContent;

import java.util.List;

/**
 * @ProjectName: TicketUnion
 * @Author: Tz
 * @CreateDate: 2020/5/19 19:22
 * God bless my code!
 */
public interface ICategoryPagerCallback extends IBaseCallback {

    /**
     * 数据加载回来
     *
     * @param contents
     */
    void onContentLoaded(List<HomePagerContent.DataBean> contents);


    int getCategoryId();

    /**
     * 加载更多时网络错误
     */
    void onLoadMoreError();

    /**
     * 加载更多时内容为空
     */
    void onLoadMoreEmpty();

    /**
     * 加载更多内容
     * @param contents
     */
    void onLoadMoreLoaded(List<HomePagerContent.DataBean> contents);

    /**
     * 轮播图回调
     * @param contents
     */
    void onLooperListLoaded(List<HomePagerContent.DataBean> contents);

}
