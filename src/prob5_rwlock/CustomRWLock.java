package prob5_rwlock;

public interface CustomRWLock {
    void readLock() throws InterruptedException;
    void readUnlock();
    void writeLock() throws InterruptedException;
    void writeUnlock();
}
