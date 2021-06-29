package com.xzw.shardingdemo.goods.controller;

import com.xzw.shardingdemo.goods.dao.BroadTableDOMapper;
import com.xzw.shardingdemo.goods.dao.MyShardingOrderDOMapper;
import com.xzw.shardingdemo.goods.dao.MyUserMapper;
import com.xzw.shardingdemo.goods.dao.ShardingUserDOMapper;
import com.xzw.shardingdemo.goods.domain.MyUser;
import com.xzw.shardingdemo.goods.dto.BroadTableDO;
import com.xzw.shardingdemo.goods.dto.MyShardingOrderDO;
import io.seata.common.util.CollectionUtils;
import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.apache.shardingsphere.transaction.core.TransactionTypeHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RequestMapping
@RestController
public class ShardingTestController {

    @Autowired
    private MyShardingOrderDOMapper myShardingOrderDOMapper;

    @Autowired
    private BroadTableDOMapper broadTableDOMapper;

    @Autowired
    private MyUserMapper myUserMapper;

    @Autowired
    private ShardingUserDOMapper shardingUserDOMapper;

    /**
     * 单表新增 通过sharding jdbc 分库分表规则 插入对应的库、表
     *
     * @throws Exception
     */
    //@GlobalTransactional(timeoutMills = 20000,rollbackFor = Exception.class)
    @GetMapping("/testSingleAdd")
    //@ShardingTransactionType(TransactionType.BASE)
    void testSingleAdd() throws Exception {
        System.out.println("=================================start===============================");
        MyShardingOrderDO myShardingOrderDO = new MyShardingOrderDO();
        myShardingOrderDO.setStatus("主键自增测试" + 1);
        myShardingOrderDO.setUserId(8L);
        myShardingOrderDO.setOrderId(8L);
        myShardingOrderDO.setOrderNo("8");
        //TransactionTypeHolder.set(TransactionType.BASE);// 每一次操作数据库，都需要加上// 不然就不会当前的seata全局事务管理 todo 优化...
        myShardingOrderDOMapper.insertWithGenerator(myShardingOrderDO);
    }


    /**
     * 两表 新增 通过sharding jdbc 分库分表规则 插入对应的库、表 分别插入到eladmin 和 mall 两个库的 两个表中（两库四表）
     *
     * @throws Exception
     */
    @GetMapping("/testMultiAdd")
    void testMultiAdd() throws Exception {
        System.out.println("=================================start===============================");
        MyShardingOrderDO myShardingOrderDO = new MyShardingOrderDO();
        myShardingOrderDO.setStatus("主键自增测试" + 1);
        myShardingOrderDO.setUserId(9L);
        myShardingOrderDO.setOrderNo("9");
        //TransactionTypeHolder.set(TransactionType.BASE);// 每一次操作数据库，都需要加上// 不然就不会当前的seata全局事务管理 todo 优化...
        myShardingOrderDOMapper.insertWithGenerator(myShardingOrderDO);


        MyShardingOrderDO myShardingOrderDO2 = new MyShardingOrderDO();
        myShardingOrderDO2.setStatus("主键自增测试" + 2);
        myShardingOrderDO2.setUserId(10L);
        myShardingOrderDO2.setOrderNo("10");
        //TransactionTypeHolder.set(TransactionType.BASE);
        myShardingOrderDOMapper.insertWithGenerator(myShardingOrderDO2);
    }


