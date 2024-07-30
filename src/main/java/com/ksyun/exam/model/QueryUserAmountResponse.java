package com.ksyun.exam.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ksyun.exam.mapper.module.UserRecord;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QueryUserAmountResponse {

    @JsonProperty("code")
    private int code;

    @JsonProperty("msg")
    private String msg;

    @JsonProperty("requestId")
    private String requestId;

    @JsonProperty("data")
    private List<UserRecord> users;
}
