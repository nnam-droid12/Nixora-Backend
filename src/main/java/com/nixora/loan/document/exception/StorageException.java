package com.nixora.loan.document.exception;

public class StorageException extends RuntimeException{

    public StorageException(String message, Exception e){
        super(message);
    }
}

