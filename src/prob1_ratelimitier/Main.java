package prob1_ratelimitier;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        RateLimiter rateLimiter = new TokenBucketLimiter(2.0, 3);
        RequesterThread r1 = new RequesterThread(rateLimiter, "r1");
        RequesterThread r2 = new RequesterThread(rateLimiter, "r2");

        r1.start();
        r2.start();
    }

    public static class RequesterThread extends Thread {
        private RateLimiter rateLimiter;
        private String clientId;

        RequesterThread(RateLimiter rateLimiter, String clientId) {
            this.rateLimiter = rateLimiter;
            this.clientId = clientId;
        }

        @Override
        public void run() {
            System.out.println(this.clientId + " is request allowed: " + this.rateLimiter.allow(this.clientId));
            System.out.println(this.clientId + " is request allowed: " + this.rateLimiter.allow(this.clientId));
            System.out.println(this.clientId + " is request allowed: " + this.rateLimiter.allow(this.clientId));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(this.clientId + " is request allowed(2nd): " + this.rateLimiter.allow(this.clientId));
            System.out.println(this.clientId + " is request allowed(2nd): " + this.rateLimiter.allow(this.clientId));
            System.out.println(this.clientId + " is request allowed(2nd): " + this.rateLimiter.allow(this.clientId));
            System.out.println(this.clientId + " is request allowed(2nd): " + this.rateLimiter.allow(this.clientId));
        }
    }
}
