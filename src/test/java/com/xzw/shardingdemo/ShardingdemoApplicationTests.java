/*
package com.xzw.shardingdemo;

import com.xzw.shardingdemo.goods.service.GoodsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ShardingdemoApplicationTests {

    */
/*@Autowired
    GoodsMapper goodsMapper;

    @Autowired
    MyDictMapper myDictMapper;*//*


    @Autowired
    GoodsService goodsService;

    */
/**
     * 测试 分表插入
     *//*

    @Test
    void addGoods() {
        */
/*Goods goods = new Goods();
        goods.setGid(2);
        goods.setGName("小米手机" + 2);
        goods.setUserId(1L);
        goods.setGStatus("OPEN");
        goodsMapper.insert(goods);*//*

    }

    */
/**
     * 测试 循环 分表插入
     *//*

    @Test
    void addGoodsBatch() {
        */
/*for (int i = 0; i < 10; i++) {
            Goods goods = new Goods();
            goods.setGid(i);
            goods.setGName("小米手机" + i);
            goods.setUserId(1L);
            goods.setGStatus("OPEN");
            goodsMapper.insert(goods);
        }*//*

    }

    @Test
    public void testTransactionBatchInsert() throws Exception{
        */
/*Goods goods = new Goods();
        goods.setGid(1);
        goods.setGName("小米手机" + 1);
        goods.setUserId(1L);
        goods.setGStatus("OPEN");

        Goods goods2 = new Goods();
        goods2.setGid(2);
        goods2.setGName("小米手机" + 2);
        goods2.setUserId(2L);
        goods2.setGStatus("OPEN");

        List<Goods> goodsList = Arrays.asList(goods,goods2);
        goodsService.testAddGoodsTransaction(goodsList);*//*

    }

    */
/**
     * 根据ID 到对应表获取商品信息
     *//*

    @Test
    void getGood(){
        */
/*QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("gid",1L);
        Goods good = goodsMapper.selectOne(queryWrapper);
        System.out.println(good.toString());*//*

    }


    */
/**
     * 下面是公共表测试方法
     *//*

    @Test
    void addDict(){
        */
/*MyDict myDict = new MyDict();
        myDict.setDictId(1l);
        myDict.setDictName("已启用");
        myDict.setDictCode("1");
        myDictMapper.insert(myDict);*//*

    }

    @Test
    void deleteDict(){
        */
/*QueryWrapper<MyDict> wrapper = new QueryWrapper<>();
        wrapper.eq("dict_id","");
        myDictMapper.delete(wrapper);*//*

    }



}
*/
