package com.example.ticketunion.view;

import com.example.ticketunion.base.IBaseCallback;
import com.example.ticketunion.model.domain.SelectedContent;
import com.example.ticketunion.model.domain.SelectedPageCategory;

/**
 * @ProjectName: TicketUnion
 * @Author: Tz
 * @CreateDate: 2020/5/28 17:22
 * God bless my code!
 */
public interface ISelectedPageCallback extends IBaseCallback {

    /**
     * 分类内容结果
     * @param categories
     */
    void onCategoriesLoaded(SelectedPageCategory categories);

    /**
     * 内容
     * @param content
     */
    void onContentLoaded(SelectedContent content);
}
