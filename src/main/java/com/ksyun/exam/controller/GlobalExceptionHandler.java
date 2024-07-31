package com.ksyun.exam.controller;

import com.ksyun.exam.model.ErrorResponse;
import com.ksyun.exam.model.FundSystemResponse;
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
    public FundSystemResponse handleRestClientException(HttpServletRequest request, RestClientResponseException e) {
        log.error("handleRestClientException", e);
        return new FundSystemResponse(
                400,
                "BadRequest",
                "null",
                e.getMessage()
        );
    }

    @ExceptionHandler({ArithmeticException.class})
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public FundSystemResponse handleArithmeticException(HttpServletRequest request, ArithmeticException e) {
        log.error("handleArithmeticException request method: {}, uri: {}",
                request.getMethod(), request.getRequestURI(), e);
        FundSystemResponse response = new FundSystemResponse();
        return new FundSystemResponse(
                400,
                "BadRequest",
                "null",
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus
    public FundSystemResponse handleAllException(HttpServletRequest request, Throwable e) {
        log.error("handleAllException request method: {}, uri: {}",
                request.getMethod(), request.getRequestURI(), e);
        return new FundSystemResponse(
                400,
                "InternalServerError",
                "null",
                e.getMessage()
        );
    }
}
