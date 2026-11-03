package prob2_lrucache;

import java.util.Optional;

public interface ConcurrentLRUCache<K, V> {
    Optional<V> get(K key);
    void put(K key, V value);
    int size();
    int capacity();
}
