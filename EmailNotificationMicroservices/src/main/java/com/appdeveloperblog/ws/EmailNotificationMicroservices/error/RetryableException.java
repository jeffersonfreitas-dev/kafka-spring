package com.appdeveloperblog.ws.EmailNotificationMicroservices.error;

public class RetryableException extends RuntimeException{

    public RetryableException(String message){
        super(message);
    }

    public RetryableException(Throwable cause){
        super(cause);
    }
}
