package com.xzw.shardingdemo.goods.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Goods {
    private Integer gid;
    private String gName;
    private Long userId;
    private String gStatus;
}
