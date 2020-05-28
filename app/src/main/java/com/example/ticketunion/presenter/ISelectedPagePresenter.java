package com.example.ticketunion.presenter;

import com.example.ticketunion.base.IBasePresenter;
import com.example.ticketunion.model.domain.SelectedPageCategory;
import com.example.ticketunion.view.ISelectedPageCallback;

/**
 * @ProjectName: TicketUnion
 * @Author: Tz
 * @CreateDate: 2020/5/28 17:21
 * God bless my code!
 */
public interface ISelectedPagePresenter extends IBasePresenter<ISelectedPageCallback> {

    /**
     * 获取分类
     */
    void getCategories();

    /**
     * 根据分类获取内容
     *
     * @param item
     */
    void getContentByCategory(SelectedPageCategory.DataBean item);

    /**
     * 重新加载内容
     */
    void reloadContent();
}
