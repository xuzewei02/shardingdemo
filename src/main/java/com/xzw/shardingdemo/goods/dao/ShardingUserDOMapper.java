package com.xzw.shardingdemo.goods.dao;

import com.xzw.shardingdemo.goods.dto.MyShardingOrderDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Classname MyShardingOrderDOMapper
 */
@Mapper
@Repository
public interface ShardingUserDOMapper {


    List<MyShardingOrderDO> selectOrderAndUserInfo();


}
