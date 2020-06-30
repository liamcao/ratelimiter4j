package com.liam.ratelimiter.alg;

import com.eudemon.ratelimiter.exception.InternalErrorException;

public interface IRateLimitAlg {
    boolean tryAcquire() throws InternalErrorException;
}
