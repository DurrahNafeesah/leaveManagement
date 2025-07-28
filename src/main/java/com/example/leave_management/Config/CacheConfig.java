package com.example.leave_management.Config;

import com.example.leave_management.enums.CacheName;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.stream.Collectors;

@Configuration
public class CacheConfig {

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager(
//                CacheName.LEAVE.name(),
//                CacheName.LEAVE_DETAILS.name(),
//                CacheName.ALL_LEAVES.name(),
//                CacheName.USERS.name(),
//                CacheName.DEPARTMENTS.name()
//        );
                Arrays.stream(CacheName.values())
                        .map(Enum::name)
                        .collect(Collectors.toList())
                        .toArray(new String[0])
        );
    }
}
