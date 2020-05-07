package com.macro.cloud.config;

import org.springframework.cloud.gateway.filter.ratelimit.AbstractRateLimiter;
import org.springframework.validation.Validator;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class FrequencyRateLimiter extends AbstractRateLimiter<FrequencyRateLimiter.Config> {

    public static final String RATE_LIMIT_HEADER = "X-RateLimit";

    private Map<String, RateLimitRemaining> rateLimitRemainingMap = new HashMap<>();

    protected FrequencyRateLimiter(Class<FrequencyRateLimiter.Config> configClass, String configurationPropertyName, Validator validator) {
        super(configClass, configurationPropertyName, validator);
    }

    @Override
    public Mono<Response> isAllowed(String routeId, String id) {
        Config config = getConfig().get(routeId);
        if (config == null) {
            return Mono.just(new Response(true, Collections.singletonMap(RATE_LIMIT_HEADER, "unlimited")));
        }

        RateLimitRemaining rateLimitRemaining = rateLimitRemainingMap.get(id);
        if (rateLimitRemaining == null) {

        }

        return null;
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
