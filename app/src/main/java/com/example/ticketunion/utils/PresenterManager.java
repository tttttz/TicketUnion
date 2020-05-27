package com.example.ticketunion.utils;

import com.example.ticketunion.presenter.impl.CategoryPagePresenterImpl;
import com.example.ticketunion.presenter.impl.HomePresenterImpl;
import com.example.ticketunion.presenter.impl.TicketPresentImpl;

/**
 * @ProjectName: TicketUnion
 * @Author: Tz
 * @CreateDate: 2020/5/26 16:37
 * God bless my code!
 */
public class PresenterManager {

    private static PresenterManager ourInstance = null;
    private CategoryPagePresenterImpl mCategoryPagePresenter = null;
    private HomePresenterImpl mHomePresenter = null;
    private TicketPresentImpl mTicketPresent = null;

    public TicketPresentImpl getTicketPresent() {
        return mTicketPresent;
    }

    public HomePresenterImpl getHomePresenter() {
        return mHomePresenter;
    }

    public CategoryPagePresenterImpl getCategoryPagePresenter() {
        return mCategoryPagePresenter;
    }

    public static PresenterManager getInstance(){
        if (ourInstance == null) {
            ourInstance = new PresenterManager();
        }
        return ourInstance;
    }

    private PresenterManager(){
        mCategoryPagePresenter = new CategoryPagePresenterImpl();
        mHomePresenter = new HomePresenterImpl();
        mTicketPresent = new TicketPresentImpl();
    }

}
