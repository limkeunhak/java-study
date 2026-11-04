package prob2_lrucache;

import java.util.HashMap;
import java.util.Optional;

public class StandardLRUCache<K, V> implements ConcurrentLRUCache<K, V> {
    private final int capacity;
    private HashMap<K, CacheLine<K, V>> hashMap;
    private CacheLine<K, V> head;      // 이 노드의 앞 노드
    private CacheLine<K, V> tail;      // 이 노드의 뒤 노드

    public StandardLRUCache(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be positive");
        }
        this.capacity = capacity;
        this.hashMap = new HashMap<>();

        this.head = new CacheLine<>(null, null);
        this.tail = new CacheLine<>(null, null);
        this.head.setNext(this.tail);
        this.tail.setPrev(this.head);
    }

    @Override
    public synchronized Optional<V> get(K key) {
        CacheLine<K, V> cacheLine = this.hashMap.get(key);
        if (cacheLine == null) {
            return Optional.empty();
        }

        moveToHead(cacheLine);
        return Optional.of(cacheLine.getValue());
    }

    @Override
    public synchronized void put(K key, V value) {
        // 존재하지 않는 노드라면
        CacheLine<K, V> cacheLine = this.hashMap.get(key);
        if (cacheLine == null) {
            cacheLine = new CacheLine<>(key, value);

            cacheLine.setPrev(this.head);
            cacheLine.setNext(this.head.getNext());

            this.head.getNext().setPrev(cacheLine);

            this.head.setNext(cacheLine);
            this.hashMap.put(key, cacheLine);
        } else {
            // 이미 존재하는 노드라면
            cacheLine.setValue(value);
            moveToHead(cacheLine);   // HEAD와 다음 노드 사이에 캐시라인 삽입
        }

        // 캐시 사이즈 체크
        if (this.hashMap.size() > this.capacity) {
            CacheLine<K, V> deleted = this.tail.getPrev();
            deleted.getPrev().setNext(this.tail);

            this.tail.setPrev(deleted.getPrev());
            this.hashMap.remove(deleted.getKey());
        }
    }

    private void moveToHead(CacheLine<K, V> cacheLine) {
        this.removeNode(cacheLine);
        this.addFirst(cacheLine);
    }

    private void removeNode(CacheLine<K, V> cacheLine) {
        cacheLine.getPrev().setNext(cacheLine.getNext());
        cacheLine.getNext().setPrev(cacheLine.getPrev());
    }

    private void addFirst(CacheLine<K, V> cacheLine) {
        cacheLine.setNext(this.head.getNext());
        cacheLine.setPrev(this.head);

        this.head.getNext().setPrev(cacheLine);
        this.head.setNext(cacheLine);
    }

    @Override
    public synchronized int size() {
        return this.hashMap.size();
    }

    @Override
    public int capacity() {
        return this.capacity;
    }

    @Override
    public synchronized void print() {
        System.out.print("head -> ");
        CacheLine<K, V> cacheLine = head.getNext();
        while(cacheLine.getNext() != null) {
            if (cacheLine.getNext() == null) {
                break;
            }

            System.out.print(cacheLine.getValue().toString() + " -> ");
            cacheLine = cacheLine.getNext();
        }
        System.out.println("tail");
    }
}

