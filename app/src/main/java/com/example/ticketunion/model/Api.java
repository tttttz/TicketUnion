package com.example.ticketunion.model;

import com.example.ticketunion.model.domain.Categories;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * @ProjectName: TicketUnion
 * @Author: Tz
 * @CreateDate: 2020/5/18 17:08
 * God bless my code!
 */
public interface Api {

    @GET("discovery/categories")
    Call<Categories> getCategories();
}
