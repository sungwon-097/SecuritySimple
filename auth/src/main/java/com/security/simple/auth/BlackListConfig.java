package com.security.simple.auth;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class BlackListConfig {

    @Bean
    @ConditionalOnMissingBean(BlackList.class)
    public BlackList defaultBlackList() {
        return new BlackList() {

            private final List<String> blackList = new ArrayList<>();

            @Override
            public void addBlackList(String username) {
                blackList.add(username);
            }

            @Override
            public void removeBlackList(String username) {
                blackList.remove(username);
            }

            @Override
            public boolean isBlackList(String username) {
                return blackList.contains(username);
            }

            @Override
            public void clear() {
                blackList.clear();
            }
        };
    }
}
