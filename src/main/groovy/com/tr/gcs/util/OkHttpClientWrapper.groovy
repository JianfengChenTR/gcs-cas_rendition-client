package com.tr.gcs.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.tr.gcs.JacksonConfig
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.apache.commons.io.FileUtils
import org.apache.commons.lang3.StringUtils
import org.apache.log4j.Logger

import java.nio.charset.StandardCharsets

@SuppressWarnings(['DuplicateNumberLiteral', 'PrivateFieldCouldBeFinal', 'DuplicateStringLiteral', 'JavaIoPackageAccess'])
class OkHttpClientWrapper
{
    final static Logger LOG = Logger.getLogger(this.class)
    private static Integer maxErrorResponseRetryAttempts = 3
    private static long exponentialBackOffBase = 500L

    public <T> T execute(Request request, Class<T> clazz = null)
    {
        T retVal = null
        Boolean finished = false
        Integer attempts = 0
        RetryExecutor retryExecutor = new RetryExecutor(exceptionsToRetry: [IOException, SocketTimeoutException] as Set, logDetails: request.toString())
        Response response = null
        try
        {
            OkHttpClient okHttpClient = new OkHttpClient()
            ObjectMapper objectMapper = JacksonConfig.objectMapper()

            while (!finished)
            {
                response = retryExecutor.executeWithRetry { okHttpClient.newCall(request).execute() } as Response
                if (response.isSuccessful())
                {
                    if (clazz)
                    {
                        retVal = objectMapper.readValue(response.body().string(), clazz)
                    }
                    finished = true
                }
                else if (response.code() >= 500)
                {
                    attempts++
                    if (reachedMaxAttempts(attempts))
                    {
                        throwHttpException(request, response)
                    }
                    else
                    {
                        closeResponse(response)
                        Thread.sleep(getWaitTimeInMilliseconds(attempts))
                        LOG.debug("Attempting retry #$attempts for $request")
                    }
                }
                else
                {
                    throwHttpException(request, response)
                }
            }
        }
        finally
        {
            closeResponse(response)
        }
        retVal
    }

    void executeAndSaveResponseBody(Request request, String outputFilePath)
    {
        Boolean finished = false
        Integer attempts = 0
        RetryExecutor retryExecutor = new RetryExecutor(exceptionsToRetry: [IOException, SocketTimeoutException] as Set, logDetails: request.toString())
        Response response = null
        try
        {
            OkHttpClient okHttpClient = new OkHttpClient()

            while (!finished)
            {
                response = retryExecutor.executeWithRetry { okHttpClient.newCall(request).execute() } as Response
                if (response.isSuccessful())
                {
                    FileUtils.writeStringToFile(new File(outputFilePath), response.body().string(), StandardCharsets.UTF_8)
                    finished = true
                }
                else if (response.code() >= 500)
                {
                    attempts++
                    if (reachedMaxAttempts(attempts))
                    {
                        throwHttpException(request, response)
                    }
                    else
                    {
                        closeResponse(response)
                        Thread.sleep(getWaitTimeInMilliseconds(attempts))
                        LOG.debug("Attempting retry #$attempts for $request")
                    }
                }
                else
                {
                    throwHttpException(request, response)
                }
            }
        }
        finally
        {
            closeResponse(response)
        }
    }

    String executeAndSaveData(Request request, String dir)
    {
        String retVal = null
        Boolean finished = false
        Integer attempts = 0
        RetryExecutor retryExecutor = new RetryExecutor(exceptionsToRetry: [IOException, SocketTimeoutException] as Set, logDetails: request.toString())
        Response response = null
        try
        {
            OkHttpClient okHttpClient = new OkHttpClient()

            while (!finished)
            {
                response = retryExecutor.executeWithRetry { okHttpClient.newCall(request).execute() } as Response
                if (response.isSuccessful())
                {
                    if (response.header('content-disposition') && response.header('content-disposition').contains('filename='))
                    {
                        String filePath = dir.endsWith('/') ? dir : dir + '/'
                        filePath = filePath + StringUtils.substringAfter(response.header('content-disposition'), 'filename=')
                        filePath = StringUtils.replace(filePath, '"', '')
                        LOG.info("Rendition data file path: $filePath")
                        InputStream inputStream = response.body().byteStream()
                        try
                        {
                            FileUtils.copyInputStreamToFile(inputStream, new File(filePath))
                        }
                        finally
                        {
                            inputStream?.close()
                        }
                        retVal = filePath
                        finished = true
                    }
                }
                else if (response.code() >= 500)
                {
                    attempts++
                    if (reachedMaxAttempts(attempts))
                    {
                        throwHttpException(request, response)
                    }
                    else
                    {
                        closeResponse(response)
                        Thread.sleep(getWaitTimeInMilliseconds(attempts))
                        LOG.debug("Attempting retry #$attempts for $request")
                    }
                }
                else
                {
                    throwHttpException(request, response)
                }
            }
        }
        finally
        {
            closeResponse(response)
        }
        retVal
    }

    private static void throwHttpException(Request request, Response response)
    {
        String message = "Failed to make request to '${request.url().toString()}' " + (response.body() ? response.body().string() : response.message())
        throw new HttpException(message, response.code())
    }

    private static Boolean reachedMaxAttempts(Integer attempts)
    {
        attempts >= maxErrorResponseRetryAttempts
    }

    private static long getWaitTimeInMilliseconds(int retryCount)
    {
        (long) Math.pow(2, retryCount) * exponentialBackOffBase
    }

    private static void closeResponse(Response response)
    {
        if (response?.body())
        {
            response.close()
        }
    }
}
