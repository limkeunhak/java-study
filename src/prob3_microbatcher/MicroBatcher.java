package prob3_microbatcher;

public interface MicroBatcher<T> extends AutoCloseable {
    /**
     * 배치 처리를 위해 아이템을 버퍼에 추가합니다.
     * 이미 닫힌 상태라면 IllegalStateException을 던집니다.
     */
    void add(T item);

    /**
     * 버퍼에 대기 중인 모든 아이템을 즉시 플러시합니다.
     */
    void flush();

    /**
     * 배처를 정상 종료합니다.
     * 남은 데이터를 모두 플러시하고 타이머 리소스를 정리합니다.
     */
    @Override
    void close();
}
