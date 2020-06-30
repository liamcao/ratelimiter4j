package com.liam.ratelimiter.rule.parser;

import com.liam.ratelimiter.rule.RuleConfig;
import com.liam.ratelimiter.rule.parser.IRuleConfigParser;

import java.io.InputStream;

public class JsonRuleConfigParser implements IRuleConfigParser {
    @Override
    public RuleConfig parse(InputStream resourceAsStream) {
        return null;
    }

    @Override
    public RuleConfig parse(String configText) {
        return null;
    }
}
