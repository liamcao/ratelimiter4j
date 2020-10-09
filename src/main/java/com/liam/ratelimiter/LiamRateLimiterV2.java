package com.liam.ratelimiter;

import com.eudemon.ratelimiter.exception.InternalErrorException;
import com.liam.ratelimiter.alg.FixTimeWinRateLimitAlg;
import com.liam.ratelimiter.rule.ApiLimit;
import com.liam.ratelimiter.rule.RuleConfig;
import com.liam.ratelimiter.rule.TrieRateLimitRule;
import com.liam.ratelimiter.rule.datasource.FileRuleConfigSource;
import com.liam.ratelimiter.rule.datasource.IRuleConfigSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ConcurrentHashMap;

public class LiamRateLimiterV2 {
    private static final Logger logger = LoggerFactory.getLogger(LiamRateLimiterV2.class);
    private TrieRateLimitRule rule;
    private ConcurrentHashMap<String, FixTimeWinRateLimitAlg> algs = new ConcurrentHashMap<>();

    public LiamRateLimiterV2() {
        IRuleConfigSource configSource = new FileRuleConfigSource();
        RuleConfig ruleConfig = configSource.load();
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

        //ziji:感觉上面应该不对，因为上面当get和判null不是原子操作。应该用computeIfAbsent(algKey，k -> new FixTimeWinRateLimitAlg(apiLimit.getLimit()));

        // 判断是否限流
        return neededAlg.tryAcquire();
    }
}
