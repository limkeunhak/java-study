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
        if (!this.clientTokenMap.containsKey(clientId)) {
            this.clientTokenMap.put(clientId, new ClientBucket(this.capacity, this.refillRatePerSec));
        }

        return this.clientTokenMap.get(clientId).allow(tokens);
    }
}

