package com.liam.ratelimiter;

import com.eudemon.ratelimiter.exception.InternalErrorException;
import com.google.common.base.Stopwatch;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

@Test
public class LiamTest {

    public void testM1(){
        System.out.println("xxx");
        System.out.println(TimeUnit.SECONDS.toMillis(1));
        Stopwatch started = Stopwatch.createStarted();

             do {
                 if(started.elapsed(TimeUnit.MILLISECONDS) > TimeUnit.SECONDS.toMillis(2)){
                     System.out.println(" ....2");
                     started.reset();
                     started.start();
                 }
             }while (true);

    }

    public void testLiamRateLimiter(){
        LiamRateLimiterV1 liamRateLimiter = new LiamRateLimiterV1();
        try {
            boolean isLimit = liamRateLimiter.limit("app1", "/xxx");
        } catch (InternalErrorException e) {
            e.printStackTrace();
        }
    }
}
