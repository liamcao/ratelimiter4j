package com.liam.ratelimiter.rule.parser;

import com.liam.ratelimiter.rule.RuleConfig;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;

public class YamlRuleConfigParser implements IRuleConfigParser {
    @Override
    public RuleConfig parse(InputStream resourceAsStream) {
        Yaml yaml = new Yaml();
        return yaml.loadAs(resourceAsStream, RuleConfig.class);
    }

    @Override
    public RuleConfig parse(String configText) {
        return null;
    }
}
