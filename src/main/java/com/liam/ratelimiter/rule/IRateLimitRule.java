package com.liam.ratelimiter.rule;

public interface IRateLimitRule {
     ApiLimit getLimit(String appId, String url);
}
