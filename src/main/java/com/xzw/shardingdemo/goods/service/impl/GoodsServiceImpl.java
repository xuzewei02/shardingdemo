package com.xzw.shardingdemo.goods.service.impl;

import com.xzw.shardingdemo.goods.domain.Goods;
import com.xzw.shardingdemo.goods.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GoodsServiceImpl implements GoodsService {

    /*@Autowired
    private GoodsMapper goodsMapper;*/


    /**
     * 事务是否一致测试 不一致
     * @param goodsList
     * @throws Exception
     */
    @Transactional
    public void testAddGoodsTransaction(List<Goods> goodsList) throws Exception {
        for (Goods goods : goodsList) {
            System.out.println("testAddGoodsTransaction, good id=" + goods.getGid());
            if (goods.getGid().equals(2)) {
                System.out.println("testAddGoodsTransaction will throw exception, good id=" + goods.getGid());
                /*goodsMapper.insert(goods);
                continue;*/
                throw new Exception("111");
            }
            //oodsMapper.insert(goods);
        }
    }
}
