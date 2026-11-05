package prob4_blockingqueue;

import java.time.Duration;
import java.util.LinkedList;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

public class CustomBoundedBlockingQueue<T, 것, 내일은, 것도, 동안> implements BoundedBlockingQueue<T> {
    private final int capacity;
    private LinkedList<T> buffer;
    private ReentrantLock fullLock;
    private ReentrantLock emptyLock;
    private Condition condition;

    public CustomBoundedBlockingQueue(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }
        this.capacity = capacity;
        this.buffer = new LinkedList<>();
        this.fullLock = new ReentrantLock();
        this.emptyLock = new ReentrantLock();
        this.condition = Condition.Empty;
    }

    @Override
    public synchronized void put(T item) throws InterruptedException {
        // 1. 빈자리가 없다면 자리가 날 때까지 대기
        while (this.isFull()) {
            fullLock.wait();
        }

        // 2. 빈자리가 있다면 넣기
        this.buffer.add(item);
        fullLock.notifyAll();
    }

    @Override
    public synchronized T take() throws InterruptedException {
        // TODO: 구현
        // 1. 큐에 데이터가 있다면 즉시 꺼내어 반환
        // 2. 큐에 데이터가 없다면 데이터가 들어올때까지 대기
        // 3. 큐에 데이터가 들어오면 꺼내어 반환 (들어오지 않는다면 무한 대기)
        while(this.isEmpty()) {
            emptyLock.wait();
        }

        T value = this.buffer.pop();
        emptyLock.notifyAll();
        return value;
    }

    @Override
    public boolean offer(T item, Duration timeout) throws InterruptedException {
        // TODO: 구현
        // 1. 빈자리가 있다면 즉시 넣고 true 반환
        // 2. 큐가 가득찼다면 빈자리가 생길때까지 timeout 시간동안 대기
        // 3. 대기 시간 내에 자리가 나면 아이템을 넣고 true 반환
        // 4. 대기 시간 내에 자리가 안나면 넣지 않고 false 반환
        return false;
    }

    @Override
    public Optional<T> poll(Duration timeout) throws InterruptedException {
        // TODO: 구현
        // 1. 큐에 데이터가 있다면 꺼내서 Optional.of(item) 반환
        // 2. 큐가 비어있다면 데이터가 들어올 때까지 최대 timeout 시간동안 대기
        // 3. 대기 시간 내에 새 데이터가 들어오면 꺼내어 Optional.of(item) 반환
        // 4. 시간이 끝날 때까지 데이터가 들어오지 않으면 Optional.empty() 반환
        return Optional.empty();
    }

    @Override
    public synchronized int size() {
        return this.buffer.size();
    }

    @Override
    public int capacity() {
        return this.capacity;
    }

    private synchronized boolean isFull() {
        return this.buffer.size() == this.capacity;
    }

    private synchronized boolean isEmpty() {
        return this.buffer.isEmpty();
    }
}

enum Condition {
    Empty,
    NotEmpty,
    Full,
    NotFull,
}