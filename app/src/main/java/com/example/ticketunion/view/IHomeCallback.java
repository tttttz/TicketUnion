package com.example.ticketunion.view;

import com.example.ticketunion.model.domain.Categories;

/**
 * @ProjectName: TicketUnion
 * @Author: Tz
 * @CreateDate: 2020/5/18 16:30
 * God bless my code!
 */
public interface IHomeCallback {
    void onCategoriesLoaded(Categories categories);
    void onNetWorkError();
    void onLoading();
    void onEmpty();
}
