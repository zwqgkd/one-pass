package com.ksyun.exam.mapper.module;

import lombok.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserRecord {

    @JsonProperty("uid")
    private Long id;

    @JsonProperty("amount")
    private BigDecimal balanceAmount;
};
