package prob4_blockingqueue;

import java.time.Duration;
import java.util.Optional;

public interface BoundedBlockingQueue<T> {
    void put(T item) throws InterruptedException;
    T take() throws InterruptedException;
    boolean offer(T item, Duration timeout) throws InterruptedException;
    Optional<T> poll(Duration timeout) throws InterruptedException;
    int size();
    int capacity();
}
