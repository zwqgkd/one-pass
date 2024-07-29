package com.ksyun.exam.mapper.module;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRecord {

    private Long id;

    private BigDecimal balanceAmount;
};
