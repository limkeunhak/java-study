package prob5_rwlock;

public class ReadWriteLockImpl implements CustomRWLock {
    // TODO: 동기화 상태 변수 정의 (readers, writers, writeRequests 등)

    public ReadWriteLockImpl() {
        // TODO: 초기화
    }

    @Override
    public synchronized void readLock() throws InterruptedException {
        // TODO: 읽기 락 진입 조건 (쓰기 기아 방지 고려)
    }

    @Override
    public synchronized void readUnlock() {
        // TODO: 읽기 락 해제 및 대기 중인 스레드 신호
    }

    @Override
    public synchronized void writeLock() throws InterruptedException {
        // TODO: 쓰기 락 진입 조건 (읽는 중이거나 쓰는 중이면 대기)
    }

    @Override
    public synchronized void writeUnlock() {
        // TODO: 쓰기 락 해제 및 대기 중인 스레드 신호
    }
}
