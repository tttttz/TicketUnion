package com.example.ticketunion.base;

import android.app.Application;
import android.content.Context;

/**
 * @ProjectName: TicketUnion
 * @Author: Tz
 * @CreateDate: 2020/5/24 14:48
 * God bless my code!
 */
public class BaseApplication extends Application {

    private static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getBaseContext();
    }

    public static Context getAppContext() {
        return appContext;
    }
}
