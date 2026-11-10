package prob4_blockingqueue;

import java.time.Duration;
import java.util.LinkedList;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class CustomBoundedBlockingQueue<T> implements BoundedBlockingQueue<T> {
    private final int capacity;
    private LinkedList<T> buffer;
    private ReentrantLock lock;
    private Condition notEmpty;
    private Condition notFull;

    public CustomBoundedBlockingQueue(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }
        this.capacity = capacity;
        this.buffer = new LinkedList<>();
        this.lock = new ReentrantLock();
        this.notEmpty = lock.newCondition();
        this.notFull = lock.newCondition();
    }

    @Override
    public void put(T item) throws InterruptedException {
        if (item == null) {
            throw new NullPointerException("Item cannot be null");
        }

        this.lock.lock();
        try {
            // 1. 빈자리가 없다면 자리가 날 때까지 대기
            while (this.isFull()) {
                this.notFull.await();
            }

            // 2. 빈자리가 생기면 넣고 소비자에게 신호
            this.buffer.add(item);
            this.notEmpty.signal();
        } finally {
            this.lock.unlock(); // 예외/인터럽트 발생 시에도 100% 해제 보장
        }
    }

    @Override
    public T take() throws InterruptedException {
        this.lock.lock();
        try {
            // 1. 데이터가 없다면 들어올 때까지 대기
            while (this.isEmpty()) {
                this.notEmpty.await();
            }

            // 2. 데이터가 생기면 꺼내고 생산자에게 신호
            T value = this.buffer.removeFirst(); // FIFO 보장
            this.notFull.signal();
            return value;
        } finally {
            this.lock.unlock(); // 100% 해제 보장
        }
    }

    @Override
    public boolean offer(T item, Duration timeout) throws InterruptedException {
        if (item == null) {
            throw new NullPointerException("Item cannot be null");
        }

        long nanosTimeout = timeout.toNanos(); // 1. 전체 나노초 변환
        this.lock.lock();
        try {
            // 2. while 루프로 가짜 깨어남 방어 및 남은 시간 차감
            while (this.isFull()) {
                if (nanosTimeout <= 0L) {
                    return false; // 시간 초과 시 포기
                }
                nanosTimeout = this.notFull.awaitNanos(nanosTimeout);
            }

            // 3. 자리가 확보되었으므로 데이터 추가
            this.buffer.add(item);
            this.notEmpty.signal();
            return true; // 성공 시 true 반환
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public Optional<T> poll(Duration timeout) throws InterruptedException {
        long nanosTimeout = timeout.toNanos(); // 1. 전체 나노초 변환
        this.lock.lock();
        try {
            // 2. while 루프로 가짜 깨어남 방어 및 남은 시간 차감
            while (this.isEmpty()) {
                if (nanosTimeout <= 0L) {
                    return Optional.empty(); // 시간 초과 시 빈 값 반환
                }
                nanosTimeout = this.notEmpty.awaitNanos(nanosTimeout);
            }

            // 3. 데이터가 확보되었으므로 꺼내기
            T value = this.buffer.removeFirst();
            this.notFull.signal();
            return Optional.of(value);
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public int size() {
        this.lock.lock();
        try {
            return this.buffer.size();
        } finally {
            this.lock.unlock();
        }
    }

    @Override
    public int capacity() {
        return this.capacity;
    }

    // 이미 외부(put, take, offer, poll)에서 lock을 쥔 상태에서만 호출되므로 동기화 불필요
    private boolean isFull() {
        return this.buffer.size() == this.capacity;
    }

    private boolean isEmpty() {
        return this.buffer.isEmpty();
    }
}
