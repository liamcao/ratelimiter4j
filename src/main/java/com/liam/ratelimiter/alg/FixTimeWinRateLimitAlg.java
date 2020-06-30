package com.liam.ratelimiter.alg;

import com.eudemon.ratelimiter.exception.InternalErrorException;
import com.google.common.base.Stopwatch;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FixTimeWinRateLimitAlg implements IRateLimitAlg{
    private static final long TRY_LOCK_TIME = 200L;//200ms
    private Stopwatch stopwatch;
    private final int limit;
    private AtomicInteger counter = new AtomicInteger(0);
    private Lock lock = new ReentrantLock();

    public FixTimeWinRateLimitAlg(int limit) {
        this(limit, Stopwatch.createStarted());
    }

    public FixTimeWinRateLimitAlg(int limit, Stopwatch stopwatch ) {
        this.limit = limit;
        this.stopwatch = stopwatch;
    }

    public boolean tryAcquire() throws InternalErrorException {
        //1.未超过limit数量
        int updatedCount = counter.incrementAndGet();
        if(updatedCount <= limit){
            return true;
        }

        //2.超过了limit数量，只能让一个线程来重制计时器，否则就乱了
        try{
            if(lock.tryLock(TRY_LOCK_TIME, TimeUnit.MILLISECONDS)){//如果这里lock上了，在里面就要unlock
                try {
                    //2.1如果时间到了，就重制计时器
                    if(stopwatch.elapsed(TimeUnit.MILLISECONDS) > TimeUnit.SECONDS.toMillis(1)){
                        counter.set(0);
                        stopwatch.reset();
                        stopwatch.start();
                    }
                    //2.2 如果计时器的时间到了，或者时间没到，都来执行
                    updatedCount = counter.incrementAndGet();
                    return updatedCount <= limit;
                }finally {
                    lock.unlock();
                }
            }else{//如果在TRY_LOCK_TIME时间内没有lock上
                throw new InternalErrorException("tryAcquire() wait lock too long:" + TRY_LOCK_TIME + "ms");
            }
        } catch (InterruptedException e) {//如果获取lock的过程中被打断
            throw new InternalErrorException("tryAcquire() is interrupted by lock-time-out.", e);
        }
    }
}
