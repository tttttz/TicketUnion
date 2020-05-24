package com.example.ticketunion.base;

/**
 * @ProjectName: TicketUnion
 * @Author: Tz
 * @CreateDate: 2020/5/19 19:48
 * God bless my code!
 */
public interface IBasePresenter <T>{

    /**
     * 注册UI通知接口
     * @param callback
     */
    void registerViewCallback(T callback);

    /**
     * 取消注册UI通知接口
     * @param callback
     */
    void unregisterViewCallback(T callback);

}
