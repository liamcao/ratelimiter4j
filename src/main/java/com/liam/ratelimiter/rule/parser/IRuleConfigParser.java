package com.liam.ratelimiter.rule.parser;

import com.liam.ratelimiter.rule.RuleConfig;

import java.io.InputStream;

public interface IRuleConfigParser {
    RuleConfig parse(InputStream resourceAsStream);
    RuleConfig parse(String configText);
}
