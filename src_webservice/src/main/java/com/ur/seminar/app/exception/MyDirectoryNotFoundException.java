package com.ur.seminar.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * This class contains the exception format of a directory not found
 * **/
@ResponseStatus(HttpStatus.NOT_FOUND)
public class MyDirectoryNotFoundException extends RuntimeException {

    public MyDirectoryNotFoundException(String message){ super(message);}
    public MyDirectoryNotFoundException(String message, Throwable cause){ super(message, cause);}
}
