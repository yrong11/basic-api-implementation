package com.thoughtworks.rslist.componet;

import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.exception.Error;
import com.thoughtworks.rslist.exception.RsEventIndexNotValidException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RsEventExceptionHandler {

    @ExceptionHandler({RsEventIndexNotValidException.class, MethodArgumentNotValidException.class})
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

        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(value = BindException.class)
    public ResponseEntity UserExceptionHandler(){
        Error error = new Error();
        error.setError("invalid user");

        return ResponseEntity.badRequest().body(error);
    }
}
