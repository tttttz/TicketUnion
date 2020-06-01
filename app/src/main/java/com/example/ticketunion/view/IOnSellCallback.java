package com.example.ticketunion.view;

import com.example.ticketunion.base.IBaseCallback;
import com.example.ticketunion.model.domain.OnSellContent;

/**
 * @ProjectName: TicketUnion
 * @Author: Tz
 * @CreateDate: 2020/6/1 15:30
 * God bless my code!
 */
public interface IOnSellCallback extends IBaseCallback {

    /**
     * 特惠内容加载完成
     *
     * @param result
     */
    void onContentLoadSuccess(OnSellContent result);

    /**
     * 加载更多结果
     *
     * @param result
     */
    void onLoadMoreLoaded(OnSellContent result);

    /**
     * 加载更多失败
     */
    void onLoadMoreError();

    /**
     * 加载更多结果为空
     */
    void onLoadMoreEmpty();
}
