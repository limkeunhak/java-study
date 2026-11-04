package prob2_lrucache;

public class CacheLine<K, V> {
    private K key;
    private V value;
    private CacheLine<K, V> prev;      // 이 노드의 앞 노드
    private CacheLine<K, V> next;      // 이 노드의 뒤 노드

    CacheLine(K key, V value) {
        this.key = key;
        this.value = value;
        this.prev = null;
        this.next = null;
    }

    public K getKey() {
        return this.key;
    }

    public V getValue() {
        return this.value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    public CacheLine<K, V> getPrev() {
        return prev;
    }

    public CacheLine<K, V> getNext() {
        return next;
    }

    public void setPrev(CacheLine<K, V> cacheLine) {
        this.prev = cacheLine;
    }

    public void setNext(CacheLine<K, V> cacheLine) {
        this.next = cacheLine;
    }

    @Override
    public String toString() {
        if (this.key == null) {
            return "";
        }
        return this.key.toString();
    }
}

