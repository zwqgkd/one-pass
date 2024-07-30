package com.ksyun.exam.controller;

import com.ksyun.exam.model.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.RestClientResponseException;
import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler({RestClientResponseException.class})
    @ResponseBody
    @ResponseStatus
    public ErrorResponse handleRestClientException(HttpServletRequest request, RestClientResponseException e) {
        log.error("handleRestClientException", e);
        ErrorResponse response = new ErrorResponse();
        response.setErrorCode("SystemError");
        response.setErrorMsg(e.getMessage());
        return response;
    }

    @ExceptionHandler({ArithmeticException.class})
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleArithmeticException(HttpServletRequest request, ArithmeticException e) {
        log.error("handleArithmeticException request method: {}, uri: {}",
                request.getMethod(), request.getRequestURI(), e);
        ErrorResponse response = new ErrorResponse();
        response.setErrorCode("BadRequest");
        response.setErrorMsg(e.getMessage());
        return response;
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus
    public ErrorResponse handleAllException(HttpServletRequest request, Throwable e) {
        log.error("handleAllException request method: {}, uri: {}",
                request.getMethod(), request.getRequestURI(), e);
        ErrorResponse response = new ErrorResponse();
        response.setErrorCode("UnExpectedError");
        response.setErrorMsg(e.getMessage());
        return response;
    }
}
