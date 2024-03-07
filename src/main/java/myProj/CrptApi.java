package myProj;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class CrptApi {

    private final Semaphore semaphore;
    private final AtomicInteger requestCounter;
    private final Lock lock = new ReentrantLock();
    private final AtomicInteger totalDocumentsCreated = new AtomicInteger(0);
    private final AtomicInteger totalSignaturesReceived = new AtomicInteger(0);

    public CrptApi(TimeUnit timeUnit, int requestLimit) {
        this.semaphore = new Semaphore(requestLimit);
        this.requestCounter = new AtomicInteger(0);
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(this::resetRequestCounter, 0, 1, timeUnit);
    }

    private void resetRequestCounter() {
        lock.lock();
        try {
            requestCounter.set(0);
        } finally {
            lock.unlock();
        }
    }

    public void createDocument(String document, String signature) {
        if (document == null || signature == null) {
            throw new IllegalArgumentException("Document and signature must not be null");
        }

        try {
            semaphore.acquire();
            incrementRequestCounter();
            incrementTotalDocumentsCreated();
            incrementTotalSignaturesReceived();
            System.out.println("Document created: " + document);
            System.out.println("Signature received: " + signature);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            semaphore.release();
        }
    }


    private void incrementRequestCounter() {
        lock.lock();
        try {
            requestCounter.incrementAndGet();
        } finally {
            lock.unlock();
        }
    }

    private void incrementTotalDocumentsCreated() {
        totalDocumentsCreated.incrementAndGet();
    }

    private void incrementTotalSignaturesReceived() {
        totalSignaturesReceived.incrementAndGet();
    }

    public int getTotalDocumentsCreated() {
        return totalDocumentsCreated.get();
    }

    public int getTotalSignaturesReceived() {
        return totalSignaturesReceived.get();
    }
}
