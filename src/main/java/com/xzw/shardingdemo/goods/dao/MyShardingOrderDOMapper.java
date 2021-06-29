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
public interface MyShardingOrderDOMapper {
////    @Insert("insert into my_sharding values(#{orderId},#{userId},#{status},#{orderNo})")
//    int insert(MyShardingOrderDO myShardingOrderDO);
//
////    @Insert("insert into my_sharding (user_id,status,order_no) values(#{userId},#{status},#{orderNo})")
    int insertWithGenerator(MyShardingOrderDO myShardingOrderDO);
//
////    @Select("select * from my_sharding where user_id = #{userId} and order_id = #{orderId}")
////    @Results({
////            @Result(property = "orderId",column = "order_id"),
////            @Result(property = "userId",column = "user_id"),
////            @Result(property = "status",column = "status"),
////            @Result(property = "orderNo",column = "order_no")
////
////    })
//    MyShardingOrderDO selectByCondition(Long userId, Long orderId);
//
////    @Select("select * from my_sharding")
////    @Results({
////            @Result(property = "orderId",column = "order_id"),
////            @Result(property = "userId",column = "user_id"),
////            @Result(property = "status",column = "status"),
////            @Result(property = "orderNo",column = "order_no")
////
////    })
    List<MyShardingOrderDO> selectAll();

    List<MyShardingOrderDO> selectShardingOrderListByOrderRange();

    int deleteByPrimaryKey(Long orderId);

    int insert(MyShardingOrderDO record);

    int insertSelective(MyShardingOrderDO record);

    MyShardingOrderDO selectByPrimaryKey(Long orderId);

    MyShardingOrderDO selectByOrderNo(String orderNo);

    int updateByPrimaryKeySelective(MyShardingOrderDO record);

    int updateByPrimaryKey(MyShardingOrderDO record);

    List<MyShardingOrderDO> findShardingOrderByUserPage(Integer fromSize);

}
