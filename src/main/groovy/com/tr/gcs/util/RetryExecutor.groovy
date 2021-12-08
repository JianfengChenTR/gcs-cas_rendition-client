package com.tr.gcs.util

import org.apache.log4j.Logger

@SuppressWarnings(['CatchException', 'DuplicateNumberLiteral', 'MethodReturnTypeRequired', 'NoDef', 'VariableTypeRequired'])
class RetryExecutor
{
    final static Logger LOG = Logger.getLogger(this.class)
    Set<Class<Exception>> exceptionsToRetry
    Integer maxAttempts = 4
    long exponentialBackOffBase = 500L
    String logDetails = ""

    def executeWithRetry(Closure method)
    {
        def retVal = null
        Boolean finished = false
        Integer attempts = 0

        while (!finished)
        {
            try
            {
                retVal = method()
                finished = true
            }
            catch (Exception e)
            {
                attempts++
                if (reachedMaxAttempts(attempts) || !isRetryableException(e))
                {
                    throw e
                }
                else
                {
                    Thread.sleep(getWaitTimeInMilliseconds(attempts))
                    LOG.info("Attempting retry #$attempts for $logDetails")
                }
            }
        }

        retVal
    }

    private Boolean reachedMaxAttempts(Integer attempts)
    {
        attempts >= maxAttempts
    }

    protected Boolean isRetryableException(Exception e)
    {
        Boolean retVal = false
        if (exceptionsToRetry?.contains(e.class))
        {
            retVal = true
        }
        else
        {
            Class thrownExceptionClass = e.class
            // Adding check for Object just in case something goes really wrong, so we won't get stuck in an infinite loop
            while (thrownExceptionClass != Throwable && thrownExceptionClass != Object)
            {
                if (exceptionsToRetry?.contains(thrownExceptionClass))
                {
                    retVal = true
                    break
                }
                thrownExceptionClass = thrownExceptionClass.superclass
            }
        }
        retVal
    }

    private long getWaitTimeInMilliseconds(int retryCount)
    {
        getExponentialBackOffMilliseconds(retryCount, exponentialBackOffBase)
    }

    static long getExponentialBackOffMilliseconds(int attempt, long exponentialBackOffBaseMilliseconds)
    {
        (long) Math.pow(2, attempt) * exponentialBackOffBaseMilliseconds
    }
}

