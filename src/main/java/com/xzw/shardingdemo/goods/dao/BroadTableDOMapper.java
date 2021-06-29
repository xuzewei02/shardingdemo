package com.xzw.shardingdemo.goods.dao;

import com.xzw.shardingdemo.goods.dto.BroadTableDO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @Classname BroadTableDOMapper
 */
@Mapper
@Repository
public interface BroadTableDOMapper {
    int deleteByPrimaryKey(Long id);

    int insert(BroadTableDO record);

    int insertGenerator(BroadTableDO record);

    int insertSelective(BroadTableDO record);

    BroadTableDO selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(BroadTableDO record);

    int updateByPrimaryKey(BroadTableDO record);

}