    /**
     * 循环新增
     * 两表 新增 通过sharding jdbc 分库分表规则 插入对应的库、表 分别插入到eladmin 和 mall 两个库的 两个表中（两库四表）
     *
     * @throws Exception
     */
    @GetMapping("/testMultiForeachAdd")
    void testMultiForeachAdd() throws Exception {
        System.out.println("=================================start===============================");
        for (int i = 20; i < 31; i++) {
            MyShardingOrderDO myShardingOrderDO = new MyShardingOrderDO();
            myShardingOrderDO.setStatus("主键自增测试" + 1);
            myShardingOrderDO.setOrderId(Long.valueOf(i));
            myShardingOrderDO.setUserId(Long.valueOf(i) + 1);
            myShardingOrderDO.setOrderNo(String.valueOf(i));
            myShardingOrderDO.setStatus("OPEN");
            //TransactionTypeHolder.set(TransactionType.BASE);// 每一次操作数据库，都需要加上// 不然就不会当前的seata全局事务管理
            myShardingOrderDOMapper.insert(myShardingOrderDO);
        }
        for (int i = 31; i < 41; i++) {
            MyShardingOrderDO myShardingOrderDO = new MyShardingOrderDO();
            myShardingOrderDO.setStatus("主键自增测试" + i);
            myShardingOrderDO.setOrderId(Long.valueOf(i));
            myShardingOrderDO.setUserId(Long.valueOf(i));
            myShardingOrderDO.setOrderNo(String.valueOf(i));
            myShardingOrderDO.setStatus("OPEN");
            //TransactionTypeHolder.set(TransactionType.BASE);// 每一次操作数据库，都需要加上// 不然就不会当前的seata全局事务管理
            myShardingOrderDOMapper.insert(myShardingOrderDO);
        }
    }

    /**
     * 两表 新增 通过sharding jdbc 分库分表规则 插入对应的库、表 分别插入到eladmin 和 mall 两个库的 两个表中（两库四表）
     * 同时测试 分布式事务一致的问题，即加上SEATA @GlobalTransactional 注解后，第一张表数据处理成功后，在后续抛出异常
     * 第一张表数据应 回滚
     *
     * @throws Exception
     */
    @GlobalTransactional(timeoutMills = 20000, rollbackFor = Exception.class)
    //@ShardingTransactionType(TransactionType.BASE)
    @GetMapping("/testMultiAdd4Transaction")
    void testMultiAdd4Transaction() throws Exception {
        System.out.println("=================================start===============================");
        MyShardingOrderDO myShardingOrderDO = new MyShardingOrderDO();
        myShardingOrderDO.setStatus("主键自增测试" + 11);
        myShardingOrderDO.setUserId(11L);
        myShardingOrderDO.setOrderId(11L);
        myShardingOrderDO.setOrderNo("11");
        TransactionTypeHolder.set(TransactionType.BASE);// 每一次操作数据库，都需要加上// 不然就不会当前的seata全局事务管理 todo 优化...
        myShardingOrderDOMapper.insert(myShardingOrderDO);


        if (1 == 1) {
            throw new Exception("TEST GLOBAL TRANSACTION...");
        }
        MyShardingOrderDO myShardingOrderDO2 = new MyShardingOrderDO();
        myShardingOrderDO2.setStatus("主键自增测试" + 12);
        myShardingOrderDO2.setUserId(12L);
        myShardingOrderDO.setOrderId(12L);
        myShardingOrderDO2.setOrderNo("12");
        TransactionTypeHolder.set(TransactionType.BASE);
        myShardingOrderDOMapper.insert(myShardingOrderDO2);
    }

    /**
     * 通过其中一个字段 到对应库、表中查询数据 这里通过orderId（主键）
     *
     * @throws Exception
     */
    @GetMapping("/getShardingOrderByOrderId")
    void getShardingOrderByOrderId() throws Exception {
        System.out.println("=================================start===============================");
        MyShardingOrderDO shardingOrderDO = myShardingOrderDOMapper.selectByPrimaryKey(586571065375850497L);
        System.out.println(shardingOrderDO);
    }


    /**
     * 分页查询，通过order_id 排序 ，查询第一页，第二页...直到查到最后一页
     *
     * @throws Exception
     */
    @GetMapping("/findShardingOrderByUserPage")
    void findShardingOrderByUserPage() throws Exception {
        System.out.println("=================================start===============================");
        Integer fromSize = 0;
        for (int i = 0; i <= 10; i++) {
            fromSize = i * 5;
            List<MyShardingOrderDO> shardingOrderDO = myShardingOrderDOMapper.findShardingOrderByUserPage(fromSize);
            System.out.println(shardingOrderDO);
        }
    }


