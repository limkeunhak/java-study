package prob1_ratelimitier;

import java.time.Duration;
import java.time.LocalDateTime;

public class ClientBucket {
    private double tokens;
    private final int capacity;
    private final double refillRatePerSec;
    private long lastRefillNanos;

    ClientBucket(int capacity, double refillRatePerSec) {
        this.capacity = capacity;
        this.tokens = capacity;
        this.refillRatePerSec = refillRatePerSec;
        this.lastRefillNanos = System.nanoTime();
    }

    public synchronized boolean allow(int requestedTokens) {
        long nowNanos = System.nanoTime();
        double elapsedSeconds = (nowNanos - this.lastRefillNanos) / 1_000_000_000.0;

        // 1. 경과 시간에 따른 토큰 충전 (Capacity 상한 적용)
        this.tokens = Math.min(this.capacity, this.tokens + (elapsedSeconds * this.refillRatePerSec));
        this.lastRefillNanos = nowNanos;

        // 2. 토큰 보유량 검증 및 차감
        if (this.tokens < requestedTokens) {
            return false;
        }

        this.tokens -= requestedTokens;
        return true;
    }
}
