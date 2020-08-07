package com.thoughtworks.rslist.exception;

public class UserIndexNotValidException extends Exception{
    private String message = "Invalid user index";
    public UserIndexNotValidException(String message) {
        this.message = message;
    }
    public UserIndexNotValidException(){}
}
