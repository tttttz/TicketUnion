package com.example.ticketunion.utils;

import android.widget.Toast;

import com.example.ticketunion.base.BaseApplication;

/**
 * @ProjectName: TicketUnion
 * @Author: Tz
 * @CreateDate: 2020/5/24 14:47
 * God bless my code!
 */
public class ToastUtil {

    private static Toast sToast;

    public static void showToast(String tips) {
        if (sToast == null) {
            sToast = Toast.makeText(BaseApplication.getAppContext(), tips, Toast.LENGTH_SHORT);
        } else {
            sToast.setText(tips);
        }
        sToast.show();
    }

}
