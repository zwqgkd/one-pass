package com.ksyun.exam.model;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class FundSystemResponse {

    private int code;

    private String msg;

    private String requestId;

    private String data;
}


