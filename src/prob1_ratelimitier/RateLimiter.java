package prob1_ratelimitier;

public interface RateLimiter {
    boolean allow(String clientId);
    boolean allowN(String clientId, int tokens);
}

