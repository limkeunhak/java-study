package prob1_ratelimitier;

import java.util.concurrent.ConcurrentHashMap;

public class TokenBucketLimiter implements RateLimiter {
    private final double refillRatePerSec;
    private final int capacity;
    private ConcurrentHashMap<String, ClientBucket> clientTokenMap;

    public TokenBucketLimiter(double refillRatePerSec, int capacity) {
        this.refillRatePerSec = refillRatePerSec;
        this.capacity = capacity;
        this.clientTokenMap = new ConcurrentHashMap<>();
    }

    @Override
    public boolean allow(String clientId) {
        return allowN(clientId, 1);
    }

    @Override
    public boolean allowN(String clientId, int tokens) {
        if (tokens <= 0 || tokens > this.capacity) {
            return false;
        }

        return this.clientTokenMap.computeIfAbsent(clientId, k -> new ClientBucket(this.capacity, this.refillRatePerSec)).
                allow(tokens);
    }
}

