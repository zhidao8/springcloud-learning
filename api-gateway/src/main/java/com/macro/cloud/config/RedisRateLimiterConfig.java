package com.macro.cloud.config;

import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.factory.RequestRateLimiterGatewayFilterFactory;
import org.springframework.cloud.gateway.filter.ratelimit.AbstractRateLimiter;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RateLimiter;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Configuration
public class RedisRateLimiterConfig {
//    @Bean
//    KeyResolver userKeyResolver() {
//        return exchange -> Mono.just(exchange.getRequest().getQueryParams().getFirst("username"));
//    }

    @Bean
    public KeyResolver ipKeyResolver() {
        return exchange -> Mono.just(exchange.getRequest().getRemoteAddress().getHostName());
    }

    @Bean
    public RateLimiter rateLimiter() {
        GatewayFilterSpec.RequestRateLimiterSpec requestRateLimiterSpec =
                new GatewayFilterSpec.RequestRateLimiterSpec(new RequestRateLimiterGatewayFilterFactory(
                new RedisRateLimiter(0, 0), ipKeyResolver()
        ));

        new AbstractRateLimiter<Object>() {

            @Override
            public Mono<Response> isAllowed(String routeId, String id) {
                return null;
            }
        };
        return exchange -> Mono.just(exchange.getRequest().getRemoteAddress().getHostName());
    }
}
