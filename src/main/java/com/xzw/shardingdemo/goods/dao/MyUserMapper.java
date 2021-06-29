package com.xzw.shardingdemo.goods.dao;

import com.xzw.shardingdemo.goods.domain.MyUser;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface MyUserMapper {
    int deleteByPrimaryKey(Long userId);

    int insert(MyUser record);

    int insertSelective(MyUser record);

    MyUser selectByPrimaryKey(Long userId);

    int updateByPrimaryKeySelective(MyUser record);

    int updateByPrimaryKey(MyUser record);
}