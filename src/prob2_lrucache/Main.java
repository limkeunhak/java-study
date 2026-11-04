package prob2_lrucache;

import java.util.Optional;

public class Main {
    public void main(String[] args) {
        ConcurrentLRUCache<String, String> lruCache = new StandardLRUCache<>(3);

        System.out.println(lruCache.get("A"));
        lruCache.put("A", "A");
        System.out.println("lruCache.put(\"A\", \"A\")");
        lruCache.print();
        lruCache.put("B", "B");
        System.out.println("lruCache.put(\"B\", \"B\")");
        lruCache.print();
        lruCache.put("C", "C");
        System.out.println("lruCache.put(\"C\", \"C\")");
        lruCache.print();
        lruCache.get("A");
        System.out.println("lruCache.get(\"A\")");
        lruCache.print();
        lruCache.put("A", "A2");
        System.out.println("lruCache.put(\"A\", \"A2\")");
        lruCache.print();
        lruCache.get("B");
        System.out.println("lruCache.get(\"B\")");
        lruCache.print();
        lruCache.get("C");
        System.out.println("lruCache.get(\"C\")");
        lruCache.print();
        lruCache.get("D");
        System.out.println("lruCache.get(\"D\")");
        lruCache.print();
        lruCache.put("D", "D");
        System.out.println("lruCache.put(\"D\", \"D\")");
        lruCache.print();
    }
}
