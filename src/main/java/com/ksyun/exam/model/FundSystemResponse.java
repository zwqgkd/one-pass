package com.ksyun.exam.model;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class FundSystemResponse {

    private int code;

    private String msg;

    private String requestId;

    private String data;
}


