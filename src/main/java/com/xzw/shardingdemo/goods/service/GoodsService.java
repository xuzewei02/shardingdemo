package com.xzw.shardingdemo.goods.service;

import com.xzw.shardingdemo.goods.domain.Goods;

import java.util.List;

public interface GoodsService {

    void testAddGoodsTransaction(List<Goods> goodsList) throws Exception ;
}
