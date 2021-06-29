package com.xzw.shardingdemo.goods.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("t_dict")
public class MyDict {
    private Long dictId;
    private String dictName;
    private String dictCode;
}
