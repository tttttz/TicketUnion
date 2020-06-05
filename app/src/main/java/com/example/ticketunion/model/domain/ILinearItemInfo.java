package com.example.ticketunion.model.domain;

import com.example.ticketunion.base.IBaseInfo;

/**
 * @ProjectName: TicketUnion
 * @Author: Tz
 * @CreateDate: 2020/6/5 15:27
 * God bless my code!
 */
public interface ILinearItemInfo extends IBaseInfo {

    String getFinalPrice();

    long getCouponAmount();

    long getVolume();

}
