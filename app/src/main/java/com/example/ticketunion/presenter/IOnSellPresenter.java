package com.example.ticketunion.presenter;

import com.example.ticketunion.base.IBasePresenter;
import com.example.ticketunion.view.IOnSellCallback;

/**
 * @ProjectName: TicketUnion
 * @Author: Tz
 * @CreateDate: 2020/6/1 15:29
 * God bless my code!
 */
public interface IOnSellPresenter extends IBasePresenter<IOnSellCallback> {

    /**
     * 加载特惠内容
     */
    void getSellContent();

    /**
     * 网络错误时重新加载内容
     */
    void reload();

    /**
     * 加载更多
     */
    void loadMore();
}
