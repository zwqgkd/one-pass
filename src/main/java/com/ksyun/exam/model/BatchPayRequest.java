package com.ksyun.exam.model;

import lombok.*;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class BatchPayRequest {

    private String batchPayId;

    private List<Long> uids;
}
