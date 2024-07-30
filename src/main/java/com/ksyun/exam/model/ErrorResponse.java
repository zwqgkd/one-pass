package com.ksyun.exam.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {
    private String errorCode;

    private String errorMsg;
}
