package com.liam.ratelimiter.rule.datasource;


import com.liam.ratelimiter.rule.RuleConfig;
import com.liam.ratelimiter.rule.parser.IRuleConfigParser;
import com.liam.ratelimiter.rule.parser.JsonRuleConfigParser;
import com.liam.ratelimiter.rule.parser.YamlRuleConfigParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class FileRuleConfigSource implements IRuleConfigSource {
    private static final Logger logger = LoggerFactory.getLogger(FileRuleConfigSource.class);

    private static final String API_LIMIT_CONFIG_NAME = "ratelimiter-rule";
    private static final String YAML_EXTENSION = "yaml";
    private static final String YML_EXTENSION = "yml";
    private static final String JSON_EXTENSION = "json";

    private static final String[] SUPPORT_EXTENSIONS = new String[]{YAML_EXTENSION, YML_EXTENSION, JSON_EXTENSION};

    private static final Map<String, IRuleConfigParser> cachedParsers = new HashMap<>();
    static {
        cachedParsers.put(YAML_EXTENSION, new YamlRuleConfigParser());
        cachedParsers.put(YML_EXTENSION, new YamlRuleConfigParser());
        cachedParsers.put(JSON_EXTENSION, new JsonRuleConfigParser());
    }

    @Override
    public RuleConfig load() {
        //1. 将限流规则配置文件ratelimiter-rule.yaml中的内容读取到RuleConfig中
        for (String extension : SUPPORT_EXTENSIONS){//我只支持n种，那么我就循环n次，看能否找到我支持的配置文件
            InputStream resourceAsStream = null;
            RuleConfig ruleConfig = null;
            try{
                resourceAsStream = this.getClass().getResourceAsStream("/" + API_LIMIT_CONFIG_NAME +  "." + extension);
                if (resourceAsStream != null){
                    IRuleConfigParser parser = cachedParsers.get(extension);
                    return parser.parse(resourceAsStream);
                }
            }finally {
                if(resourceAsStream != null){
                    try {
                        resourceAsStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        logger.error("close file error:{}" , e);
                    }
                }
            }
        }

        return null;
    }
}
