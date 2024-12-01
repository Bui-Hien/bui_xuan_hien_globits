package com.buihien.demo.exception;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidation(Exception exception, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(new Date());
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setPath(request.getDescription(false).replace("uri=", ""));
        String message = exception.getMessage();
        if (exception instanceof MethodArgumentNotValidException) {
            int startIndex = message.lastIndexOf("[");
            int lastIndex = message.lastIndexOf("]]");
            message = message.substring(startIndex + 1, lastIndex);
            errorResponse.setError("Payload Invalid");

        } else if (exception instanceof ConstraintViolationException) {
            message = message.substring(message.indexOf(" ") + 1);
            errorResponse.setError("Parameter Invalid");
        }
        errorResponse.setMessage(message);

        return errorResponse;
    }

//    @ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class})
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public ErrorResponse handleCustomException(Exception exception, WebRequest request) {
//        ErrorResponse errorResponse = new ErrorResponse();
//        errorResponse.setTimestamp(new Date());
//        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
//        errorResponse.setPath(request.getDescription(false).replace("uri=", ""));
//        String message = exception.getMessage();
//        if (exception instanceof MethodArgumentNotValidException) {
//            int startIndex = message.lastIndexOf("[");
//            int lastIndex = message.lastIndexOf("]]");
//            message = message.substring(startIndex + 1, lastIndex);
//            errorResponse.setError("Payload Invalid");
//
//        } else if (exception instanceof ConstraintViolationException) {
//            message = message.substring(message.indexOf(" ") + 1);
//            errorResponse.setError("Parameter Invalid");
//        }
//        errorResponse.setMessage(message);
//
//        return errorResponse;
//    }
}