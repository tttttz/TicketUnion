package com.example.ticketunion.view;

import com.example.ticketunion.base.IBaseCallback;
import com.example.ticketunion.model.domain.TicketResult;

/**
 * @ProjectName: TicketUnion
 * @Author: Tz
 * @CreateDate: 2020/5/26 16:12
 * God bless my code!
 */
public interface ITicketPagerCallback extends IBaseCallback {

    /**
     * 淘口令加载结果
     * @param cover
     * @param result
     */
    void onTicketLoaded(String cover, TicketResult result);

}
