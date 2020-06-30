package com.liam.ratelimiter.rule;

public class ApiLimit {
    private static final int DEFAULT_TIME_UNIT = 1; //second

    private String api;
    private int limit;
    private int unit = DEFAULT_TIME_UNIT;

    public ApiLimit() {
    }

    public ApiLimit(String api, int limit) {
        this(api,limit,DEFAULT_TIME_UNIT);
    }

    public ApiLimit(String api, int limit, int unit) {
        this.api = api;
        this.limit = limit;
        this.unit = unit;
    }

    public int getLimit() {
        return 0;
    }

    public String getApi() {
        return null;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getUnit() {
        return unit;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return "LiamApiLimit{" +
                "api='" + api + '\'' +
                ", limit=" + limit +
                ", unit=" + unit +
                '}';
    }
}
