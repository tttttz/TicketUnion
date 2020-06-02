package com.example.ticketunion.model.domain;

/**
 * @ProjectName: TicketUnion
 * @Author: Tz
 * @CreateDate: 2020/6/2 19:42
 * God bless my code!
 */
public class CacheWithDuration {
    private long duration;
    private String cache;

    public CacheWithDuration(long duration, String cache) {
        this.duration = duration;
        this.cache = cache;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getCache() {
        return cache;
    }

    public void setCache(String cache) {
        this.cache = cache;
    }
}