    /**
     * 通过非分库分表字段查询对应数据 两库四表
     *
     * @throws Exception
     */
    @GetMapping("/getShardingOrderByOtherColumn")
    void getShardingOrderByOtherColumn() throws Exception {
        System.out.println("=================================start===============================");
        MyShardingOrderDO shardingOrderDO4 = myShardingOrderDOMapper.selectByOrderNo("4");
        System.out.println(shardingOrderDO4);
        MyShardingOrderDO shardingOrderDO3 = myShardingOrderDOMapper.selectByOrderNo("3");
        System.out.println(shardingOrderDO3);
    }

    /**
     * 通过一个区间段 查询两库 4表 的 List
     *
     * @throws Exception
     */
    @GetMapping("/getShardingOrderListByOrderRange")
    void getShardingOrderListByOrderRange() throws Exception {
        System.out.println("=================================start===============================");
        List<MyShardingOrderDO> shardingOrderDOList = myShardingOrderDOMapper.selectShardingOrderListByOrderRange();
        if (!CollectionUtils.isEmpty(shardingOrderDOList)) {
            System.out.println(shardingOrderDOList);
        }

    }

    /**
     * 操作公共表 插入一条数据 应在两库 两表中各有一条
     *
     * @throws Exception
     */
    @GetMapping("/insertBroadTable")
    void insertBroadTable() throws Exception {
        System.out.println("=================================start===============================");
        BroadTableDO broadTableDO = new BroadTableDO();
        broadTableDO.setId(1L);
        broadTableDO.setCode("B1");
        broadTableDO.setEnable(Boolean.TRUE);
        TransactionTypeHolder.set(TransactionType.BASE);
        broadTableDOMapper.insert(broadTableDO);
    }

    /**
     * 用户表，分两个库，各1个表
     *
     * @throws Exception
     */
    @GetMapping("/insertMyUser")
    void insertMyUser() throws Exception {
        System.out.println("=================================start===============================");
        MyUser myUserDO = new MyUser();
        myUserDO.setUserId(4L);
        myUserDO.setUserName("FOUR");
        TransactionTypeHolder.set(TransactionType.BASE);
        myUserMapper.insert(myUserDO);
    }

    /**
     * 关联查询 USER表两库各1表 SHARDING_ORDER 两库 各2表
     *
     * @throws Exception
     */
    @GetMapping("/selectOrderAndUserInfo")
    void selectOrderAndUserInfo() throws Exception {
        System.out.println("=================================start===============================");
        List<MyShardingOrderDO> shardingOrderDOList = shardingUserDOMapper.selectOrderAndUserInfo();
        if (!CollectionUtils.isEmpty(shardingOrderDOList)) {
            System.out.println(shardingOrderDOList.size());
        }
    }

    /**
     * 调用其他 微服务 并落实事务一致
     * 待测试 todo 需另起一个服务 并调用，收到返回结果，继续处理本地事务，出现异常，本事务和调用另一个服务的事务均应回滚
     */
    @GetMapping("/invokeHttpHostApi")
    @GlobalTransactional
//(timeoutMills = 20000,rollbackFor = Exception.class)
    void invokeHttpHostApi() throws Exception {
        System.out.println("=================================start===============================");
        MyShardingOrderDO myShardingOrderDO = new MyShardingOrderDO();
        myShardingOrderDO.setStatus("主键自增测试" + 1);
        myShardingOrderDO.setUserId(16L);
        myShardingOrderDO.setOrderNo("16");
        TransactionTypeHolder.set(TransactionType.BASE);// 每一次操作数据库，都需要加上// 不然就不会当前的seata全局事务管理 todo 优化...
        myShardingOrderDOMapper.insertWithGenerator(myShardingOrderDO);
        httprequest();
        if (1 == 1) {
            System.out.println("=============================test===============================");
            int x = 1 / 0;
        }
        System.out.println("================================= end ==============================");
    }

    /**
     * 调用其他服务,验证跨服务的分布式事务，这里就启动两个服务,其中一个调用另一个来实验
     */
    private void httprequest() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        System.out.println("RootContext.getXID():" + RootContext.getXID());
        headers.add("TX_XID".toLowerCase(), RootContext.getXID());
        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);
        String url = "http://127.0.0.1:8098/testHttpSingleAdd";
        ResponseEntity<String> resEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);
        System.out.println("response=============================:" + resEntity.toString());
    }


}
