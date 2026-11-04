package prob3_microbatcher;

import java.time.Duration;
import java.util.List;
import java.util.function.Consumer;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Consumer<List<String>> batchConsumer = new Consumer<List<String>>() {
            @Override
            public void accept(List<String> buffer) {
                System.out.println("accepted " + buffer.size() + " items, " + buffer.toString());
            }
        };
        MicroBatcher<String> batcher = new DefaultMicroBatcher<>(5, Duration.ofMillis(800), batchConsumer);
        batcher.add("A");
        batcher.add("B");
        batcher.add("C");
        batcher.add("D");
        batcher.add("E");
        batcher.add("F");
        Thread.sleep(1000);
        batcher.add("G");
        batcher.add("H");
        batcher.close();
    }
}

