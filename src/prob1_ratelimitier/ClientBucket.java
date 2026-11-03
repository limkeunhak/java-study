package prob1_ratelimitier;

import java.time.Duration;
import java.time.LocalDateTime;

public class ClientBucket {
    private int remainTokens;
    private final int capacity;
    private final double refillRatePerSec;
    private LocalDateTime lastUpdatedAt;

    ClientBucket(int capacity, double refillRatePerSec) {
        this.capacity = capacity;
        this.remainTokens = capacity;
        this.refillRatePerSec = refillRatePerSec;
        this.lastUpdatedAt = LocalDateTime.now();
    }

    public boolean allow(int tokens) {
        synchronized (this) {
            LocalDateTime now = LocalDateTime.now();

            long durationMillis = Duration.between(this.lastUpdatedAt, now).toMillis();
            double refillCounts = ((double)durationMillis/1000.0) * this.refillRatePerSec;
            if (refillCounts >= 1.0) {
                this.remainTokens = (int) (this.remainTokens + refillCounts);
                this.lastUpdatedAt = now;
            }

            if (this.capacity < tokens || remainTokens == 0) {
                return false;
            }
            this.remainTokens -= tokens;

            return true;
        }
    }
}
