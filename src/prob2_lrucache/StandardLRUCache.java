package prob2_lrucache;

import java.util.Optional;

public class StandardLRUCache<K, V> implements ConcurrentLRUCache<K, V> {
    private final int capacity;

    public StandardLRUCache(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }
        this.capacity = capacity;
        // TODO: 필요한 자료구조 초기화
    }

    @Override
    public Optional<V> get(K key) {
        // TODO: 구현
        return Optional.empty();
    }

    @Override
    public void put(K key, V value) {
        // TODO: 구현
    }

    @Override
    public int size() {
        // TODO: 구현
        return 0;
    }

    @Override
    public int capacity() {
        return this.capacity;
    }
}
