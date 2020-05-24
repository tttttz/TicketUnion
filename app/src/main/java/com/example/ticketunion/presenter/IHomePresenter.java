package com.example.ticketunion.presenter;

/**
 * @ProjectName: TicketUnion
 * @Author: Tz
 * @CreateDate: 2020/5/18 16:28
 * God bless my code!
 */


import com.example.ticketunion.base.IBasePresenter;
import com.example.ticketunion.view.IHomeCallback;

/**
 * Home主页需要实现的逻辑方法
 */
public interface IHomePresenter extends IBasePresenter<IHomeCallback> {

    /**
     * 获取商品分类
     */
    void getCategories();



}
