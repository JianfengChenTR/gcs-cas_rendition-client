package com.tr.gcs.util

class HttpException extends RuntimeException
{
    int statusCode

    HttpException(String msg, int statusCode)
    {
        super(msg)
        this.statusCode = statusCode
    }
}
