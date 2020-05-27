package com.example.ticketunion.model;

import com.example.ticketunion.model.domain.Categories;
import com.example.ticketunion.model.domain.HomePagerContent;
import com.example.ticketunion.model.domain.TicketParams;
import com.example.ticketunion.model.domain.TicketResult;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * @ProjectName: TicketUnion
 * @Author: Tz
 * @CreateDate: 2020/5/18 17:08
 * God bless my code!
 */
public interface Api {

    @GET("discovery/categories")
    Call<Categories> getCategories();

    @GET
    Call<HomePagerContent> getHomePagerContent(@Url String url);

    @POST("tpwd")
    Call<TicketResult> getTicket(@Body TicketParams ticketParams);
}
