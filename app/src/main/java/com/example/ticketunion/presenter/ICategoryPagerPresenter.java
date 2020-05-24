package com.example.ticketunion.presenter;

import com.example.ticketunion.base.IBasePresenter;
import com.example.ticketunion.view.ICategoryPagerCallback;

/**
 * @ProjectName: TicketUnion
 * @Author: Tz
 * @CreateDate: 2020/5/19 19:08
 * God bless my code!
 */
public interface ICategoryPagerPresenter extends IBasePresenter<ICategoryPagerCallback> {

    void getContentByCategoryId(int categoryId);

    void loadMore(int categoryId);

    void reload(int categoryId);



}
