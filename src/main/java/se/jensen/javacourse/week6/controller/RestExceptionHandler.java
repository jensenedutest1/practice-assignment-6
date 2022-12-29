package se.jensen.javacourse.week6.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler
{
    @ExceptionHandler(value = { MethodArgumentTypeMismatchException.class })
    protected ResponseEntity<Object> handleMismatchedArgument(RuntimeException ex, WebRequest request)
    {
        String responseBody = "Wrong path variable type: " + ex.getMessage();
        return handleExceptionInternal(ex, responseBody, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
}