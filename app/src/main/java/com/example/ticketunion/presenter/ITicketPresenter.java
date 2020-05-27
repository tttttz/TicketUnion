package com.example.ticketunion.presenter;

import com.example.ticketunion.base.IBasePresenter;
import com.example.ticketunion.view.ITicketPagerCallback;

/**
 * @ProjectName: TicketUnion
 * @Author: Tz
 * @CreateDate: 2020/5/26 16:11
 * God bless my code!
 */
public interface ITicketPresenter extends IBasePresenter<ITicketPagerCallback> {

    /**
     * 生成淘口令
     * @param title
     * @param url
     * @param cover
     */
    void getTicket(String title, String url, String cover);

}
