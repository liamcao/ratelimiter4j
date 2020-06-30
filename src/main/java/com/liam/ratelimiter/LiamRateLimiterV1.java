package com.liam.ratelimiter;

import com.eudemon.ratelimiter.exception.InternalErrorException;
import com.liam.ratelimiter.alg.FixTimeWinRateLimitAlg;
import com.liam.ratelimiter.rule.ApiLimit;
import com.liam.ratelimiter.rule.TrieRateLimitRule;
import com.liam.ratelimiter.rule.RuleConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ConcurrentHashMap;

public class LiamRateLimiterV1 {
    private static final Logger logger = LoggerFactory.getLogger(LiamRateLimiterV1.class);
    private TrieRateLimitRule rule;
    private ConcurrentHashMap<String, FixTimeWinRateLimitAlg> algs = new ConcurrentHashMap<>();

    public LiamRateLimiterV1() {
        //1. 将限流规则配置文件ratelimiter-rule.yaml中的内容读取到RuleConfig中
        InputStream resourceAsStream = null;
        RuleConfig ruleConfig = null;
        try{
            resourceAsStream = this.getClass().getResourceAsStream("/ratelimiter-rule.yaml");
            if (resourceAsStream != null){
                Yaml yaml = new Yaml();
                ruleConfig = yaml.loadAs(resourceAsStream, RuleConfig.class);
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
        System.out.println(ruleConfig);
        //2. 将限流规则构建成支持快速查找的数据结构RateLimitRule
        this.rule = new TrieRateLimitRule(ruleConfig);
    }

    public boolean limit(String appId, String url) throws InternalErrorException {
        ApiLimit apiLimit = rule.getLimit(appId, url);

        //no limit for this url
        if(apiLimit == null){
            return true;
        }

        //结果是：如果放进去了就拿新new的，如果没有放进去，就拿map里面之前的那个
        String algKey = appId + ":" + apiLimit.getApi();
        FixTimeWinRateLimitAlg neededAlg = algs.get(algKey);
        if(neededAlg == null){
            neededAlg = new FixTimeWinRateLimitAlg(apiLimit.getLimit());
            FixTimeWinRateLimitAlg result = algs.putIfAbsent(algKey, neededAlg);
            if(result != null){//如果没有放进去
                neededAlg = result;
            }
        }

        // 判断是否限流
        return neededAlg.tryAcquire();
    }
}
