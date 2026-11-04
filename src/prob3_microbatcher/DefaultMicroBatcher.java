package prob3_microbatcher;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

public class DefaultMicroBatcher<T> implements MicroBatcher<T> {
    private final int maxBatchSize;
    private final Duration maxWaitDuration;
    private final Consumer<List<T>> batchConsumer;
    private List<T> buffer;
    private final ScheduledExecutorService scheduler;
    private ReentrantLock lock;
    private boolean closed;

    public DefaultMicroBatcher(int maxBatchSize, Duration maxWaitDuration, Consumer<List<T>> batchConsumer) {
        if (maxBatchSize <= 0) {
            throw new IllegalArgumentException("maxBatchSize must be positive");
        }
        this.maxBatchSize = maxBatchSize;
        this.maxWaitDuration = maxWaitDuration;
        this.batchConsumer = batchConsumer;
        this.buffer = new ArrayList<>();
        this.scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(this::flush, this.maxWaitDuration.toMillis(), this.maxWaitDuration.toMillis(), TimeUnit.MILLISECONDS);
        this.lock = new ReentrantLock();
        this.closed = false;
    }

    @Override
    public void add(T item) {
        List<T> batchToAccept = null;
        this.lock.lock();
        if (this.closed) {
            return;
        }

        this.buffer.add(item);
        if (this.buffer.size() >= this.maxBatchSize) {
            batchToAccept = this.buffer;
            this.buffer = new ArrayList<>();
        }
        this.lock.unlock();

        if (batchToAccept != null) {
            this.invokeConsumer(batchToAccept);
        }
    }

    @Override
    public void flush() {
        this.lock.lock();
        if (this.closed) {
            return;
        }

        List<T> batchToAccept = this.buffer;
        this.buffer = new ArrayList<>();
        this.lock.unlock();

        if (batchToAccept != null) {
            this.invokeConsumer(batchToAccept);
        }
    }

    private void invokeConsumer(List<T> batchToAccept) {
        if (batchToAccept.isEmpty()) {
            return;
        }

        this.batchConsumer.accept(batchToAccept);
    }

    @Override
    public void close() {
        this.lock.lock();
        if (closed) {
            return;
        }
        this.closed = true;
        List<T> batchToAccept = this.buffer;
        this.buffer = new ArrayList<>();
        invokeConsumer(batchToAccept);

        this.scheduler.close();
        this.lock.unlock();
    }
}