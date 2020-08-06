package com.thoughtworks.rslist.exception;

public class UserIndexNotValidException extends Exception{
    private String message;
    public UserIndexNotValidException(String message) {
        this.message = message;
    }
}
