package com.example.ticketunion.model;

import com.example.ticketunion.model.domain.Categories;
import com.example.ticketunion.model.domain.HomePagerContent;
import com.example.ticketunion.model.domain.OnSellContent;
import com.example.ticketunion.model.domain.SearchRecommend;
import com.example.ticketunion.model.domain.SearchResult;
import com.example.ticketunion.model.domain.SelectedContent;
import com.example.ticketunion.model.domain.SelectedPageCategory;
import com.example.ticketunion.model.domain.TicketParams;
import com.example.ticketunion.model.domain.TicketResult;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
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

    @GET("recommend/categories")
    Call<SelectedPageCategory> getSelectedCategory();

    @GET
    Call<SelectedContent> getSelectedPageContent(@Url String url);

    @GET
    Call<OnSellContent> getOnSellContent(@Url String url);

    @GET("search/recommend")
    Call<SearchRecommend> getRecommend();

    @GET("search")
    Call<SearchResult> doSearch(@Query("page") int page, @Query("keyword") String keyword);
}
