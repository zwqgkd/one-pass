package com.ksyun.exam.model;

import lombok.*;

import java.math.BigDecimal;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TradeRequest {

    private Long sourceUid;
    private Long targetUid;
    private BigDecimal amount;
}