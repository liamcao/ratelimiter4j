package com.liam.ratelimiter.rule.datasource;

import com.liam.ratelimiter.rule.RuleConfig;

public interface IRuleConfigSource {
    RuleConfig load();
}
