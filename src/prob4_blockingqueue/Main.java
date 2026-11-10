package prob4_blockingqueue;

import java.time.Duration;
import java.util.Optional;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== 1. CustomBoundedBlockingQueue 기본 동작 및 블로킹 테스트 ===");
        // 용량 2짜리 작은 큐 생성
        CustomBoundedBlockingQueue<Integer> queue = new CustomBoundedBlockingQueue<>(2);

        // 소비자 스레드: 천천히 500ms 간격으로 꺼냄
        Thread consumer = new Thread(() -> {
            try {
                Thread.sleep(300); // 생산자가 먼저 채우도록 잠시 대기
                for (int i = 1; i <= 3; i++) {
                    int val = queue.take();
                    System.out.println("[소비자] 꺼냄: " + val + " (현재 size: " + queue.size() + ")");
                    Thread.sleep(300);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // 생산자 스레드: 3개를 빠르게 넣으려고 시도 (용량이 2이므로 3번째 아이템에서 블로킹 발생해야 함)
        Thread producer = new Thread(() -> {
            try {
                for (int i = 1; i <= 3; i++) {
                    System.out.println("[생산자] 넣기 시도: " + i);
                    queue.put(i);
                    System.out.println("[생산자] 넣기 완료: " + i + " (현재 size: " + queue.size() + ")");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        consumer.start();
        producer.start();

        consumer.join();
        producer.join();

        System.out.println("\n=== 2. offer() 타임아웃 테스트 (큐가 가득 찼을 때) ===");
        queue.put(100);
        queue.put(200); // 큐가 꽉 참 (capacity: 2)

        long start = System.currentTimeMillis();
        boolean offerResult = queue.offer(300, Duration.ofMillis(300));
        long elapsed = System.currentTimeMillis() - start;

        System.out.println("offer 결과 (false 예상): " + offerResult);
        System.out.println("대기 소요 시간 (~300ms 예상): " + elapsed + "ms");

        System.out.println("\n=== 3. poll() 타임아웃 테스트 (큐가 비었을 때) ===");
        queue.take();
        queue.take(); // 큐를 완전히 비움

        start = System.currentTimeMillis();
        Optional<Integer> pollResult = queue.poll(Duration.ofMillis(300));
        elapsed = System.currentTimeMillis() - start;

        System.out.println("poll 결과 (Optional.empty 예상): " + pollResult);
        System.out.println("대기 소요 시간 (~300ms 예상): " + elapsed + "ms");

        System.out.println("\n모든 동시성 검증 테스트 완료!");
    }
}

