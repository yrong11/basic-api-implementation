package com.thoughtworks.rslist.exception;

import lombok.Data;

public class RsEventIndexNotValidException extends RuntimeException{
    private String message;
    public RsEventIndexNotValidException(String message) {
        this.message = message;
    }
}
