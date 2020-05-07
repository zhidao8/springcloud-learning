package com.demo.gateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.AbstractRateLimiter;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class FrequencyRateLimiter extends AbstractRateLimiter<FrequencyRateLimiter.Config> {

    public static final String REMAINING_HEADER = "X-RateLimit-Remaining";
    public static final String CONFIGURATION_PROPERTY_NAME = "frequency-rate-limiter";

    private final Map<String, RateLimitRemaining> rateLimitRemainingMap = new HashMap<>();

    public FrequencyRateLimiter() {
        super(Config.class, CONFIGURATION_PROPERTY_NAME, null);
    }

    @Override
    public Mono<Response> isAllowed(String routeId, String id) {
        Config config = getConfig().get(routeId);
        if (config == null) {
            return Mono.just(new Response(true, Collections.singletonMap(REMAINING_HEADER, "unlimited")));
        }

        long currentTime = new Date().getTime();
        RateLimitRemaining rateLimitRemaining = rateLimitRemainingMap.get(id);
        if (rateLimitRemaining == null) {
            RateLimitRemaining newRateLimitRemaining = new RateLimitRemaining();
            newRateLimitRemaining.startTime = currentTime;
            newRateLimitRemaining.remainingTimes = config.times;
            rateLimitRemaining = newRateLimitRemaining;
            rateLimitRemainingMap.put(id, rateLimitRemaining);
        }

        if (currentTime - rateLimitRemaining.startTime > config.period) {
            rateLimitRemaining.startTime = currentTime;
            rateLimitRemaining.remainingTimes = config.times;
        }

        if (rateLimitRemaining.remainingTimes == 0) {
            return Mono.just(new Response(false, Collections.singletonMap(REMAINING_HEADER, "0")));
        }

        --rateLimitRemaining.remainingTimes;

        return Mono.just(new Response(true, Collections.singletonMap(REMAINING_HEADER, String.valueOf(rateLimitRemaining.remainingTimes))));
    }

    private static class RateLimitRemaining {
        long startTime;
        long remainingTimes;
    }

    public static class Config {

        private long period;

        private long times;

        public long getPeriod() {
            return period;
        }

        public void setPeriod(long period) {
            this.period = period;
        }

        public long getTimes() {
            return times;
        }

        public void setTimes(long times) {
            this.times = times;
        }
    }
}
