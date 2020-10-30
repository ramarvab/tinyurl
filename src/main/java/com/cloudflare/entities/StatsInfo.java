package com.cloudflare.entities;

/**
 * To return Stats info to the user
 */
public class StatsInfo {

    private String url;
    private int accessed;
    private String days;

    public StatsInfo(String url, int accessed, String days) {
        this.url = url;
        this.accessed = accessed;
        this.days = days;
    }

    @Override
    public String toString() {
        return "StatsInfo{" +
                "url='" + url + '\'' +
                ", accessed=" + accessed +
                ", days='" + days + '\'' +
                '}';
    }
}
