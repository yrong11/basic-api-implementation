package com.thoughtworks.rslist.exception;

public class BadRequestException extends Exception{
    private String message = "invalid params";
    public BadRequestException(String message) {
        this.message = message;
    }
    public BadRequestException(){}
}
