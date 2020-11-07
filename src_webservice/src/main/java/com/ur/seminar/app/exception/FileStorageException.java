package com.ur.seminar.app.exception;

/**
 * This class contains the exception returned, if a file could not have been stored*/
public class FileStorageException extends RuntimeException{

    public FileStorageException(String message) {
        super(message);
    }

    public FileStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
