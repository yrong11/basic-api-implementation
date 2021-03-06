package com.thoughtworks.rslist.componet;

import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.exception.BadRequestException;
import com.thoughtworks.rslist.exception.Error;
import com.thoughtworks.rslist.exception.RsEventIndexNotValidException;
import com.thoughtworks.rslist.exception.UserIndexNotValidException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class RsEventExceptionHandler {

    private static final String logExceptionFormat = "Capture Exception By GlobalExceptionHandler - Detail: %s";
    private static Logger log = LoggerFactory.getLogger(RsEventExceptionHandler.class);

    @ExceptionHandler({RsEventIndexNotValidException.class, MethodArgumentNotValidException.class, UserIndexNotValidException.class})
    public ResponseEntity RsEventExceptionHandler(Exception e){
        String message;
        if (e instanceof RsEventIndexNotValidException)
            message = e.getMessage();
        else{
            if (((MethodArgumentNotValidException) e).getBindingResult().getTarget() instanceof User)
                message = "invalid user";
            else
                message = "invalid param";
        }

        Error error = new Error();
        error.setError(message);

        log.error(String.format(logExceptionFormat,message));
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(value =  BadRequestException.class)
    public ResponseEntity BadRequestExceptionHandler(Exception e){
        Error error = new Error();
        error.setError(e.getMessage());

        return ResponseEntity.badRequest().body(error);
    }


}
