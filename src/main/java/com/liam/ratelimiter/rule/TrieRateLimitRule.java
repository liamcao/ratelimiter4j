package com.liam.ratelimiter.rule;

public class TrieRateLimitRule implements IRateLimitRule {
    public TrieRateLimitRule(RuleConfig ruleConfig) {
    }

    public ApiLimit getLimit(String appId, String url) {
        return null;
    }
}
