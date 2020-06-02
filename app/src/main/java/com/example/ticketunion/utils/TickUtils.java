package com.example.ticketunion.utils;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.example.ticketunion.base.IBaseInfo;
import com.example.ticketunion.presenter.impl.TicketPresentImpl;
import com.example.ticketunion.ui.activity.TicketActivity;

/**
 * @ProjectName: TicketUnion
 * @Author: Tz
 * @CreateDate: 2020/6/2 15:13
 * God bless my code!
 */
public class TickUtils {

    public static void toTickPage(Context context, IBaseInfo baseInfo){
        //内容被点击了
        //拿到presenter加载数据
        String title = baseInfo.getTitle();
        //这个是优惠券的地址
        String url = baseInfo.getUrl();
        if (TextUtils.isEmpty(url)) {
            //若没有优惠券了,则使用详情地址
            url = baseInfo.getUrl();
        }
        String cover = baseInfo.getCover();
        TicketPresentImpl ticketPresent = PresenterManager.getInstance().getTicketPresent();
        ticketPresent.getTicket(title, url, cover);
        context.startActivity(new Intent(context, TicketActivity.class));
    }

}
