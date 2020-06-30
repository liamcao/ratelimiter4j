package com.liam.ratelimiter.rule;

import java.util.List;

public class RuleConfig {
    private List<AppConfig> configs;

    public List<AppConfig> getConfigs() {
        return configs;
    }

    public void setConfigs(List<AppConfig> configs){
        this.configs = configs;
    }

    public static class AppConfig{
        private String appId;
        private List<ApiLimit> limits;

        public AppConfig() {
        }

        public AppConfig(String appId, List<ApiLimit> limits) {
            this.appId = appId;
            this.limits = limits;
        }

        public String getAppId() {
            return appId;
        }

        public void setAppId(String appId) {
            this.appId = appId;
        }

        public List<ApiLimit> getLimits() {
            return limits;
        }

        public void setLimits(List<ApiLimit> limits) {
            this.limits = limits;
        }

        @Override
        public String toString() {
            return "AppConfig{" +
                    "appId='" + appId + '\'' +
                    ", limits=" + limits +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "LiamRuleConfig{" +
                "configs=" + configs +
                '}';
    }
}
